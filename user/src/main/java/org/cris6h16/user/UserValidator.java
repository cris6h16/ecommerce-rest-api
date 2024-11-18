package org.cris6h16.user;

import lombok.extern.slf4j.Slf4j;
import org.cris6h16.user.Exceptions.UserErrorCode;
import org.cris6h16.user.Exceptions.UserComponentInvalidAttributeException;
import org.springframework.stereotype.Component;

import static org.cris6h16.user.Exceptions.UserErrorCode.EMAIL_NULL;
import static org.cris6h16.user.Exceptions.UserErrorCode.EMAIL_REGEX_MISMATCH;
import static org.cris6h16.user.Exceptions.UserErrorCode.EMAIL_TOO_LONG;
import static org.cris6h16.user.Exceptions.UserErrorCode.FIRSTNAME_NULL;
import static org.cris6h16.user.Exceptions.UserErrorCode.FIRSTNAME_TOO_LONG;
import static org.cris6h16.user.Exceptions.UserErrorCode.LASTNAME_NULL;
import static org.cris6h16.user.Exceptions.UserErrorCode.LASTNAME_TOO_LONG;
import static org.cris6h16.user.Exceptions.UserErrorCode.PASSWORD_LESS_THAN_8;
import static org.cris6h16.user.Exceptions.UserErrorCode.PASSWORD_NULL;
import static org.cris6h16.user.Exceptions.UserErrorCode.PASSWORD_TOO_LONG;
import static org.cris6h16.user.Exceptions.UserErrorCode.USER_ID_LESS_THAN_1;
import static org.cris6h16.user.Exceptions.UserErrorCode.USER_ID_NULL;
import static org.cris6h16.user.UserEntity.EMAIL_MAX_LENGTH;
import static org.cris6h16.user.UserEntity.FIRSTNAME_MAX_LENGTH;
import static org.cris6h16.user.UserEntity.LASTNAME_MAX_LENGTH;
import static org.cris6h16.user.UserEntity.PASSWORD_LENGTH;

@Slf4j
@Component
 public class UserValidator {
    private void throwE(UserErrorCode errorCode) {
        throw new UserComponentInvalidAttributeException(errorCode);
    }


    public void validateFirstname(String firstname) {
        if (firstname == null) throwE(FIRSTNAME_NULL);
        if (firstname.length() > FIRSTNAME_MAX_LENGTH) throwE(FIRSTNAME_TOO_LONG);
    }


    public void validateLastname(String lastname) {
        if (lastname == null) throwE(LASTNAME_NULL);
        if (lastname.length() > LASTNAME_MAX_LENGTH) throwE(LASTNAME_TOO_LONG);
    }


    public void validatePassword(String password) {
        if (password == null) throwE(PASSWORD_NULL);
        if (password.length() > PASSWORD_LENGTH) throwE(PASSWORD_TOO_LONG);
        if (password.length() < 8) throwE(PASSWORD_LESS_THAN_8);
    }

    /*
    ^ = starting
    \S = any non-whitespace character
    + = one or more
    @ = @
    \S = any non-whitespace character
    + = one or more
    \. = .
    \S = any non-whitespace character
    + = one or more
    $ = end of the string
     */
    public void validateEmail(String email) {
        if (email == null) throwE(EMAIL_NULL);
        if (email.length() > EMAIL_MAX_LENGTH) throwE(EMAIL_TOO_LONG);
        if (!email.matches("^\\S+@\\S+\\.\\S+$")) throwE(EMAIL_REGEX_MISMATCH);
    }

    void validateUserId(Long id) {
        if (id==null) throwE(USER_ID_NULL);
        if (id < 1) throwE(USER_ID_LESS_THAN_1);
    }
}
