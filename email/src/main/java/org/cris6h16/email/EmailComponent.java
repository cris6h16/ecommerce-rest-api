package org.cris6h16.email;

public interface EmailComponent {
    String sendEmailVerificationCode(String email, String actionType);
    boolean isCodeValid(String email, String code, String actionType);

    void deleteAll();

    void removeByEmailAndActionType(String email, String actionType);
}