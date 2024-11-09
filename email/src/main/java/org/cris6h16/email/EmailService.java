package org.cris6h16.email;

public interface EmailService {
    void remOldCodesAndCreateOneAndSendInEmailVerification(String email);
    void checkCodeAfterRemAllMyCodes(String email, String code);
}