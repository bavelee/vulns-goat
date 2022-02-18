package io.easygoat;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.toilelibre.libe.curl.Curl;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.io.Writer;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author: bavelee
 * @date: 2021/12/31 09:39:29
 */
@RestController
public class EasyGoatApi {

    private List<CategoryInfo> categoryInfos = null;
    private final List<String> curlList = new ArrayList<>(64);

    @RequestMapping(value = "/", produces = "text/html; charset=utf-8")
    public String index(@RequestParam(name = "cat", defaultValue = "0") Integer cat) {
        curlList.clear();
        if (categoryInfos == null) {
            synchronized (EasyGoatApi.class) {
                categoryInfos = new ArrayList<>(GoatCaseScanner.getGoatCasesMap().keySet());
            }
        }
        StringBuilder sb = new StringBuilder();
        CategoryInfo category = categoryInfos.get(cat);
        Map<CategoryInfo, List<CaseInfo>> goatCasesMap = GoatCaseScanner.getGoatCasesMap();
        for (int i = 0; i < categoryInfos.size(); i++) {
            CategoryInfo info = categoryInfos.get(i);
            sb.append("<a href=?cat=").append(i).append(">");
            sb.append("<strong>");
            sb.append(info.getName());
            sb.append("</strong>");
            sb.append("</a>");
            sb.append("&nbsp&nbsp(");
            sb.append(info.getDesc());
            sb.append(")<br>");
        }
        List<CaseInfo> caseInfos = goatCasesMap.get(category);
        sb.append("<br><br><br>");
        sb.append("<h1>");
        sb.append(category.getName());
        sb.append("</h1>");
        sb.append(category.getDesc());
        sb.append("<br>");
        sb.append("<br>");
//        for (CaseInfo goatCaseInfo : caseInfos) {
//            curlList.add(goatCaseInfo.getcURL().replace("curl ", ""));
//            sb.append("<b>")
//                    .append(goatCaseInfo.getTitle())
//                    .append("</b>")
//                    .append("<br>")
//                    .append("<a target='_blank' href=")
//                    .append(curlList.size() - 1)
//                    .append(">执行</a>")
//                    .append("<br><pre><xmp>")
//                    .append(goatCaseInfo.getcURL())
//                    .append("</xmp></pre><br>")
//                    .append("预期结果：")
//                    .append(goatCaseInfo.getExpect().replace("\n", "<br>"))
//                    .append("<br>")
//                    .append("<br>");
//        }
        sb.append("<br>");
        sb.append("<br>");
        sb.append("=====================================================");
        sb.append("<br>");
        sb.append("<br>");
        return sb.toString();
    }

    @RequestMapping(value = "{i}")
    public void runCurl(@PathVariable int i, HttpServletResponse response) throws Exception {
        if (i < 0 || i >= curlList.size()) {
            response.sendRedirect("/");
            return;
        }
        String originCurl = curlList.get(i);
        String curl = originCurl;
        int lastIndex = curl.indexOf('=');
        if (lastIndex++ > 0) {
            char last = curl.toCharArray()[curl.length() - 1];
            String s;
            if (last == '\'' || last == '"') {
                s = curl.substring(lastIndex, curl.length() - 1);
            } else {
                s = curl.substring(lastIndex);
            }
            String encode = URLEncoder.encode(s, "UTF-8");
            curl = curl.replace(s, encode);
        }
        HttpResponse httpResponse = Curl.curl(curl);
        HttpEntity entity = httpResponse.getEntity();
        Header contentType = entity.getContentType();
        response.setContentType(contentType == null ? "text/plain" : contentType.getValue());
        Header encoding = entity.getContentEncoding();
        response.setCharacterEncoding(encoding == null ? "utf-8" : encoding.getValue());
        response.setStatus(httpResponse.getStatusLine().getStatusCode());
        InputStream stream = entity.getContent();
        byte[] buf = new byte[1024];
        int len;
        try (Writer writer = response.getWriter()) {
            while ((len = stream.read(buf)) > 0) {
                writer.write(new String(buf, 0, len));
            }
            writer.flush();
        }
//        StringBuilder sb = new StringBuilder();
//        sb.append("cURL          : ").append(originCurl).append("<br><br>========= 响应报文 =========<br><br>");
//        sb.append(Curl.$(curl));
//        sb.append(httpResponse.getStatusLine());
//        sb.append(httpResponse.getEntity());
    }
}
