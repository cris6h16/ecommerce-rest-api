package org.cris6h16.Controllers.Advices.Properties;

import lombok.Getter;
import lombok.Setter;
import org.cris6h16.Controllers.Advices.Properties.others.ValidatePropertiesNotNullOrEmpty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "error-messages.components.application")
@Getter
@Setter
@ValidatePropertiesNotNullOrEmpty
public class ApplicationComponentErrorMsgProperties {
    private String unsupportedActionType;
    private String pageSizeTooBig;
    private String forbiddenSortProperty;

    private String validVerificationCodeNotFound;
    private String emailNotVerified;
    private String invalidCredentials;
    private String imgMultipartFileIsEmpty;
    private String enabledUserNotFound;
    private String productNotFoundById;
    private String invalidActionType;
    private String insufficientStock;
    private String cartItemNotFound;
    private String addressNotFound;
}
