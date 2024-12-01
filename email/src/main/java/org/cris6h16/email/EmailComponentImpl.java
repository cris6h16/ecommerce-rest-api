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


    @Override
    public String sendEmailVerificationCode(String email, String actionType) {
        email = validator.validateEmail(email);
        actionType = validator.validateActionType(actionType);

        String code = verificationCodeGenerator.genCode();
        repository.save(new VerificationCodeEntity(email, code, actionType));
        emailSender.sendEmailVerificationCode(email, code);
        return code;
    }

    @Override
    public boolean isCodeValid(String email, String code, String actionType) {
        email = validator.validateEmail(email);
        code = validator.validateCode(code);

        return repository.existsByEmailAndCodeAndExpiresAtAfter(email, code, LocalDateTime.now());
    }

    @Override
    public void deleteAll() {
        repository.deleteAll();
    }

    @Override
    public void removeByEmailAndActionType(String email, String actionType) {
        email = validator.validateEmail(email);
        actionType = validator.validateActionType(actionType);

        repository.deleteByEmailAndActionType(email, actionType);
    }

}
