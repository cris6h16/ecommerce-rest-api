package org.cris6h16;

import lombok.extern.slf4j.Slf4j;
import org.cris6h16.security.SecurityComponent;
import org.cris6h16.user.CreateUserInput;
import org.cris6h16.user.UserComponent;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.math.BigDecimal;
import java.util.Set;

@SpringBootApplication(
        scanBasePackages = "org.cris6h16"
)
@Slf4j
public class Main {
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    @Bean
    CommandLineRunner commandLineRunner(UserComponent userComponent, SecurityComponent securityComponent, TransactionTemplate transactionTemplate) {
        return args -> {
            log.info("CommandLineRunner running");
            transactionTemplate.execute(status -> {
                try {
                    userComponent.create(CreateUserInput.builder()
                            .firstname("cris6h16")
                            .lastname("test")
                            .email("cristianmherrera21@gmail.com")
                            .password(securityComponent.encodePassword("12345678"))
                            .enabled(true)
                            .emailVerified(true)
                            .authorities(Set.of("ROLE_SELLER"))
                            .balance(BigDecimal.valueOf(10.15))
                            .build());
                } catch (Exception e) {
                    log.error("Error creating user", e);
                    status.setRollbackOnly();
                }
                return null;
            });
        };
    }

}
