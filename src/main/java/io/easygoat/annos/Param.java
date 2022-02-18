package io.easygoat.annos;


import io.easygoat.enums.ParamType;

import java.lang.annotation.*;

/**
 * @author bavelee
 * @date 2022/02/15 22:32
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Param {
    String key() default "";

    String value();

    ParamType type() default ParamType.QUERYSTRING;
}
