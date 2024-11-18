package org.cris6h16.email;

import lombok.extern.slf4j.Slf4j;
import org.cris6h16.email.Exceptions.EmailComponentInvalidAttributeException;
import org.cris6h16.email.Exceptions.EmailErrorCode;
import org.springframework.stereotype.Component;

import static org.cris6h16.email.Exceptions.EmailErrorCode.CODE_INVALID_LENGTH;
import static org.cris6h16.email.Exceptions.EmailErrorCode.CODE_NULL;
import static org.cris6h16.email.Exceptions.EmailErrorCode.EMAIL_NULL;
import static org.cris6h16.email.Exceptions.EmailErrorCode.EMAIL_REGEX_MISMATCH;
import static org.cris6h16.email.Exceptions.EmailErrorCode.EMAIL_TOO_LONG;
import static org.cris6h16.email.VerificationCodeEntity.EMAIL_MAX_LENGTH;

@Slf4j
@Component
public class EmailValidator {

    public void validateEmail(String email) {
        if (email == null) throwE(EMAIL_NULL);
        if (email.length() > EMAIL_MAX_LENGTH) throwE(EMAIL_TOO_LONG);
        if (!email.matches("^\\S+@\\S+\\.\\S+$")) throwE(EMAIL_REGEX_MISMATCH);
    }


    void validateCode(String code) {
        if (code == null) throwE(CODE_NULL);
        if (code.length() != VerificationCodeEntity.CODE_LENGTH) throwE(CODE_INVALID_LENGTH);
    }

    private void throwE(EmailErrorCode errorCode) {
        throw new EmailComponentInvalidAttributeException(errorCode);
    }
}
