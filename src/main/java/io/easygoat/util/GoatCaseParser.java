package io.easygoat.util;

import io.easygoat.CaseInfo;
import io.easygoat.Constants;
import io.easygoat.PathInfo;
import io.easygoat.RequestMethod;
import io.easygoat.annos.GoatCase;
import io.easygoat.annos.Param;
import io.easygoat.enums.ParamType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author bavelee
 * @date 2022/02/16 19:40
 */
@Component
public class GoatCaseParser {

    @Value("${server.port}")
    private String port;
    @Value("${server.servlet.context-path}")
    private String contextPath;

    public CaseInfo parse(PathInfo classPathInfo, PathInfo methodPathInfo, GoatCase goatCase) {
        CaseInfo ci = new CaseInfo();
        ci.setContentType(goatCase.contentType());
        ci.setTitle(goatCase.title());
        ci.setExpects(goatCase.expects());
        ci.setDescription(goatCase.description());
        if (!goatCase.body().isEmpty()) {
            ci.setBody(goatCase.body());
        }
        StringBuilder urlBuilder = new StringBuilder("http://").append(Constants.HOST_PLACE_HOLDER).append(":").append(port);
        if (contextPath != null && !contextPath.isEmpty()) {
            urlBuilder.append(contextPath);
        }
        if (classPathInfo != null && classPathInfo.getPath() != null) {
            urlBuilder.append(classPathInfo.getPath().isEmpty() ? "/" : classPathInfo.getPath());
        }
        if (methodPathInfo == null) {
            throw new IllegalArgumentException("用例路径不能为空");
        }
        ci.setMethod(methodPathInfo.getMethod());
        if (methodPathInfo.getPath() != null) {
            if (urlBuilder.charAt(urlBuilder.length() - 1) == '/') {
                urlBuilder.append(methodPathInfo.getPath());
            } else {
                urlBuilder.append("/").append(methodPathInfo.getPath());
            }
        }
        boolean appendedQueryString = false;
        boolean appendedUrlEncoded = false;
        StringBuilder bodyBuilder = null;
        for (Param param : goatCase.params()) {
            ParamType type = param.type();
            if (type.equals(ParamType.QUERYSTRING)) {
                urlBuilder.append(appendedQueryString ? "&" : "?");
                appendedQueryString = true;
                urlBuilder.append(param.key()).append("=").append(EncodeUtils.encode(param.value()));
            } else if (type.equals(ParamType.URL_ENCODED)) {
                if (bodyBuilder == null) {
                    bodyBuilder = new StringBuilder();
                }
                if (appendedUrlEncoded) {
                    bodyBuilder.append("&");
                }
                appendedUrlEncoded = true;
                bodyBuilder.append(param.key()).append("=").append(param.value());
            } else if (type.equals(ParamType.FORM)) {
                ci.getFormParams().put(param.key(), param.value());
            } else if (type.equals(ParamType.COOKIE)) {
                ci.getCookies().put(param.key(), param.value());
            } else if (type.equals(ParamType.HEADER)) {
                ci.getHeaders().put(param.key(), param.value());
            } else if (type.equals(ParamType.BODY)) {
                if (bodyBuilder == null) {
                    bodyBuilder = new StringBuilder();
                }
                bodyBuilder.append(param.value());
            }
        }
        ci.setUrl(urlBuilder.toString());
        if (!methodPathInfo.getMethod().equals(RequestMethod.GET) && bodyBuilder != null) {
            ci.setBody(bodyBuilder.toString());
        }
        return ci;
    }

}
