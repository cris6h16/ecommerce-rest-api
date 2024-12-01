package org.cris6h16.email;

import lombok.extern.slf4j.Slf4j;
import org.cris6h16.email.Exceptions.EmailComponentException;
import org.cris6h16.email.Exceptions.EmailErrorCode;
import org.springframework.stereotype.Component;

import static org.cris6h16.email.Exceptions.EmailErrorCode.ACTION_TYPE_BLANK;
import static org.cris6h16.email.Exceptions.EmailErrorCode.ACTION_TYPE_NULL;
import static org.cris6h16.email.Exceptions.EmailErrorCode.ACTION_TYPE_TOO_LONG;
import static org.cris6h16.email.Exceptions.EmailErrorCode.CODE_INVALID_LENGTH;
import static org.cris6h16.email.Exceptions.EmailErrorCode.CODE_IS_BLANK;
import static org.cris6h16.email.Exceptions.EmailErrorCode.CODE_NULL;
import static org.cris6h16.email.Exceptions.EmailErrorCode.EMAIL_NULL;
import static org.cris6h16.email.Exceptions.EmailErrorCode.EMAIL_REGEX_MISMATCH;
import static org.cris6h16.email.Exceptions.EmailErrorCode.EMAIL_TOO_LONG;
import static org.cris6h16.email.VerificationCodeEntity.ACTION_TYPE_MAX_LENGTH;
import static org.cris6h16.email.VerificationCodeEntity.EMAIL_MAX_LENGTH;

@Slf4j
@Component
public class EmailValidator {

     String validateEmail(String email) {
        if (email == null) throwE(EMAIL_NULL);
        if ((email = email.trim()).length() > EMAIL_MAX_LENGTH) throwE(EMAIL_TOO_LONG);
        if (!email.matches("^\\S+@\\S+\\.\\S+$")) throwE(EMAIL_REGEX_MISMATCH);
        return email.toLowerCase();
    }


    String validateCode(String code) {
        if (code == null) throwE(CODE_NULL);
        if ((code = code.trim()).isBlank()) throwE(CODE_IS_BLANK);
        if (code.length() != VerificationCodeEntity.CODE_LENGTH) throwE(CODE_INVALID_LENGTH);
        return code;
    }

    private void throwE(EmailErrorCode errorCode) {
        throw new EmailComponentException(errorCode);
    }

    String validateActionType(String actionType) {
        if (actionType == null) throwE(ACTION_TYPE_NULL);
        if ((actionType.trim()).isBlank()) throwE(ACTION_TYPE_BLANK);
        if (actionType.length() > ACTION_TYPE_MAX_LENGTH) throwE(ACTION_TYPE_TOO_LONG);
        return actionType;
    }
}
