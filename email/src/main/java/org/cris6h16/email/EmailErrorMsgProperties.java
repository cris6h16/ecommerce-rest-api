package org.cris6h16.email;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.function.BiConsumer;

@Component
@ConfigurationProperties(prefix = "email.error-messages")
@Getter
@Setter
public class EmailErrorMsgProperties {
    private String emailInvalid;
    private String invalidCodeLength;
    private String validVerificationCodeNotFound;
    private String unexpectedError;


    @PostConstruct
    public void initNotNull() {
        BiConsumer<String, Object> ex = (propertyName, propertyVal) -> {
            if (propertyVal == null) {
                throw new IllegalStateException("Property " + propertyName + " cannot be null");
            }
        };

        ex.accept("emailInvalid", emailInvalid);
        ex.accept("invalidCodeLength", invalidCodeLength);
        ex.accept("validVerificationCodeNotFound", validVerificationCodeNotFound);
    }
}
