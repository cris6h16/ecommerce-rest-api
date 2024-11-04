package org.cris6h16.email;

public interface EmailService {
    void sendEmailVerificationCode(String email);
    void checkCode(String email, String code);
}