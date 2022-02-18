package io.easygoat;

import io.easygoat.enums.ContentType;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: bavelee
 * @date: 2021/12/31 14:22:50
 */
@Data
public class CaseInfo {
    private String url;
    private String title;
    @Deprecated
    private String cURL;
    @Deprecated
    private String expect;
    private String[] expects;
    private String description;
    private ContentType contentType;
    private String body;
    private RequestMethod method;
    private final Map<String, String> urlEncodedParams = new HashMap<>();
    private final Map<String, String> headers = new HashMap<>();
    private final Map<String, String> cookies = new HashMap<>();
    private final Map<String, Object> formParams = new HashMap<>();

    public CaseInfo() {
    }

    @Override
    public String toString() {
        StringBuilder text = new StringBuilder(1024);
        text.append("      title : ").append(title).append("\n");
        if (expects != null && expects.length > 0) {
            text.append("    expects : ").append(expects.length).append("\n");
            for (String expect : expects) {
                text.append("              - ").append(expect).append("\n");
            }
        }
        text.append("     method : ").append(method).append("\n");
        text.append("contextType : ").append(contentType).append("\n");
        text.append("        url : ").append(url).append("\n");
        if (!urlEncodedParams.isEmpty()) {
            text.append(" url params : ").append("\n");
            for (Map.Entry<String, String> entry : urlEncodedParams.entrySet()) {
                text.append("              - ").append(entry.getKey()).append(" :").append(entry.getValue()).append("\n");
            }
        }
        if (!formParams.isEmpty()) {
            text.append("form params : ").append("\n");
            for (Map.Entry<String, Object> entry : formParams.entrySet()) {
                text.append("              - ").append(entry.getKey()).append(" : ");
                Object value = entry.getValue();
                if (value instanceof String) {
                    text.append(value).append("\n");
                } else {
                    text.append("<").append(value).append(">").append("\n");
                }
            }
        }
        if (body != null) {
            text.append("       body : ").append(body).append("\n");
        }
        return text.toString();
    }
}
