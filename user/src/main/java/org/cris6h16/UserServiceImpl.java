package org.cris6h16;

import org.cris6h16.Exceptions.AlreadyExistsException;
import org.cris6h16.Exceptions.NotFoundException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserValidator userValidator;
    private final ErrorMsgProperties errorProps;

    UserServiceImpl(UserRepository userRepository, UserValidator userValidator, ErrorMsgProperties errorMessagesProperties) {
        this.userRepository = userRepository;
        this.userValidator = userValidator;
        this.errorProps = errorMessagesProperties;
    }

    @Override
    public UserOutput createUser(CreateUserInput input) {
        input.prepare();
        isValid(input);
        checkDuplicates(input);
        UserEntity saved = userRepository.save(createUserEntity(input));
        return toUserOutput(saved);
    }

    private UserOutput toUserOutput(UserEntity saved) {
        return UserOutput.builder()
                .id(saved.getId())
                .firstname(saved.getFirstname())
                .lastname(saved.getLastName())
                .email(saved.getEmail())
                .password(saved.getPassword())
                .balance(saved.getBalance())
                .enabled(saved.isEnabled())
                .emailVerified(saved.isEmailVerified())
                .authorities(saved.getAuthorities())
                .build();
    }


    private void checkDuplicates(CreateUserInput user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new AlreadyExistsException(errorProps.getEmailAlreadyExists());
        }
    }

    private void isValid(CreateUserInput user) {
        userValidator.validateFirstname(user.getFirstname());
        userValidator.validateLastname(user.getLastname());
        userValidator.validateEmail(user.getEmail());
        userValidator.validatePassword(user.getPassword());
        userValidator.validateBalance(user.getBalance());
    }


    private UserEntity createUserEntity(CreateUserInput user) {
        return UserEntity.builder()
                .id(null)
                .firstname(user.getFirstname())
                .lastName(user.getLastname())
                .email(user.getEmail())
                .password(user.getPassword())
                .balance(BigDecimal.valueOf(0))
                .enabled(true)
                .emailVerified(false)
                .authorities(user.getAuthorities())
                .build();
    }

    @Override
    public UserOutput findByEmail(String email) {
        userValidator.validateEmail(email);
        UserEntity user = getByEmailElseThrow(email);
        return toUserOutput(user);
    }

    private UserEntity getByEmailElseThrow(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException(errorProps.getUserNotFound()));
    }
}
