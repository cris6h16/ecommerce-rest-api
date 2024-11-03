package org.cris6h16;

import lombok.extern.slf4j.Slf4j;
import org.cris6h16.Exceptions.InvalidAttributeException.InvalidEmailException;
import org.cris6h16.Exceptions.InvalidAttributeException.InvalidFirstnameLengthException;
import org.cris6h16.Exceptions.InvalidAttributeException.InvalidLastnameLengthException;
import org.cris6h16.Exceptions.InvalidAttributeException.InvalidPasswordLengthException;
import org.springframework.stereotype.Component;

import static org.cris6h16.UserEntity.EMAIL_LENGTH;
import static org.cris6h16.UserEntity.FIRSTNAME_LENGTH;
import static org.cris6h16.UserEntity.LASTNAME_LENGTH;
import static org.cris6h16.UserEntity.PASSWORD_LENGTH;

@Slf4j
@Component
class UserValidator {
    //todo: eliminar property classes ya que se elimino el parametro string para recibir por el contructor

    public void validateFirstname(String firstname) {
        firstname = firstname == null ? "" : firstname.trim();
        if (firstname.isEmpty() || firstname.length() > FIRSTNAME_LENGTH) {
            throw new InvalidFirstnameLengthException();
        }
        log.debug("Valid first name: {}", firstname);
    }

    public void validateLastname(String lastname) {
        lastname = lastname == null ? "" : lastname.trim();
        if (lastname.isEmpty() || lastname.length() > LASTNAME_LENGTH) {
            throw new InvalidLastnameLengthException();
        }
        log.debug("Valid last name: {}", lastname);
    }


    public void validatePassword(String password) {
        password = password == null ? "" : password.trim();
        if (password.length() < 8 || password.length() > PASSWORD_LENGTH) {
            throw new InvalidPasswordLengthException();
        }
        log.debug("Valid password length: {}", password.length());
    }

    //--> ^ = start of the string, \S = any non-whitespace character, + = one or more, @ = @, \S = any non-whitespace character, + = one or more, \. = ., \S = any non-whitespace character, + = one or more, $ = end of the string
    public void validateEmail(String email) {
        email = email == null ? "" : email.trim();
        if (email.isEmpty() || email.length() > EMAIL_LENGTH || !email.matches("^\\S+@\\S+\\.\\S+$")) {
            throw new InvalidEmailException();
        }
        log.debug("Valid email: {}", email);
    }

}
