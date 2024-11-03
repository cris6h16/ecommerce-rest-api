package org.cris6h16;

public class EmailServiceImpl implements EmailService {
    private final VerificationCodeService verificationCodeService;
    private final EmailSender emailSender;

    public EmailServiceImpl(VerificationCodeService verificationCodeService, EmailSender emailSender) {
        this.verificationCodeService = verificationCodeService;
        this.emailSender = emailSender;
    }

    @Override
    public void sendEmailVerificationCode(String email) {
        removeOldCodes(email);
        String code = createAndSaveCode(email);
        emailSender.sendEmailVerificationCode(email, code);
    }

    private String createAndSaveCode(String email) {
        return verificationCodeService.createAndSaveCode(email);
    }

    private void removeOldCodes(String email) {
        verificationCodeService.deleteByEmail(email);
    }
}
