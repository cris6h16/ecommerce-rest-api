package org.cris6h16.payment;

import org.springframework.stereotype.Component;

import static org.cris6h16.payment.PaymentErrorCode.NEGATIVE_ORDER_ID;
import static org.cris6h16.payment.PaymentErrorCode.NEGATIVE_USER_ID;
import static org.cris6h16.payment.PaymentErrorCode.NULL_ORDER_ID;
import static org.cris6h16.payment.PaymentErrorCode.NULL_USER_ID;

@Component
public class PaymentValidator {
    void validate(AppCreditsPaymentRequestInput input) {
        validateOrderId(input.getOrderId());
        validateUserId(input.getUserId());
    }

    void validateOrderId(Long orderId) {
        if (orderId == null) throwE(NULL_ORDER_ID);
        if (orderId < 1) throwE(NEGATIVE_ORDER_ID);
    }

    void validateUserId(Long userId) {
        if (userId == null) throwE(NULL_USER_ID);
        if (userId < 1) throwE(NEGATIVE_USER_ID);
    }

    private void throwE(PaymentErrorCode errorCode) {
        throw new PaymentComponentException(errorCode);
    }
}
