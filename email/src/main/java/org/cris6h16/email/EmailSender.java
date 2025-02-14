package org.cris6h16.email;

 interface EmailSender {
    void sendEmailVerificationCode(String email, String code);
}
