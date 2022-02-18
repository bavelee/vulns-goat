package io.easygoat;

import io.easygoat.annos.GoatCase;
import io.easygoat.annos.GoatCategory;
import io.easygoat.enums.Category;
import io.easygoat.util.GoatCaseParser;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.*;

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

    @Autowired
    private GoatCaseParser parser;


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Map<CategoryInfo, List<CaseInfo>> casesMap = GoatCaseScanner.getGoatCasesMap();
        for (String beanName : applicationContext.getBeanDefinitionNames()) {
            if ("easyGoatScanner".equals(beanName)) {
                continue;
            }
            Object bean = applicationContext.getBean(beanName);
            Class<?> targetClass = bean.getClass();
            if (org.springframework.aop.support.AopUtils.isAopProxy(bean)) {
                targetClass = org.springframework.aop.support.AopUtils.getTargetClass(bean);
            }
            CategoryInfo category = Category.UNKNOWN.get();
            if (targetClass.isAnnotationPresent(GoatCategory.class)) {
                GoatCategory annotation = targetClass.getAnnotation(GoatCategory.class);
                if (annotation.type() == Category.UNKNOWN) {
                    String name = annotation.name();
                    String desc = annotation.desc();
                    category = new CategoryInfo(name, desc);
                } else {
                    category = annotation.type().get();
                }
                System.out.println("scan " + targetClass.getName() + " => " + category.getName());
            }
            PathInfo basePathInfo = getMappingPath(targetClass);
            for (Method m : targetClass.getDeclaredMethods()) {
                if (m.isAnnotationPresent(GoatCase.class)) {
                    GoatCase goatCase = m.getAnnotation(GoatCase.class);
                    if (!casesMap.containsKey(category)) {
                        casesMap.put(category, new LinkedList<>());
                    }
                    List<CaseInfo> caseInfoList = casesMap.get(category);
                    PathInfo pathInfo = getMappingPath(m);
                    CaseInfo caseInfo = parser.parse(basePathInfo, pathInfo, goatCase);
                    caseInfoList.add(caseInfo);
                    System.out.println("  add case " + goatCase.title());
                }
//                if (c != null && m.isAnnotationPresent(GoatVar.class)) {
//                    GoatVar goatVar = m.getAnnotation(GoatVar.class);
//                    File file = null;
//                    switch (goatVar.type()) {
//                        case READABLE_DIR:
//                            String dir = JARUtils.getJarContainingFolder();
//                            if (dir == null) {
//                                System.err.println("jar container is null");
//                                continue;
//                            }
//                            c.setcURL(c.getcURL().replace(goatVar.var(), dir));
//                            break;
//                        case CREATE_FILE:
//                            file = new File(goatVar.value());
//                            try {
//                                file.createNewFile();
//                                System.out.println("     create file : " + file.getAbsolutePath());
//                                c.setcURL(c.getcURL().replace(goatVar.var(), goatVar.value()));
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            }
//                            break;
//                        case TEMP_FILE:
//                            String value = goatVar.value();
//                            int dot = value.indexOf(".");
//                            String prefix = value.substring(0, dot);
//                            String suffix = value.substring(dot);
//                            try {
//                                file = File.createTempFile(prefix, suffix);
//                                c.setcURL(c.getcURL().replace(goatVar.var(), file.getPath()));
//                                System.out.println("     set var " + goatVar.var() + " to " + file.getPath());
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            }
//                            break;
//                    }
//                }
            }
            List<CaseInfo> caseInfos = casesMap.get(category);
            if (caseInfos != null) {
                caseInfos.sort(caseSort);
            }
        }
    }

    private PathInfo getMappingPath(Class<?> targetClass) {
        PathInfo pathInfo = new PathInfo();
        pathInfo.setMethod(RequestMethod.ALL);
        if (targetClass.isAnnotationPresent(RequestMapping.class)) {
            RequestMapping mapping = targetClass.getAnnotation(RequestMapping.class);
            pathInfo.setPath(mapping.path().length > 0 ? mapping.path()[0] : (mapping.value().length > 0 ? mapping.value()[0] : null));
            pathInfo.setMethod(RequestMethod.ALL);
        }
        return pathInfo;
    }

    private PathInfo getMappingPath(Method method) {
        PathInfo pathInfo = new PathInfo();
        pathInfo.setMethod(RequestMethod.ALL);
        if (method.isAnnotationPresent(RequestMapping.class)) {
            RequestMapping mapping = method.getAnnotation(RequestMapping.class);
            pathInfo.setPath(mapping.path().length > 0 ? mapping.path()[0] : (mapping.value().length > 0 ? mapping.value()[0] : null));
            pathInfo.setMethod(RequestMethod.ALL);
        } else if (method.isAnnotationPresent(GetMapping.class)) {
            GetMapping mapping = method.getAnnotation(GetMapping.class);
            pathInfo.setPath(mapping.path().length > 0 ? mapping.path()[0] : (mapping.value().length > 0 ? mapping.value()[0] : null));
            pathInfo.setMethod(RequestMethod.GET);
        } else if (method.isAnnotationPresent(PostMapping.class)) {
            PostMapping mapping = method.getAnnotation(PostMapping.class);
            pathInfo.setPath(mapping.path().length > 0 ? mapping.path()[0] : (mapping.value().length > 0 ? mapping.value()[0] : null));
            pathInfo.setMethod(RequestMethod.POST);
        } else if (method.isAnnotationPresent(PutMapping.class)) {
            PutMapping mapping = method.getAnnotation(PutMapping.class);
            pathInfo.setPath(mapping.path().length > 0 ? mapping.path()[0] : (mapping.value().length > 0 ? mapping.value()[0] : null));
            pathInfo.setMethod(RequestMethod.PUT);
        } else if (method.isAnnotationPresent(DeleteMapping.class)) {
            DeleteMapping mapping = method.getAnnotation(DeleteMapping.class);
            pathInfo.setPath(mapping.path().length > 0 ? mapping.path()[0] : (mapping.value().length > 0 ? mapping.value()[0] : null));
            pathInfo.setMethod(RequestMethod.DELETE);
        }
        return pathInfo;
    }

    static class CaseSort implements Comparator<CaseInfo> {
        @Override
        public int compare(CaseInfo o1, CaseInfo o2) {
//            return o1.getcURL().compareTo(o2.getcURL());
            return o1.getTitle().compareTo(o2.getTitle());
        }
    }
}
