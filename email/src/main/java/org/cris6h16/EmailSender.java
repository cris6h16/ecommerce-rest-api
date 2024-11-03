package org.cris6h16;

public interface EmailSender {
    void sendEmailVerificationCode(String email, String code);
}
