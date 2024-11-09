package org.cris6h16.Controllers.Advices.Properties;

import lombok.Getter;
import lombok.Setter;
import org.cris6h16.Controllers.Advices.Properties.others.ValidatePropertiesNotNullOrEmpty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "system.error-messages")
@Getter
@Setter
@ValidatePropertiesNotNullOrEmpty
public class SystemErrorProperties {
    private String unexpectedError;
}
