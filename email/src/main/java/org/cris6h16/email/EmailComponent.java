package org.cris6h16.email;

public interface EmailComponent {
    void removeOldCodesByEmail(String email);
    String sendEmailVerificationCode(String email);
    boolean isCodeValid(String email, String code);

    void deleteAll();
}