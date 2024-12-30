package org.cris6h16.Controllers.Advices.Properties;

import lombok.Getter;
import lombok.Setter;
import org.cris6h16.Controllers.Advices.Properties.others.ValidatePropertiesNotNullOrEmpty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "error-messages.components.user")
@Getter
@Setter
@ValidatePropertiesNotNullOrEmpty
public class UserComponentErrorMsgProperties {
    private String emailAlreadyExists;
    private String emailNull;
    private String userIdInvalid;
    private String lastnameNull;
    private String lastnameTooLong;
    private String emailTooLong;
    private String emailInvalid;
    private String passwordNull;
    private String passwordLessThan8;
    private String passwordTooLong;
    private String firstnameTooLong;
    private String firstnameNull;
    private String firstnameIsBlank;
    private String lastnameIsBlank;
}
