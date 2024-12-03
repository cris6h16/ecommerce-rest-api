package org.cris6h16.facades;

import org.cris6h16.facades.Exceptions.AppCreditsPaymentRequestDTO;
import org.cris6h16.payment.AppCreditsPaymentRequestInput;
import org.cris6h16.payment.PaymentComponent;
import org.springframework.stereotype.Component;

@Component
public class PaymentFacadeImpl implements PaymentFacade {
    private final PaymentComponent paymentComponent;

    public PaymentFacadeImpl(PaymentComponent paymentComponent) {
        this.paymentComponent = paymentComponent;
    }

    @Override
    public Long processPayment(PaymentRequestDTO dto) {
        if (dto instanceof AppCreditsPaymentRequestDTO appDTO) {
            return paymentComponent.processAppCreditsPayment(toInput(appDTO));

        } else {
            throw new UnsupportedOperationException("Payment method not supported");
        }
    }

    private AppCreditsPaymentRequestInput toInput(AppCreditsPaymentRequestDTO dto) {
        return AppCreditsPaymentRequestInput.builder()
                .userId(dto.getUserId())
                .orderId(dto.getOrderId())
                .build();
    }
}
