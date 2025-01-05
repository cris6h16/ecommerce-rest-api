package org.cris6h16.user.Exceptions;

public enum UserErrorCode {
    FIRSTNAME_TOO_LONG,
    LASTNAME_NULL,
    LASTNAME_TOO_LONG,
    FIRSTNAME_NULL,
    PASSWORD_NULL,
    PASSWORD_TOO_LONG,
    PASSWORD_LESS_THAN_8,
    EMAIL_NULL,
    EMAIL_TOO_LONG,
    EMAIL_REGEX_MISMATCH,
    USER_ID_NULL,
    USER_ID_LESS_THAN_1,
    USER_NOT_FOUND_BY_ID,
    BALANCE_NULL,
    BALANCE_NEGATIVE,
    EMAIL_ALREADY_EXISTS,
    FIRSTNAME_IS_BLANK,
    LASTNAME_IS_BLANK,
    FIRSTNAME_LENGTH_MISMATCH,
    LASTNAME_LENGTH_MISMATCH,
    PASSWORD_LENGTH_MISMATCH,
}
