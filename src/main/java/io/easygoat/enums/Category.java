package io.easygoat.enums;

import io.easygoat.CategoryInfo;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: bavelee
 * @date: 2021/12/31 10:00:39
 */
public enum Category {
    UNKNOWN("未分类", "未分类"),
    COMMAND_INJECTION("命令注入", "命令注入"),
    SQL_INJECTION("SQL注入", "SQL注入"),
    SQL_EXCEPTION("SQL异常", "SQL异常"),
    XSS("XSS", "XSS反射"),
    FILE_LINK("文件链接"),
    FILE_READ("文件读取", "暂不可用，需要保证文件可读，较为苛刻"),
    FILE_RENAME("文件重命名"),
    FILE_WRITE("文件写入"),
    FILE_DELETE("文件删除"),
    FILE_UPLOAD("文件上传", "Hook点没进入"),
    INCLUDE("Include"),
    LOAD_LIBRARY("类库加载", "暂无插件"),
    PATH_TRAVERSAL("目录遍历"),
    OGNL("OGNL表达式"),
    XXE("XXE", "外部实体注入"),
    DESERIALIZATION("反序列化"),
    SSRF("SSRF");
    private static final Map<String, CategoryInfo> infoMap = new HashMap<>();
    private String name;
    private String desc;

    Category(String name) {
        this.name = name;
        this.desc = name;
    }

    Category(String name, String desc) {
        this.name = name;
        this.desc = desc;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public CategoryInfo get() {
        infoMap.putIfAbsent(name, new CategoryInfo(name, desc));
        return infoMap.get(name);
    }

}
