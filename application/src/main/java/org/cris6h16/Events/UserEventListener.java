package org.cris6h16.Events;

import org.cris6h16.EmailService;
import org.cris6h16.VerificationCodeService;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
 class UserEventListener {

    private final EmailService emailService;
    private final VerificationCodeService codeService;

    public UserEventListener(EmailService emailService, VerificationCodeService verificationCodeService) {
        this.emailService = emailService;
        this.codeService = verificationCodeService;
    }

    @EventListener
    //todo: async
    public void handleUserCreatedEvent(UserCreatedEvent event) {
        handleEmailVerif(event.getEmail());
    }

    @EventListener
    public void handleEmailVerificationRequiredEvent(EmailVerificationRequiredEvent event) {
        handleEmailVerif(event.getEmail());
    }

    private void handleEmailVerif(String email) {
        codeService.deleteByEmail(email);
        String code = codeService.createAndSaveCode(email);
        emailService.sendEmailVerificationCode(email, code);
    }
}
