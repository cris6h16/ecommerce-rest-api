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
private String userAlreadyHasAProductWithTheSpecifiedName;
private String invalidApproxHeightCm;
private String invalidApproxWeightLb;
private String invalidApproxWidthCm;
private String invalidCategoryId;
private String invalidCategoryNameLength;
private String invalidDescriptionLength;
private String invalidImageUrlLength;
private String invalidPrice;
private String invalidProductId;
private String invalidProductNameLength;
private String invalidStock;
private String invalidUserId;
private String categoryNotFound;
private String userNotFound;
}
