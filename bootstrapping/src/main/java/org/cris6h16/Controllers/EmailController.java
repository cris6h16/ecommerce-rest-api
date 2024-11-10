package org.cris6h16.Controllers;

import org.cris6h16.facades.EmailFacade;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Transactional(
        rollbackFor = Exception.class,
        isolation = Isolation.READ_COMMITTED
)
@RestController
@RequestMapping(EmailController.BASE_PATH)
public class EmailController {
    public static final String BASE_PATH = "/api/v1/email";

    private final EmailFacade emailFacade;

    public EmailController(EmailFacade emailFacade) {
        this.emailFacade = emailFacade;
    }

    @PostMapping(
            path = "send-email-verification",
            consumes = MediaType.TEXT_PLAIN_VALUE
    )
    public ResponseEntity<Void> sendEmailVerification(@RequestBody String email) {
        this.emailFacade.sendEmailVerificationCodeIfExists(email);
        return ResponseEntity.ok().build();
    }
}
