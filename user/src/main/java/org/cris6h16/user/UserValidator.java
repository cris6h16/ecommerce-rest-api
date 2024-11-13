package org.cris6h16.user;

import lombok.extern.slf4j.Slf4j;
import org.cris6h16.user.Exceptions.InvalidAttributeException.UserInvalidEmailException;
import org.cris6h16.user.Exceptions.InvalidAttributeException.UserInvalidFirstnameLengthException;
import org.cris6h16.user.Exceptions.InvalidAttributeException.UserInvalidIdException;
import org.cris6h16.user.Exceptions.InvalidAttributeException.UserInvalidLastnameLengthException;
import org.cris6h16.user.Exceptions.InvalidAttributeException.UserInvalidPasswordLengthException;
import org.springframework.stereotype.Component;

import static org.cris6h16.user.UserEntity.EMAIL_LENGTH;
import static org.cris6h16.user.UserEntity.FIRSTNAME_LENGTH;
import static org.cris6h16.user.UserEntity.LASTNAME_LENGTH;
import static org.cris6h16.user.UserEntity.PASSWORD_LENGTH;

@Slf4j
@Component
 public class UserValidator {
    //todo: eliminar property classes ya que se elimino el parametro string para recibir por el contructor

    public void validateFirstname(String firstname) {
        firstname = firstname == null ? "" : firstname.trim();
        if (firstname.isEmpty() || firstname.length() > FIRSTNAME_LENGTH) {
            throw new UserInvalidFirstnameLengthException();
        }
        log.debug("Valid first name: {}", firstname);
    }

    public void validateLastname(String lastname) {
        lastname = lastname == null ? "" : lastname.trim();
        if (lastname.isEmpty() || lastname.length() > LASTNAME_LENGTH) {
            throw new UserInvalidLastnameLengthException();
        }
        log.debug("Valid last name: {}", lastname);
    }


    public void validatePassword(String password) {
        password = password == null ? "" : password.trim();
        if (password.length() < 8 || password.length() > PASSWORD_LENGTH) {
            throw new UserInvalidPasswordLengthException();
        }
        log.debug("Valid password length: {}", password.length());
        log.debug("Valid password: {}", password);
    }

    //--> ^ = start of the string, \S = any non-whitespace character, + = one or more, @ = @, \S = any non-whitespace character, + = one or more, \. = ., \S = any non-whitespace character, + = one or more, $ = end of the string
    public void validateEmail(String email) {
        email = email == null ? "" : email.trim();
        if (email.isEmpty() || email.length() > EMAIL_LENGTH || !email.matches("^\\S+@\\S+\\.\\S+$")) {
            throw new UserInvalidEmailException();
        }
        log.debug("Valid email: {}", email);
    }

    void validateId(Long id) {
        if (id == null || id <= 0) {
            throw new UserInvalidIdException();
        }
        log.debug("Valid id: {}", id);
    }
}
