package io.easygoat.annos;

import io.easygoat.enums.Category;

import java.lang.annotation.*;

/**
 * @author: bavelee
 * @date: 2021/12/31 09:48:30
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface GoatCategory {

    Category type() default Category.UNKNOWN;

    String name() default "";

    String desc() default "";

}
