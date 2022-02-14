package io.easygoat;

import io.easygoat.annos.GoatCase;
import io.easygoat.annos.GoatCategory;
import io.easygoat.annos.GoatVar;
import io.easygoat.enums.Category;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.*;


/**
 * @author: bavelee
 * @date: 2021/12/31 09:46:21
 */
@Configuration("easyGoatScanner")
public class GoatCaseScanner implements ApplicationContextAware {

    private static final Map<CategoryInfo, List<CaseInfo>> goatCasesMap = new TreeMap<>();
    private final CaseSort caseSort = new CaseSort();

    public static Map<CategoryInfo, List<CaseInfo>> getGoatCasesMap() {
        return goatCasesMap;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Map<CategoryInfo, List<CaseInfo>> casesMap = GoatCaseScanner.getGoatCasesMap();
        for (String beanName : applicationContext.getBeanDefinitionNames()) {
            if ("easyGoatScanner".equals(beanName)) {
                continue;
            }
            Object obj = applicationContext.getBean(beanName);
            Class<?> objClz = obj.getClass();
            if (org.springframework.aop.support.AopUtils.isAopProxy(obj)) {
                objClz = org.springframework.aop.support.AopUtils.getTargetClass(obj);
            }
            CategoryInfo category = Category.UNKNOWN.get();
            if (objClz.isAnnotationPresent(GoatCategory.class)) {
                GoatCategory annotation = objClz.getAnnotation(GoatCategory.class);
                if (annotation.type() == Category.UNKNOWN) {
                    String name = annotation.name();
                    String desc = annotation.desc();
                    category = new CategoryInfo(name, desc);
                } else {
                    category = annotation.type().get();
                }
                System.out.println("scan " + objClz.getName() + " => " + category.getName());
            }
            for (Method m : objClz.getDeclaredMethods()) {
                CaseInfo c = null;
                if (m.isAnnotationPresent(GoatCase.class)) {
                    GoatCase goatCase = m.getAnnotation(GoatCase.class);
                    if (!casesMap.containsKey(category)) {
                        casesMap.put(category, new LinkedList<>());
                    }
                    List<CaseInfo> caseInfoList = casesMap.get(category);
                    c = new CaseInfo(goatCase.title(), goatCase.cURL(), goatCase.expect());
                    caseInfoList.add(c);
                    System.out.println("  add case " + goatCase.title());
                }
                if (c != null && m.isAnnotationPresent(GoatVar.class)) {
                    GoatVar goatVar = m.getAnnotation(GoatVar.class);
                    File file = null;
                    switch (goatVar.type()) {
                        case READABLE_DIR:
                            String dir = JARUtils.getJarContainingFolder();
                            if (dir == null) {
                                System.err.println("jar container is null");
                                continue;
                            }
                            c.setcURL(c.getcURL().replace(goatVar.var(), dir));
                            break;
                        case CREATE_FILE:
                            file = new File(goatVar.value());
                            try {
                                file.createNewFile();
                                System.out.println("     create file : " + file.getAbsolutePath());
                                c.setcURL(c.getcURL().replace(goatVar.var(), goatVar.value()));
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            break;
                        case TEMP_FILE:
                            String value = goatVar.value();
                            int dot = value.indexOf(".");
                            String prefix = value.substring(0, dot);
                            String suffix = value.substring(dot);
                            try {
                                file = File.createTempFile(prefix, suffix);
                                c.setcURL(c.getcURL().replace(goatVar.var(), file.getPath()));
                                System.out.println("     set var " + goatVar.var() + " to " + file.getPath());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            break;
                    }
                }
            }
            List<CaseInfo> caseInfos = casesMap.get(category);
            if (caseInfos != null) {
                caseInfos.sort(caseSort);
            }
        }
    }

    static class CaseSort implements Comparator<CaseInfo> {
        @Override
        public int compare(CaseInfo o1, CaseInfo o2) {
            return o1.getcURL().compareTo(o2.getcURL());
        }
    }
}
