package io.easygoat.enums;


public enum ContentType {

    APPLICATION_FORM_URLENCODED("application/x-www-form-urlencoded", "utf-8"),
    APPLICATION_JSON("application/json", "utf-8"),
    APPLICATION_OCTET_STREAM("application/octet-stream"),
    APPLICATION_SOAP_XML("application/soap+xml", "utf-8"),
    APPLICATION_SVG_XML("application/svg+xml", "utf-8"),
    APPLICATION_XHTML_XML("application/xhtml+xml", "utf-8"),
    APPLICATION_XML("application/xml", "utf-8"),
    IMAGE_BMP("image/bmp"),
    IMAGE_GIF("image/gif"),
    IMAGE_JPEG("image/jpeg"),
    IMAGE_PNG("image/png"),
    IMAGE_SVG("image/svg+xml"),
    IMAGE_TIFF("image/tiff"),
    IMAGE_WEBP("image/webp"),
    MULTIPART_FORM_DATA("multipart/form-data", "utf-8"),
    TEXT_HTML("text/html", "utf-8"),
    TEXT_PLAIN("text/plain", "utf-8"),
    TEXT_XML("text/xml", "utf-8"),
    WILDCARD("*/*"),
    NOT_SET("");

    private final String mimeType;
    private final String charset;

    ContentType(String mimeType, String charset) {
        this.mimeType = mimeType;
        this.charset = charset;
    }

    ContentType(String mimeType) {
        this.mimeType = mimeType;
        this.charset = null;
    }

    public String getMimeType() {
        return mimeType;
    }

    public String getCharset() {
        return charset;
    }

    @Override
    public String toString() {
        if (charset == null) {
            return mimeType + "; ";
        } else {
            return mimeType + "; charset=" + charset;
        }
    }
}
