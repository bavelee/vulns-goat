package io.easygoat.annos;

import java.lang.annotation.*;

/**
 * @author: bavelee
 * @date: 2021/12/31 09:47:02
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface GoatCase {
    String title() default "Title_1";

    String cURL() default "-X GET http://localhost:8080";

    String expect() default "一个命令注入被RASP拦截";

}
