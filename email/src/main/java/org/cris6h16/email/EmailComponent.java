package org.cris6h16.email;

public interface EmailComponent {
    void remOldCodesAndCreateOneAndSendInEmailVerification(String email);
    void checkCodeAfterRemAllMyCodes(String email, String code);

    void removeOldCodesByEmail(String email);

    void sendEmailVerificationCode(String email);

    boolean isCodeValid(String email, String code);
}