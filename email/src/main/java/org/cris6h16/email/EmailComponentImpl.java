package org.cris6h16.email;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Slf4j
public class EmailComponentImpl implements EmailComponent {
    private final EmailSender emailSender;
    private final EmailValidator validator;
    private final VerificationCodeRepository repository;
    private final VerificationCodeGenerator verificationCodeGenerator;

    EmailComponentImpl(EmailSender emailSender,
                       EmailValidator validator,
                       VerificationCodeRepository verificationCodeRepository,
                       VerificationCodeGenerator verificationCodeGenerator) {
        this.emailSender = emailSender;
        this.validator = validator;
        this.repository = verificationCodeRepository;
        this.verificationCodeGenerator = verificationCodeGenerator;
    }


    private String IfNullEmptyElseTrimAndLowerCase(String str) {
        return str == null ? "" : str.trim().toLowerCase();
    }


    @Override
    public void removeOldCodesByEmail(String email) {
        email = IfNullEmptyElseTrimAndLowerCase(email);
        validator.validateEmail(email);

        repository.deleteByEmail(email);
    }

    @Override
    public String sendEmailVerificationCode(String email) {
        email = IfNullEmptyElseTrimAndLowerCase(email);
        validator.validateEmail(email);

        String code = verificationCodeGenerator.genCode();
        repository.save(new VerificationCodeEntity(email, code));
        emailSender.sendEmailVerificationCode(email, code);
        return code;
    }

    @Override
    public boolean isCodeValid(String email, String code) {
        email = IfNullEmptyElseTrimAndLowerCase(email);
        code = IfNullEmptyElseTrimAndLowerCase(code);
        validator.validateEmail(email);
        validator.validateCode(code);

        return repository.existsByEmailAndCodeAndExpiresAtAfter(email, code, LocalDateTime.now());
    }

    @Override
    public void deleteAll() {
        repository.deleteAll();
    }

}
