package org.cris6h16.payment;

import lombok.Getter;

@Getter
public class PaymentComponentException extends RuntimeException {
private PaymentErrorCode errorCode;

    public PaymentComponentException(PaymentErrorCode errorCode) {
        this.errorCode = errorCode;
    }
}
