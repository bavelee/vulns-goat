package io.easygoat.annos;

import io.easygoat.enums.ContentType;

import java.lang.annotation.*;

/**
 * @author bavelee
 * @date 2021/12/31 09:47:02
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface GoatCase {
    /**
     * 用例标题
     */
    String title() default "";

    /**
     * 描述
     */
    String description() default "";

    /**
     * 期望结果
     */
    String[] expects() default {};

    /**
     * 请求的ContentType
     */
    ContentType contentType() default ContentType.NOT_SET;

    Param[] params() default {};

    /**
     * 请求 Body 字符串，不会被编码
     */
    String body() default "";

    @Deprecated
    String cURL() default "";

    @Deprecated
    String expect() default "";

}
