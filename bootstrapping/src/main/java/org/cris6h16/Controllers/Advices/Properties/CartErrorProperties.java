package org.cris6h16.Controllers.Advices.Properties;

import lombok.Getter;
import lombok.Setter;
import org.cris6h16.Controllers.Advices.Properties.others.ValidatePropertiesNotNullOrEmpty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "error-messages.components.cart")
@Getter
@Setter
@ValidatePropertiesNotNullOrEmpty
public class CartErrorProperties {
    private String invalidQuantity;
    private String productAlreadyInCart;
    private String invalidDelta;
}
