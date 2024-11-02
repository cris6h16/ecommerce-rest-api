package org.cris6h16;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
 class UserEventListener {

    private final EmailService emailService;

    @Autowired
    public UserEventListener(EmailService emailService) {
        this.emailService = emailService;
    }

    @EventListener
    public void handleUserCreatedEvent(UserCreatedEvent event) {
        String verificationLink = generateVerificationLink(event.getEmail());
        emailService.sendVerificationEmail(event.getEmail(), verificationLink);
    }
}
