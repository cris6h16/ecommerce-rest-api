package org.cris6h16.facades;

public interface EmailFacade {
    void sendEmailVerificationCodeIfExists(SendEmailVerificationDTO dto);
}
