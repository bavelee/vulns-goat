package io.vulns.goat;

import io.easygoat.annos.EnableEasyGoat;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableEasyGoat
public class VulnsGoatApplication {

    public static void main(String[] args) {
        SpringApplication.run(VulnsGoatApplication.class, args);
    }

}
