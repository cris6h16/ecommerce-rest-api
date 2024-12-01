package org.cris6h16.user;

import lombok.extern.slf4j.Slf4j;
import org.cris6h16.user.Exceptions.UserComponentException;
import org.cris6h16.user.Exceptions.UserErrorCode;
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
        throw new UserComponentException(errorCode);
    }


    public String validateFirstname(String firstname) {
        if (firstname == null) throwE(FIRSTNAME_NULL);
        if ((firstname.trim()).length() > FIRSTNAME_MAX_LENGTH) throwE(FIRSTNAME_TOO_LONG);
        return firstname;
    }


    public String validateLastname(String lastname) {
        if (lastname == null) throwE(LASTNAME_NULL);
        if ((lastname.trim()).length() > LASTNAME_MAX_LENGTH) throwE(LASTNAME_TOO_LONG);
        return lastname;
    }


    public String validatePassword(String password) {
        if (password == null) throwE(PASSWORD_NULL);
        if ((password.trim()).length() > PASSWORD_LENGTH) throwE(PASSWORD_TOO_LONG);
        if (password.length() < 8) throwE(PASSWORD_LESS_THAN_8);
        return password;
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
    public String validateEmail(String email) {
        if (email == null) throwE(EMAIL_NULL);
        if ((email.trim()).length() > EMAIL_MAX_LENGTH) throwE(EMAIL_TOO_LONG);
        if (!email.matches("^\\S+@\\S+\\.\\S+$")) throwE(EMAIL_REGEX_MISMATCH);
        return email.toLowerCase();
    }

    void validateUserId(Long id) {
        if (id==null) throwE(USER_ID_NULL);
        if (id < 1) throwE(USER_ID_LESS_THAN_1);
    }
}
