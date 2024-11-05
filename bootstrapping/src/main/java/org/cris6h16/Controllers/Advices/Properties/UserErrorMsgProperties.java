package org.cris6h16.Controllers.Advices.Properties;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.function.BiConsumer;

@Component
@ConfigurationProperties(prefix = "user.error-messages")
@Getter
@Setter
public class UserErrorMsgProperties {
    private String firstNameLength;
    private String lastNameLength;
    private String passwordLength;
    private String emailInvalid;
    private String emailAlreadyExists;
    private String userNotFound;
    private String invalidCredentials;
    private String emailNotVerified;
    private String verificationCodeLength;


    @PostConstruct
    public void initNotNull() {
        BiConsumer<String, Object> ex = (propertyName, propertyVal) -> {
            if (propertyVal == null) {
                throw new IllegalStateException("Property " + propertyName + " cannot be null");
            }
        };

        ex.accept("firstNameLength", firstNameLength);
        ex.accept("lastNameLength", lastNameLength);
        ex.accept("passwordLength", passwordLength);
        ex.accept("emailInvalid", emailInvalid);
        ex.accept("emailAlreadyExists", emailAlreadyExists);
        ex.accept("userNotFound", userNotFound);
        ex.accept("invalidCredentials", invalidCredentials);
        ex.accept("emailNotVerified", emailNotVerified);
        ex.accept("verificationCodeLength", verificationCodeLength);
    }
}
