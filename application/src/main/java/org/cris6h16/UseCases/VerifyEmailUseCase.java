package org.cris6h16.UseCases;

import org.cris6h16.ErrorMsgProperties;
import org.cris6h16.InvalidVerificationCodeException;
import org.cris6h16.VerificationCodeServiceImpl;

import java.time.LocalDateTime;

public class VerifyEmailUseCase {
    private final VerificationCodeServiceImpl verificationCodeService;
    private final ErrorMsgProperties errorMsgProperties;

    public VerifyEmailUseCase(VerificationCodeServiceImpl verificationCodeService, ErrorMsgProperties errorMsgProperties) {
        this.verificationCodeService = verificationCodeService;
        this.errorMsgProperties = errorMsgProperties;
    }

    public void verifyEmail(String email, String code) {
        existValidElseThrow(email, code);
    }

    private void existValidElseThrow(String email, String code) {
        boolean e = verificationCodeService.existsByEmailAndCodeAndExpiresAtBefore(email, code, LocalDateTime.now());
        if (!e) {
            throw new InvalidVerificationCodeException(errorMsgProperties.getInvalidVerificationCode());
        }
    }
}
