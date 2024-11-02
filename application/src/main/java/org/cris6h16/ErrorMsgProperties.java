package org.cris6h16;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "error-messages")
@Getter
@Setter
class ErrorMsgProperties {
    private String invalidCredentials;
    private String emailNotVerified;
}
