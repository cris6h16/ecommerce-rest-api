package org.cris6h16.Controllers.Advices.Properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@ConfigurationProperties(prefix = "error-messages.components.file")
@Component
public class FileComponentErrorMsgProperty {
    private String fileUploadFail;
}