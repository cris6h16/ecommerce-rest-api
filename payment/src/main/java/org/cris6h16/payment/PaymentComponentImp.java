package org.cris6h16.payment;

public class PaymentComponentImp implements PaymentComponent {

    private final PaymentValidator paymentValidator;

    public PaymentComponentImp(PaymentValidator paymentValidator) {
        this.paymentValidator = paymentValidator;
    }

    @Override
    public Long processAppCreditsPayment(AppCreditsPaymentRequestInput input) {
        paymentValidator.validate(input);

        return 0;
    }
}
