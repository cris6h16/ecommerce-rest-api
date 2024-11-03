package org.cris6h16;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

// manualmente excluido la config de seguridad no deberia pasar pero la componucacion entre components no se da por controllers es por eso q se incluye sola ( al importar el module )
@SpringBootApplication(scanBasePackageClasses = UserController.class, exclude = SecurityAutoConfiguration.class)
public class Main {
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

}
