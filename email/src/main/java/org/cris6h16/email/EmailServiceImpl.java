package org.cris6h16.email;

import lombok.extern.slf4j.Slf4j;
import org.cris6h16.email.Exceptions.ValidVerificationCodeNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Slf4j
public class EmailServiceImpl implements EmailService {
    private final VerificationCodeService verificationCodeService;
    private final EmailSender emailSender;
    private final EmailValidator validator;

    public EmailServiceImpl(VerificationCodeService verificationCodeService, EmailSender emailSender, EmailValidator validator) {
        this.verificationCodeService = verificationCodeService;
        this.emailSender = emailSender;
        this.validator = validator;
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.MANDATORY)
    public void remOldCodesAndCreateOneAndSendInEmailVerification(String email) {
        validator.validateEmail(email);

        removeAllCodesByEmail(email);
        String code = createAndSaveCode(email);
        emailSender.sendEmailVerificationCode(email, code);
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.MANDATORY)
    public void checkCodeAfterRemAllMyCodes(String email, String code) {
        email = trimIfNullEmpty(email);
        code = trimIfNullEmpty(code).toLowerCase();

        validator.validateEmail(email);
        validator.validateCode(code);

        exists(email, code);
        removeAllCodesByEmail(email);
    }


    private String trimIfNullEmpty(String str) {
        return str == null ? "" : str.trim();
    }

    private void exists(String email, String code) {
        boolean isValid = verificationCodeService.existsByEmailAndCodeAndExpiresAtAfter(email, code, LocalDateTime.now());
        log.debug("Code is valid: {}", isValid);
        if (!isValid) {
            throw new ValidVerificationCodeNotFoundException();
        }
    }

    private String createAndSaveCode(String email) {
        String savedCode = verificationCodeService.createAndSaveCode(email);
        log.debug("Code saved for email: {}", email);
        return savedCode;
    }

    private void removeAllCodesByEmail(String email) {
        verificationCodeService.deleteByEmail(email);
        log.debug("Old codes removed for email: {}", email);
    }
}
