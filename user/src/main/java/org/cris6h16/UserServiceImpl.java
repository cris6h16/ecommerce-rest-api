package org.cris6h16;

import org.cris6h16.Exceptions.AlreadyExistsException;
import org.cris6h16.Exceptions.EmailNotVerifiedException;
import org.cris6h16.Exceptions.InvalidCredentialsException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Service
class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserValidator userValidator;
    private final ErrorMsgProperties errorProps;
    private final EmailService emailService;
    private final SecurityService securityService;
    private final AuthorityRepository authorityRepository;

    UserServiceImpl(UserRepository userRepository, UserValidator userValidator, ErrorMsgProperties errorMessagesProperties, EmailService emailService, SecurityService securityService, AuthorityRepository authorityRepository) {
        this.userRepository = userRepository;
        this.userValidator = userValidator;
        this.errorProps = errorMessagesProperties;
        this.emailService = emailService;
        this.securityService = securityService;
        this.authorityRepository = authorityRepository;
    }

    @Override
    public void signup(SignupInput input) {
        input.prepare();
        isValid(input);
        checkDuplicates(input);
        encodePass(input);
        UserEntity saved = userRepository.save(toUserEntity(input));
        sendVerificationEmail(saved);
    }

    private void sendVerificationEmail(UserEntity saved) {
        emailService.sendEmailVerificationCode(saved.getEmail());
    }

    private void encodePass(SignupInput input) {
        String encodedPass = securityService.encodePassword(input.getPassword());
        input.setPassword(encodedPass);
    }


    private void checkDuplicates(SignupInput user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new AlreadyExistsException(errorProps.getEmailAlreadyExists());
        }
    }

    private void isValid(SignupInput user) {
        userValidator.validateFirstname(user.getFirstname());
        userValidator.validateLastname(user.getLastname());
        userValidator.validateEmail(user.getEmail());
        userValidator.validatePassword(user.getPassword());
    }


    private UserEntity toUserEntity(SignupInput user) {
        return UserEntity.builder()
                .id(null)
                .firstname(user.getFirstname())
                .lastName(user.getLastname())
                .email(user.getEmail())
                .password(user.getPassword())
                .balance(BigDecimal.valueOf(0))
                .enabled(true)
                .emailVerified(false)
                .authorities(getOrCreateAuthority(SignupInput.DEF_AUTHORITY))
                .build();
    }

    private Set<AuthorityEntity> getOrCreateAuthority(String authority) {
        Supplier<AuthorityEntity> authoritySupplier = () -> authorityRepository.save(AuthorityEntity.builder().name(authority).build());

        AuthorityEntity entity = authorityRepository.findByName(authority)
                .orElseGet(authoritySupplier);

        return new HashSet<>(List.of(entity));
    }


    //todo: crear un exception supplier el cual sera el encargado de injectar el mensaje antes de pasarnos
    @Override
    public LoginOutput login(String email, String password) {
        Supplier<InvalidCredentialsException> credE = () -> new InvalidCredentialsException(errorProps.getInvalidCredentials());
        Supplier<EmailNotVerifiedException> emailE = () -> new EmailNotVerifiedException(errorProps.getEmailNotVerified());

        userValidator.validateEmail(email);
        userValidator.validatePassword(password);

        UserEntity entity = getByEmail(email, credE);
        passMatch(entity, password, credE);
        isEnable(entity, credE);
        isEmailVerified(entity, emailE);
        return createLoginOutput(entity);
    }

    private LoginOutput createLoginOutput(UserEntity output) {
        GenAccessTokenInput input = toGenAccessTokenInput(output);

        String accessToken = securityService.generateAccessToken(input);
        String refreshToken = securityService.generateRefreshToken(output.getId());
        return new LoginOutput(accessToken, refreshToken);
    }

    private GenAccessTokenInput toGenAccessTokenInput(UserEntity entity) {
        return new GenAccessTokenInput(entity.getId(), entity.isEnabled(), toSetOfString(entity.getAuthorities()));
    }

    private Set<String> toSetOfString(Set<AuthorityEntity> authorities) {
        return authorities.stream()
                .map(AuthorityEntity::getName)
                .collect(Collectors.toSet());
    }

    private void isEmailVerified(UserEntity entity, Supplier<? extends RuntimeException> exceptionSupplier) {
        if (!entity.isEmailVerified()) {
            emailService.sendEmailVerificationCode(entity.getEmail());
            throw exceptionSupplier.get();
        }
    }

    private void isEnable(UserEntity user, Supplier<? extends RuntimeException> exceptionSupplier) {
        if (!user.isEnabled()) throw exceptionSupplier.get();
    }

    private void passMatch(UserEntity entity, String rawPassword, Supplier<InvalidCredentialsException> exceptionSupplier) {
        if (!securityService.matches(rawPassword, entity.getPassword())) {
            throw exceptionSupplier.get();
        }
    }
//
//    private <T> void notNull(T any, Supplier<? extends RuntimeException> exceptionSupplier) {
//        if (any == null) throw exceptionSupplier.get();
//    }

    private UserEntity getByEmail(String email, Supplier<? extends RuntimeException> exceptionSupplier) {
        return userRepository.findByEmail(email).orElseThrow(exceptionSupplier);
    }
}
