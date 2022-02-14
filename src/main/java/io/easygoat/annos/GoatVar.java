package io.easygoat.annos;

import io.easygoat.enums.VarType;

import java.lang.annotation.*;

/**
 * @author: bavelee
 * @date: 2021/12/31 14:13:11
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface GoatVar {
    String var();

    String value() default "";

    VarType type();
}
