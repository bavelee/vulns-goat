package io.easygoat;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.net.URLDecoder;

/**
 * @author: bavelee
 * @date: 2021/11/13 13:53:58
 */
public class JARUtils {
    public static String getJarContainingFolder() {
        try {
            String path = URLDecoder.decode(JARUtils.class.getProtectionDomain().getCodeSource().getLocation().getPath(), "UTF-8");
            if (path.startsWith("file:")) {
                path = path.replace("file:", "");
            }
            File file = null;
            if (path.contains("!")) {
                file = new File(path.substring(0, path.indexOf("!")));
            } else {
                file = new File(path);
            }
            return URLDecoder.decode(file.isDirectory() ? file.getPath() : file.getParent(), "UTF-8");
        } catch (Exception ignore) {
        }
        return null;
    }

    public static String getAppBasePath(HttpServletRequest request) {
        return request.getServletContext().getRealPath("/");
    }

    public static String getAppBaseFile(HttpServletRequest request, String path) {
        return getAppBasePath(request) + "/" + path;
    }
}
