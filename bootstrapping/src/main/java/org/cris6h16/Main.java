package org.cris6h16;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication(
        scanBasePackages = "org.cris6h16"
)
public class Main {
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
}
