package io.easygoat.annos;

import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = {
        "io.easygoat"
})
@ConditionalOnWebApplication
public @interface EasyGoatConfiguration {
}
