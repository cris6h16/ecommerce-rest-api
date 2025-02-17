package org.cris6h16.Controllers;

import org.cris6h16.facades.EmailFacade;
import org.cris6h16.facades.SendEmailVerificationDTO;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/emails")
public class EmailController {
    private final EmailFacade emailFacade;

    public EmailController(EmailFacade emailFacade) {
        this.emailFacade = emailFacade;
    }

    @PostMapping(
            path = "/send-email-verification",
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Void> sendEmailVerification(@RequestBody SendEmailVerificationDTO dto) {
        this.emailFacade.sendEmailVerificationCodeIfExists(dto);
        return ResponseEntity.noContent().build();
    }
}
