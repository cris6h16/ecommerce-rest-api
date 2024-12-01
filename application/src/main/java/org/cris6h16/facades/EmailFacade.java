package org.cris6h16.facades;

import org.cris6h16.SendEmailVerificationDTO;

public interface EmailFacade {
    void sendEmailVerificationCodeIfExists(SendEmailVerificationDTO dto);
}
