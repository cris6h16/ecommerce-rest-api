package org.cris6h16.email;

import lombok.extern.slf4j.Slf4j;
import org.cris6h16.email.Exceptions.EmailComponentException;
import org.cris6h16.email.Exceptions.EmailErrorCode;
import org.springframework.stereotype.Component;

import static org.cris6h16.email.Exceptions.EmailErrorCode.ACTION_TYPE_BLANK;
import static org.cris6h16.email.Exceptions.EmailErrorCode.ACTION_TYPE_NULL;
import static org.cris6h16.email.Exceptions.EmailErrorCode.ACTION_TYPE_TOO_LONG;
import static org.cris6h16.email.Exceptions.EmailErrorCode.CODE_NULL;
import static org.cris6h16.email.Exceptions.EmailErrorCode.CODE_REGEX_MISMATCH;
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
        code = code == null ? "" : code.trim().toUpperCase();
        if (!code.matches("^[A-Z0-9]{6}$")) throwE(CODE_REGEX_MISMATCH);
        return code;
    }

    private void throwE(EmailErrorCode errorCode) {
        throw new EmailComponentException(errorCode);
    }

    String validateActionType(String actionType) {
        actionType = actionType == null ? "" : actionType.trim().toUpperCase();
        if (actionType.length() > ACTION_TYPE_MAX_LENGTH) throwE(ACTION_TYPE_TOO_LONG);
        return actionType;
    }
}
