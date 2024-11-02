package org.cris6h16;

import org.cris6h16.Exceptions.AlreadyExistsException;
import org.cris6h16.Exceptions.EmailNotVerifiedException;
import org.cris6h16.Exceptions.InvalidCredentialsException;
import org.springframework.context.ApplicationEventPublisher;

import java.math.BigDecimal;
import java.util.Set;

class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserValidator userValidator;
    private final ErrorMessagesProperties errorProps;

    UserServiceImpl(UserRepository userRepository, UserValidator userValidator, ErrorMessagesProperties errorMessagesProperties) {
        this.userRepository = userRepository;
        this.userValidator = userValidator;
        this.errorProps = errorMessagesProperties;
    }

    @Override
    public UserOutput createUser(CreateUserInput user) {
        user.prepare();
        isValid(user);
        checkDuplicates(user);
        UserEntity userEntity = createUserEntity(user);
        UserEntity savedUserEntity = userRepository.save(userEntity);
        return createUserOutput(savedUserEntity);
    }


    private void checkDuplicates(CreateUserInput user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new AlreadyExistsException(errorProps.getEmailAlreadyExists());
        }
    }

    private void isValid(CreateUserInput user) {
        userValidator.validateEmail(user.getEmail());
        userValidator.validatePassword(user.getPassword());
        userValidator.validateFirstname(user.getFirstname());
        userValidator.validateLastname(user.getLastname());
    }

    private UserOutput createUserOutput(UserEntity ue) {
        return UserOutput.builder()
                .email(ue.getEmail())
                .name(ue.getFirstname())
                .lastName(ue.getLastName())
                .id(ue.getId())
                .build();
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
                .authorities(Set.of(EAuthority.USER_ROLE))
                .build();
    }

    @Override
    public UserOutput login(String email, String password) {
        userValidator.validateEmail(email);

        // InvalidCredentialsException
        UserEntity user = getByEmailAndEnabledElseThrow(email, true);
        passMatchElseThrow(password, user.getPassword());

        // EmailNotVerifiedException
        isEmailVerifiedElseThrow(user);

        return createUserOutput(user);
    }

    private void isEmailVerifiedElseThrow(UserEntity user) {
        if (!user.isEmailVerified()) {
            throw new EmailNotVerifiedException(errorProps.getEmailNotVerified());
        }
    }

    private void passMatchElseThrow(String password, String passwordHash) {
        if (!passwordEncoder.matches(password, passwordHash)) {
            throw new InvalidCredentialsException(errorProps.getInvalidCredentials());
        }
    }

    private UserEntity getByEmailAndEnabledElseThrow(String email, boolean isEnabled) {
        return userRepository.findByEmailAndEnabled(email, isEnabled)
                .orElseThrow(() -> new InvalidCredentialsException(errorProps.getInvalidCredentials()));
    }

    @Override
    public UserOutput getUserByEmail(String email) {
        return null;
    }
}
