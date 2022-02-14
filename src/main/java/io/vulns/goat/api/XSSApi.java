package io.vulns.goat.api;


import io.easygoat.annos.GoatCase;
import io.easygoat.annos.GoatCategory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * XSS goat cases.
 */
@RestController
@RequestMapping("xss")
@GoatCategory(name = "跨站脚本攻击", desc = "Cross Site Scripting")
public class XSSApi {
    @GoatCase(title = "反射型XSS：最简单的XSS",
            cURL = "curl -X POST http://localhost:8000/xss/reflected-xss-1 -H \"Content-Type: application/x-www-form-urlencoded\" -d 'param1=<script>alert(\"You has been attack!\")</script>'",
            expect = "检测到一个反射型XSS漏洞")
    @RequestMapping(value = "reflected-xss-1", produces = "text/html; utf-8")
    public String xss1(@RequestParam("param1") String param1) {
        return "Request param1 is : " + param1 + "\n";
    }
}
