package io.vulns.goat.api;


import io.easygoat.annos.GoatCase;
import io.easygoat.annos.GoatCategory;
import io.easygoat.annos.Param;
import io.easygoat.enums.ContentType;
import io.easygoat.enums.ParamType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletInputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;

/**
 * XSS goat cases.
 */
@RestController
@RequestMapping("xss")
@GoatCategory(name = "跨站脚本攻击", desc = "Cross Site Scripting")
public class XSSApi {
    @GoatCase(title = "反射型XSS：最简单的XSS",
            description = "这是描述啊，这是",
            expects = {"111", "aaa"},
            contentType = ContentType.TEXT_PLAIN,
            params = {
                    @Param(key = "param1", value = "<script>alert(\"You has been attack!\")</script>", type = ParamType.QUERYSTRING),
                    @Param(key = "param2", value = "<script>alert(\"Hello, how are you!\")</script>", type = ParamType.URL_ENCODED)
            }
    )
    @RequestMapping(value = "reflected-xss-1", produces = "text/html; utf-8")
    public String xss1(HttpServletRequest request) throws Exception {
        return parseRequest(request);
    }

    private String parseRequest(HttpServletRequest request) throws IOException {
        StringBuilder sb = new StringBuilder();
        Enumeration<String> headerNames = request.getHeaderNames();
        sb.append("=========== Request Info Start ==========\n");
        while (headerNames.hasMoreElements()) {
            String key = headerNames.nextElement();
            String value = request.getHeader(key);
            sb.append("Header =>  ").append(key).append(" : ").append(value).append("\n");
        }
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {

            for (Cookie cookie : cookies) {
                String key = cookie.getName();
                String value = cookie.getValue();
                sb.append("Cookie =>  ").append(key).append(" : ").append(value).append("\n");
            }
        }
        Enumeration<String> parameterNames = request.getParameterNames();
        while (parameterNames.hasMoreElements()) {
            String key = parameterNames.nextElement();
            String value = request.getParameter(key);
            sb.append("Param =>  ").append(key).append(" : ").append(value).append("\n");
        }
        try {
            ServletInputStream content = request.getInputStream();
            byte[] buf = new byte[1024];
            int len = 0;
            while ((len = content.read(buf)) > 0) {
                sb.append(new String(buf, 0, len, StandardCharsets.UTF_8));
            }
            content.close();
        } catch (Exception ignore) {

        }
        sb.append("\n=========== Request Info End ==========\n");
        return sb.toString();
    }
}
