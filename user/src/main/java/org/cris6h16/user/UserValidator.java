package org.cris6h16.user;

import lombok.extern.slf4j.Slf4j;
import org.cris6h16.user.Exceptions.UserComponentException;
import org.cris6h16.user.Exceptions.UserErrorCode;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

import static org.cris6h16.user.Exceptions.UserErrorCode.BALANCE_NEGATIVE;
import static org.cris6h16.user.Exceptions.UserErrorCode.BALANCE_NULL;
import static org.cris6h16.user.Exceptions.UserErrorCode.EMAIL_NULL;
import static org.cris6h16.user.Exceptions.UserErrorCode.EMAIL_REGEX_MISMATCH;
import static org.cris6h16.user.Exceptions.UserErrorCode.EMAIL_TOO_LONG;
import static org.cris6h16.user.Exceptions.UserErrorCode.FIRSTNAME_LENGTH_MISMATCH;
import static org.cris6h16.user.Exceptions.UserErrorCode.LASTNAME_LENGTH_MISMATCH;
import static org.cris6h16.user.Exceptions.UserErrorCode.PASSWORD_LENGTH_MISMATCH;
import static org.cris6h16.user.Exceptions.UserErrorCode.PASSWORD_LESS_THAN_8;
import static org.cris6h16.user.Exceptions.UserErrorCode.USER_ID_LESS_THAN_1;
import static org.cris6h16.user.Exceptions.UserErrorCode.USER_ID_NULL;
import static org.cris6h16.user.UserEntity.EMAIL_MAX_LENGTH;
import static org.cris6h16.user.UserEntity.FIRSTNAME_MAX_LENGTH;
import static org.cris6h16.user.UserEntity.LASTNAME_MAX_LENGTH;
import static org.cris6h16.user.UserEntity.PASSWORD_MAX_LENGTH;

@Slf4j
@Component
class UserValidator {
    private void throwE(UserErrorCode errorCode) {
        throw new UserComponentException(errorCode);
    }


    public String validateFirstname(String firstname) {
        return validateLength(firstname, 1, FIRSTNAME_MAX_LENGTH, FIRSTNAME_LENGTH_MISMATCH);
    }
    public String validateLastname(String lastname) {
        return validateLength(lastname,1,  LASTNAME_MAX_LENGTH, LASTNAME_LENGTH_MISMATCH);
    }

    private String validateLength(String name,int minLength, int maxLength, UserErrorCode lengthError) {
        name = trim(name);
        if (name.length() < minLength || name.length() > maxLength) throwE(lengthError);
        return name;
    }

    private String trim(String s) {
        return s == null ? "" : s.trim();
    }





    public String validatePassword(String password) {
        return  validateLength(password,8, PASSWORD_MAX_LENGTH,  PASSWORD_LENGTH_MISMATCH);
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
        if (id == null) throwE(USER_ID_NULL);
        if (id < 1) throwE(USER_ID_LESS_THAN_1);
    }

    void validateBalance(BigDecimal balance) {
        if (balance == null) throwE(BALANCE_NULL);
        if (balance.compareTo(BigDecimal.ZERO) < 0) throwE(BALANCE_NEGATIVE);
    }
}
