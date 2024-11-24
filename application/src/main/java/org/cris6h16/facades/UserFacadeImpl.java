package org.cris6h16.facades;

import lombok.extern.slf4j.Slf4j;
import org.cris6h16.security.SecurityComponent;
import org.cris6h16.email.EmailComponent;
import org.cris6h16.user.LoginOutput;
import org.cris6h16.user.ResetPasswordDTO;
import org.cris6h16.user.CreateUserInput;
import org.cris6h16.user.UserComponent;
import org.cris6h16.user.UserOutput;
import org.cris6h16.user.UserValidator;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.function.Supplier;

@Slf4j
@Component
class UserFacadeImpl implements UserFacade {

    private final EmailComponent emailComponent;
    private final UserComponent userComponent;
    private final SecurityComponent securityComponent;
    private final UserValidator userValidator;

    public UserFacadeImpl(EmailComponent emailComponent,
                          UserComponent userComponent,
                          SecurityComponent securityComponent,
                          UserValidator userValidator) {
        this.emailComponent = emailComponent;
        this.userComponent = userComponent;
        this.securityComponent = securityComponent;
        this.userValidator = userValidator;
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.MANDATORY)
    public Long signup(SignupDTO dto) {
        CreateUserInput input = toCreateUserInput(dto);
        processPassword(input);
        setSignupDefaults(input);
        Long id = userComponent.create(input);
        emailComponent.removeOldCodesByEmail(input.getEmail());
        emailComponent.sendEmailVerificationCode(input.getEmail());
        return id;
    }

    private void processPassword(CreateUserInput input) {
        // replicacion de la logica de validacion de contrasena, esto lo hace el componente, pero al yo mandar la contrasena al componente ya encriptada la validacion de contrasena siempre sera exitosa ( length > 8 ) es por eso que es la unica validacion afuera del componente
        input.prepare();
        userValidator.validatePassword(input.getPassword()); // el validador del componente fue expuesto al exterior para evitar reescribir la logica de validacion de contrasena

        // encriptacion de la contrasena
        String encodedPass = securityComponent.encodePassword(input.getPassword());
        input.setPassword(encodedPass);
    }

    private CreateUserInput toCreateUserInput(SignupDTO dto) {
        return CreateUserInput.builder()
                .firstname(dto.getFirstname())
                .lastname(dto.getLastname())
                .email(dto.getEmail())
                .password(dto.getPassword())
                .build();
    }

    private void setSignupDefaults(CreateUserInput input) {
        input.setEnabled(true);
        input.setEmailVerified(false);
        input.setAuthorities(Set.of("ROLE_USER"));
    }



    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.MANDATORY)
    public LoginOutput login(LoginDTO loginDTO) {
        Supplier<ApplicationInvalidCredentialsException> credE = ApplicationInvalidCredentialsException::new;
        Supplier<ApplicationEmailNotVerifiedException> emailE = ApplicationEmailNotVerifiedException::new;

        UserOutput userDTO = userComponent.findByEmailAndEnabled(loginDTO.getEmail(), true).orElseThrow(credE);
        passMatches(loginDTO, userDTO, credE);
        isEmailVerified(userDTO, emailE);
        return createLoginOutput(userDTO);
    }

    private LoginOutput createLoginOutput(UserOutput userDTO) {
        Long id = userDTO.getId();
        Set<String> authorities = userDTO.getAuthorities();

        String accessToken = securityComponent.generateAccessToken(id, authorities);
        String refreshToken = securityComponent.generateRefreshToken(id, authorities);

        log.debug("Generated access token: {}, refresh token: {}", accessToken, refreshToken);
        return new LoginOutput(accessToken, refreshToken);
    }


    private void isEmailVerified(UserOutput userDTO, Supplier<? extends RuntimeException> exceptionSupplier) {
        if (!userDTO.isEmailVerified()) {
            log.debug("Email not verified");
            emailComponent.removeOldCodesByEmail(userDTO.getEmail());
            emailComponent.sendEmailVerificationCode(userDTO.getEmail());
            throw exceptionSupplier.get();
        }
        log.debug("Has a verified email");
    }

    private void passMatches(LoginDTO loginDTO, UserOutput userDTO, Supplier<? extends RuntimeException> exceptionSupplier) {
        if (!securityComponent.matches(loginDTO.getPassword(), userDTO.getPassword())) {
            log.debug("Password not match");
            throw exceptionSupplier.get();
        }
        log.debug("Password match");
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.MANDATORY)
    public void verifyEmail(VerifyEmailDTO dto) {
        Supplier<ApplicationEmailNotVerifiedException> emailE = ApplicationEmailNotVerifiedException::new;

        checkVerificationCode(dto.getEmail(), dto.getCode(), emailE);
        updateEmailVerifiedByEmail(dto.getEmail(), true);
        emailComponent.removeOldCodesByEmail(dto.getEmail());
    }

    private void updateEmailVerifiedByEmail(String email, boolean emailVerified) {
        userComponent.updateEmailVerifiedByEmail(email, emailVerified);
    }


    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.MANDATORY)
    public void resetPassword(ResetPasswordDTO dto) {
        Supplier<ApplicationValidVerificationCodeNotFoundException> codeE = ApplicationValidVerificationCodeNotFoundException::new;

        checkVerificationCode(dto.getEmail(), dto.getCode(), codeE);
        processPassword(dto);
        userComponent.updatePasswordByEmail(dto.getEmail(), dto.getPassword());
        emailComponent.removeOldCodesByEmail(dto.getEmail());
    }

    @Override
    public UserDTO me() {
        Long id = securityComponent.getCurrentUserId();
        UserOutput output =  userComponent.findByIdAndEnable(id, true).orElseThrow(ApplicationEnabledUserNotFoundException::new);
        return toUserDTO(output);
    }


    public static UserDTO toUserDTO(UserOutput output) {
        return UserDTO.builder()
                .id(output.getId())
                .firstname(output.getFirstname())
                .lastname(output.getLastname())
                .email(output.getEmail())
                .authorities(output.getAuthorities())
                .enabled(output.isEnabled())
                .balance(output.getBalance())
                .emailVerified(output.isEmailVerified())
                .build();
    }
    @Override
    public String refreshAccessToken() {
        Long id = securityComponent.getCurrentUserId();
        Set<String> authorities = securityComponent.getCurrentUserAuthorities();
        existsEnabledUser(id);
        return securityComponent.generateAccessToken(id, authorities);
    }

    private void existsEnabledUser(Long id) {
        if (!userComponent.existsByIdAndEnabled(id, true)) {
            throw new ApplicationEnabledUserNotFoundException();
        }
    }

    private void checkVerificationCode(String email, String code, Supplier<? extends RuntimeException> exceptionSupplier) {
        boolean valid = emailComponent.isCodeValid(email, code);
        if (!valid) {
            log.debug("Code is not valid");
            throw exceptionSupplier.get();
        }
        log.debug("Code is valid");
    }

    private void processPassword(ResetPasswordDTO dto) {
        String encodedPass = securityComponent.encodePassword(dto.getPassword());
        dto.setPassword(encodedPass);
    }
}
