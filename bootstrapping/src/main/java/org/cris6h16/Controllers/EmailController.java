package org.cris6h16.Controllers;

import org.cris6h16.email.EmailService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(EmailController.BASE_PATH)
public class EmailController {
    public static final String BASE_PATH = "/api/v1/email";

    private final EmailService emailService;

    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    @PostMapping(
            path = "send-email-verification",
            consumes = MediaType.TEXT_PLAIN_VALUE
    )
    public ResponseEntity<Void> sendEmailVerification(@RequestBody String email) {
        this.emailService.sendEmailVerificationCode(email);
        return ResponseEntity.ok().build();
    }
}
