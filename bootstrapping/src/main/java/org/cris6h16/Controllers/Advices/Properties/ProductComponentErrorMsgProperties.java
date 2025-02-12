package org.cris6h16.Controllers.Advices.Properties;

import lombok.Getter;
import lombok.Setter;
import org.cris6h16.Controllers.Advices.Properties.others.ValidatePropertiesNotNullOrEmpty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "error-messages.components.product")
@Getter
@Setter
@ValidatePropertiesNotNullOrEmpty
public class ProductComponentErrorMsgProperties {
    private String productNameLengthMismatch;
    private String weightPoundsNegative;
    private String heightCmNegative;
    private String widthCmNegative;
    private String lengthCmNegative;
    private String productDescriptionLengthMismatch;
    private String invalidPrice;
    private String invalidStock;
    private String invalidCategoryId;
    private String userNotFoundById;
    private String categoryNotFoundById;
    private String imageUrlTooLong;
    private String stockNegative;
    private String uniqueUserIdProductName;
    private String productNotFoundById;
    private String unsupportedFilterAttribute;
    private String priceFilterErrorParsingStrToBigDecimal;
}
