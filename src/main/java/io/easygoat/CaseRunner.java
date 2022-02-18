package io.easygoat;

import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.fluent.Response;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;

import java.io.File;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * @author bavelee
 * @date 2022/02/16 22:24
 */
public class CaseRunner {


    public static void run(CaseInfo ci) {
        try {
            Request request = null;
            String url = ci.getUrl();
            switch (ci.getMethod()) {
                case GET:
                    request = Request.Get(url);
                    break;
                case PUT:
                    request = Request.Put(url);
                    break;
                case DELETE:
                    request = Request.Delete(url);
                    break;
                case POST:
                default:
                    request = Request.Post(url);
                    break;
            }
            if (!ci.getContentType().equals(io.easygoat.enums.ContentType.NOT_SET) && !ci.getContentType().equals(io.easygoat.enums.ContentType.MULTIPART_FORM_DATA)) {
                request.setHeader("Content-Type", ci.getContentType().toString());
            }
            if (!ci.getHeaders().isEmpty()) {
                ci.getHeaders().forEach(request::addHeader);
            }
            if (!ci.getCookies().isEmpty()) {
                StringBuilder cookies = new StringBuilder();
                for (Map.Entry<String, String> entry : ci.getCookies().entrySet()) {
                    cookies.append(entry.getKey()).append("=").append(entry.getValue()).append(";");
                }
                request.addHeader("Cookie", cookies.toString());
            }
            if (!ci.getMethod().equals(RequestMethod.GET)) {
                if (ci.getBody() != null) {
                    ContentType contentType = ContentType.create(ci.getContentType().getMimeType(), ci.getContentType().getCharset());
                    request.bodyString(ci.getBody(), contentType);
                } else if (!ci.getFormParams().isEmpty()) {
                    MultipartEntityBuilder body = MultipartEntityBuilder.create();
                    for (Map.Entry<String, Object> entry : ci.getFormParams().entrySet()) {
                        String key = entry.getKey();
                        Object value = entry.getValue();
                        if (value instanceof String) {
                            body.addTextBody(key, (String) value);
                        } else if (value instanceof File) {
                            File file = (File) value;
                            body.addBinaryBody(key, file);
                        } else {
                            throw new IllegalArgumentException("不支持的multipart/form类型 : " + value.getClass().getName());
                        }
                    }
                    request.body(body.build());
                }
            }
            Response response = request.execute();
            HttpResponse httpResponse = response.returnResponse();
            StatusLine statusLine = httpResponse.getStatusLine();
            byte[] buf = new byte[1024];
            int len = 0;
            InputStream content = httpResponse.getEntity().getContent();
            System.out.println(statusLine.getStatusCode());
            while ((len = content.read(buf)) > 0) {
                System.out.println(new String(buf, 0, len, StandardCharsets.UTF_8));
            }
            content.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
