package org.cris6h16.email;

public interface EmailSender {
    void sendEmailVerificationCode(String email, String code);
}
