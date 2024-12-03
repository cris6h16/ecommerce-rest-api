package org.cris6h16.payment;

import org.cris6h16.user.UserRepository;
import org.springframework.stereotype.Component;

@Component
public class PaymentComponentImp implements PaymentComponent {

    private final PaymentValidator paymentValidator;
    private final UserRepository userRepository;

    public PaymentComponentImp(PaymentValidator paymentValidator, UserRepository userRepository) {
        this.paymentValidator = paymentValidator;
        this.userRepository = userRepository;
    }

    @Override
    public Long processAppCreditsPayment(AppCreditsPaymentRequestInput input) {
        paymentValidator.validate(input);
        // todo: implement thid
        return 0L;
    }
}
