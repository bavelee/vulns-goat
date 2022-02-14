package io.easygoat;

/**
 * @author: bavelee
 * @date: 2021/12/31 14:22:50
 */
public class CaseInfo {
    private String title;
    private String cURL;
    private String expect;

    public CaseInfo(String title, String cURL, String expect) {
        this.title = title;
        this.cURL = cURL;
        this.expect = expect;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getcURL() {
        return cURL;
    }

    public void setcURL(String cURL) {
        this.cURL = cURL;
    }

    public String getExpect() {
        return expect;
    }

    public void setExpect(String expect) {
        this.expect = expect;
    }
}
