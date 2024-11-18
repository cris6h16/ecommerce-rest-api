package org.cris6h16.email;

import lombok.extern.slf4j.Slf4j;
import org.cris6h16.email.Exceptions.InvalidAttributeException.EmailInvalidCodeLengthException;
import org.cris6h16.email.Exceptions.InvalidAttributeException.EmailInvalidEmailException;
import org.springframework.stereotype.Component;

import static org.cris6h16.email.VerificationCodeEntity.EMAIL_LENGTH;

@Slf4j
@Component
public class EmailValidator {

    public void validateEmail(String email) {
        email = email == null ? "" : email.trim();
        if (email.isEmpty() || email.length() > EMAIL_LENGTH || !email.matches("^\\S+@\\S+\\.\\S+$")) {
            log.debug("Invalid email: {}", email);
            throw new EmailInvalidEmailException();
        }
        log.debug("Valid email: {}", email);
    }

    void validateCode(String code) {
        code = code == null ? "" : code.trim();
        if (code.length() != VerificationCodeEntity.CODE_LENGTH) {
            log.debug("Invalid code length: {}", code);
            throw new EmailInvalidCodeLengthException();
        }
        log.debug("Valid code: {}", code);
    }
}
