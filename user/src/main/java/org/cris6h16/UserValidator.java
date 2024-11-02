package org.cris6h16;

import org.cris6h16.Exceptions.InvalidAttributeException;
import org.cris6h16.Properties.ErrorMessagesProperties;

import static org.cris6h16.UserEntity.*;

public class UserValidator {

    private final ErrorMessagesProperties propErrors;

    public UserValidator(ErrorMessagesProperties errorMessages) {
        this.propErrors = errorMessages;
    }

    public void validateFirstname(String firstname) {
        if (firstname.isEmpty() || firstname.length() > FIRSTNAME_LENGTH) {
            throw new InvalidAttributeException(propErrors.getFirstNameLength());
        }
    }

    public void validateLastname(String lastname) {
        if (lastname.isEmpty() || lastname.length() > LASTNAME_LENGTH) {
            throw new InvalidAttributeException(propErrors.getLastNameLength());
        }
    }


    public void validatePassword(String password) {
        if (password.length() < 8 || password.length() > PASSWORD_LENGTH) {
            throw new InvalidAttributeException(propErrors.getPasswordLength());
        }
    }

    //--> ^ = start of the string, \S = any non-whitespace character, + = one or more, @ = @, \S = any non-whitespace character, + = one or more, \. = ., \S = any non-whitespace character, + = one or more, $ = end of the string
    public void validateEmail(String email) {
        if (email.isEmpty() || email.length() > EMAIL_LENGTH || !email.matches("^\\S+@\\S+\\.\\S+$")) {
            throw new InvalidAttributeException(propErrors.getEmailInvalid());
        }
    }

//    public void validateId(Long id) {
//        if (id == null) {
//            throw new InvalidAttributeException(errors.getIdNull());
//        }
//    }
}
