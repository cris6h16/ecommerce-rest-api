package org.cris6h16.Events;

import org.cris6h16.EmailService;
import org.cris6h16.UserCreatedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
 class UserEventListener {

    private final EmailService emailService;

    public UserEventListener(EmailService emailService) {
        this.emailService = emailService;
    }

    @EventListener
    //todo: async
    public void handleUserCreatedEvent(UserCreatedEvent event) {
        emailService.sendEmailVerificationCode(event.getEmail());
    }
}
