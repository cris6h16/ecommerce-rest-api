package org.cris6h16.Controllers.Advices.Properties;

import lombok.Getter;
import lombok.Setter;
import org.cris6h16.Controllers.Advices.Properties.others.ValidatePropertiesNotNullOrEmpty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "user.error-messages")
@Getter
@Setter
@ValidatePropertiesNotNullOrEmpty
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
}
