package org.cris6h16.facades;

import lombok.Getter;

@Getter
public abstract class PaymentRequestDTO {
    private Long orderId;
    private Long userId;
}
