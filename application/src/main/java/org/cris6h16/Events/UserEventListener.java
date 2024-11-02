package org.cris6h16.Events;

import org.cris6h16.EmailService;
import org.cris6h16.UserCreatedEvent;
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
        codeService.deleteByEmail(event.getEmail());
        String code = codeService.createAndSaveCode(event.getEmail());
        emailService.sendEmailVerificationCode(event.getEmail(), code);
    }
}
