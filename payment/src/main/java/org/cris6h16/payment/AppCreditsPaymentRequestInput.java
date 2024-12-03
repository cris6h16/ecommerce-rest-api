package org.cris6h16.payment;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class AppCreditsPaymentRequestInput {
    private Long userId;
    private Long orderId;
}
