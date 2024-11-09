package org.cris6h16.user;

import lombok.extern.slf4j.Slf4j;
import org.cris6h16.email.EmailService;
import org.cris6h16.user.Exceptions.AlreadyExistsException.EmailAlreadyExistsException;
import org.cris6h16.user.Exceptions.EmailNotVerifiedException;
import org.cris6h16.user.Exceptions.InvalidCredentialsException;
import org.cris6h16.GenAccessTokenInput;
import org.cris6h16.SecurityService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Service
@Slf4j
class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserValidator userValidator;
    private final EmailService emailService;
    private final SecurityService securityService;
    private final AuthorityRepository authorityRepository;

    UserServiceImpl(UserRepository userRepository, UserValidator userValidator, EmailService emailService, SecurityService securityService, AuthorityRepository authorityRepository) {
        this.userRepository = userRepository;
        this.userValidator = userValidator;
        this.emailService = emailService;
        this.securityService = securityService;
        this.authorityRepository = authorityRepository;
    }


    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.MANDATORY)
    public Long signup(SignupDTO input) {
        input.prepare();
        isValid(input);
        checkDuplicates(input);
        encodePass(input);
        UserEntity saved = userRepository.save(toUserEntity(input));
        sendVerificationEmail(saved);
        return saved.getId();
    }

    private void sendVerificationEmail(UserEntity saved) {
        emailService.remOldCodesAndCreateOneAndSendInEmailVerification(saved.getEmail());
    }

    private void encodePass(SignupDTO input) {
        String encodedPass = securityService.encodePassword(input.getPassword());
        input.setPassword(encodedPass);
    }


    private void checkDuplicates(SignupDTO user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new EmailAlreadyExistsException();
        }
    }

    private void isValid(SignupDTO user) {
        userValidator.validateFirstname(user.getFirstname());
        userValidator.validateLastname(user.getLastname());
        userValidator.validateEmail(user.getEmail());
        userValidator.validatePassword(user.getPassword());
    }


    private UserEntity toUserEntity(SignupDTO user) {
        log.debug("Creating UserEntity from SignupDTO");
        log.debug("User: {}", user);
        return UserEntity.builder()
                .id(null)
                .firstname(user.getFirstname())
                .lastName(user.getLastname())
                .email(user.getEmail())
                .password(user.getPassword())
                .balance(BigDecimal.valueOf(0))
                .enabled(true)
                .emailVerified(false)
                .authorities(getOrCreateAuthority(SignupDTO.DEF_AUTHORITY))
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
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.MANDATORY)
    public LoginOutput login(LoginDTO dto) {
        //todo: refactor todas las exception y poner el mensaje en el advice en lugar de en cadwa una
        Supplier<InvalidCredentialsException> credE = InvalidCredentialsException::new;
        Supplier<EmailNotVerifiedException> emailE = EmailNotVerifiedException::new;

        String email = dto.getEmail();
        String password = dto.getPassword();

        userValidator.validateEmail(email);
        userValidator.validatePassword(password);

        UserEntity entity = getByEmail(email, credE);
        passMatch(entity, password, credE);
        isEnable(entity, credE);
        isEmailVerified(entity, emailE);
        return createLoginOutput(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.MANDATORY)
    public void verifyEmail(VerifyEmailDTO dto) {
        emailService.checkCodeAfterRemAllMyCodes(dto.getEmail(), dto.getCode());
        userRepository.updateEmailVerifiedByEmail(dto.getEmail(), true);
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
            log.debug("Email not verified");
            emailService.remOldCodesAndCreateOneAndSendInEmailVerification(entity.getEmail());
            throw exceptionSupplier.get();
        }
        log.debug("Has a verified email");
    }

    private void isEnable(UserEntity user, Supplier<? extends RuntimeException> exceptionSupplier) {
        if (!user.isEnabled()) {
            log.debug("User is not enabled");
            throw exceptionSupplier.get();
        }
        log.debug("User is enabled");
    }

    private void passMatch(UserEntity entity, String rawPassword, Supplier<InvalidCredentialsException> exceptionSupplier) {
        if (!securityService.matches(rawPassword, entity.getPassword())) {
            log.debug("Password not match");
            throw exceptionSupplier.get();
        }
        log.debug("Password match");
    }
//
//    private <T> void notNull(T any, Supplier<? extends RuntimeException> exceptionSupplier) {
//        if (any == null) throw exceptionSupplier.get();
//    }

    private UserEntity getByEmail(String email, Supplier<? extends RuntimeException> exceptionSupplier) {
        try {
            return userRepository.findByEmail(email).orElseThrow(exceptionSupplier);
        } catch (RuntimeException e) {
            log.debug("User not found with email: {}, exception: {}", email, e.toString());
            throw e;
        }
    }
}
