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
    private String userIdNull;
    private String userIdLessThanOne;
    private String userNotFoundById;
    private String categoryIdNull;
    private String categoryIdLessThanOne;
    private String categoryNameTooLong;
    private String categoryNameNull;
    private String categoryNotFoundById;
    private String imageUrlNull;
    private String imageUrlTooLong;
    private String approxWeightLbNull;
    private String stockNegative;
    private String approxHeightCmNull;
    private String approxHeightCmNegative;
    private String approxWidthCmNull;
    private String approxWidthCmNegative;
    private String approxWeightLbNegative;
    private String descriptionNull;
    private String descriptionTooLong;
    private String stockNull;
    private String priceNull;
    private String priceNegative;
    private String productNameNull;
    private String productNameTooLong;
    private String productIdNull;
    private String productIdLessThanOne;
    private String uniqueUserIdProductName;
}
