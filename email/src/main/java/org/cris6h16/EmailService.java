package org.cris6h16;

public interface EmailService {
    void sendEmailVerificationCode(String email, String verificationCode);
}