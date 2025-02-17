package org.cris6h16.Controllers;

import org.cris6h16.facades.PaymentFacade;
import org.cris6h16.facades.PaymentRequestDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

import static org.cris6h16.Controllers.PaymentController.PAYMENT_PATH;

@RestController
@RequestMapping(PAYMENT_PATH)
public class PaymentController {

    private final PaymentFacade paymentFacade;
    public static final String PAYMENT_PATH = "/api/v1/payments";

    public PaymentController(PaymentFacade paymentFacade) {
        this.paymentFacade = paymentFacade;
    }

    @PostMapping(consumes = "application/json")
    public ResponseEntity<Void> processPayment(@RequestBody PaymentRequestDTO dto, UriComponentsBuilder ucb) {
        URI uri = ucb
                .path(PAYMENT_PATH + "/{id}")
                .buildAndExpand(paymentFacade.processPayment(dto))
                .toUri();
        return ResponseEntity.created(uri).build();
    }

}
