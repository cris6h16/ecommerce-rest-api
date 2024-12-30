package org.cris6h16.Controllers.Advices.Properties;

import lombok.Getter;
import lombok.Setter;
import org.cris6h16.Controllers.Advices.Properties.others.ValidatePropertiesNotNullOrEmpty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "error-messages.components.email")
@Getter
@Setter
@ValidatePropertiesNotNullOrEmpty
public class EmailComponentErrorMsgProperties {
    private String invalidCodeLength;
    private String emailInvalid;
    private String emailSending;
    private String invalidActionTypeLength;
    private String codeRegexMismatch;
}
