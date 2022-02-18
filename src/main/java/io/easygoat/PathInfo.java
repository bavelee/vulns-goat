package io.easygoat;

/**
 * @author bavelee
 * @date 2022/02/15 23:07
 */
public class PathInfo {
    private String path;
    private RequestMethod method;

    public PathInfo() {
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public RequestMethod getMethod() {
        return method;
    }

    public void setMethod(RequestMethod method) {
        this.method = method;
    }
}
