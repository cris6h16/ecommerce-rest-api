package org.cris6h16.Controllers.Advices.Properties;

import lombok.Getter;
import lombok.Setter;
import org.cris6h16.Controllers.Advices.Properties.others.ValidatePropertiesNotNullOrEmpty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "email.error-messages")
@Getter
@Setter
@ValidatePropertiesNotNullOrEmpty
public class EmailErrorMsgProperties {
    private String emailInvalid;
    private String invalidCodeLength;
    private String validVerificationCodeNotFound;
    private String emailSending;
}
