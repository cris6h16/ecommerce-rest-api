package org.cris6h16.payment;

import org.springframework.stereotype.Component;

@Component
public class PaymentComponentImp implements PaymentComponent {

    private final PaymentValidator paymentValidator;

    public PaymentComponentImp(PaymentValidator paymentValidator) {
        this.paymentValidator = paymentValidator;
    }

    @Override
    public Long processAppCreditsPayment(AppCreditsPaymentRequestInput input) {
        paymentValidator.validate(input);
        // todo: implement thid
        return 0L;
    }
}
