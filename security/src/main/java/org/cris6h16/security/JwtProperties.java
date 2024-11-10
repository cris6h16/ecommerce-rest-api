package org.cris6h16.security;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.function.BiConsumer;

@Configuration
@ConfigurationProperties(prefix = "jwt")
@Getter
@Setter
public class JwtProperties {
    private String secretKey;
    private Long accessTokenExpMinutes;
    private Long refreshTokenExpMinutes;

    @PostConstruct
    public void initNotNull() {
        BiConsumer<String, Object> ex = (propertyName, propertyVal) -> {
            if (propertyVal == null) {
                throw new IllegalStateException("Property " + propertyName + " cannot be null");
            }
        };

        ex.accept("secretKey", secretKey);
        ex.accept("accessTokenExpMinutes", accessTokenExpMinutes);
        ex.accept("refreshTokenExpMinutes", refreshTokenExpMinutes);
    }
}