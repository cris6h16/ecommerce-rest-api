package org.cris6h16.facades;

import lombok.extern.slf4j.Slf4j;
import org.cris6h16.email.EmailComponent;
import org.cris6h16.facades.Exceptions.ApplicationErrorCode;
import org.cris6h16.facades.Exceptions.ApplicationException;
import org.cris6h16.security.SecurityComponent;
import org.cris6h16.user.CreateUserInput;
import org.cris6h16.user.EAuthority;
import org.cris6h16.user.LoginOutput;
import org.cris6h16.user.ResetPasswordDTO;
import org.cris6h16.user.UserComponent;
import org.cris6h16.user.UserOutput;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.cris6h16.facades.EmailCodeActionType.VERIFY_EMAIL;
import static org.cris6h16.facades.Exceptions.ApplicationErrorCode.EMAIL_NOT_VERIFIED;
import static org.cris6h16.facades.Exceptions.ApplicationErrorCode.ENABLED_USER_NOT_FOUND;
import static org.cris6h16.facades.Exceptions.ApplicationErrorCode.INVALID_CREDENTIALS;
import static org.cris6h16.facades.Exceptions.ApplicationErrorCode.VALID_VERIFICATION_CODE_NOT_FOUND;

@Slf4j
@Component
class UserFacadeImpl implements UserFacade {

    private final EmailComponent emailComponent;
    private final UserComponent userComponent;
    private final SecurityComponent securityComponent;

    public UserFacadeImpl(EmailComponent emailComponent,
                          UserComponent userComponent,
                          SecurityComponent securityComponent
    ) {
        this.emailComponent = emailComponent;
        this.userComponent = userComponent;
        this.securityComponent = securityComponent;
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.MANDATORY)
    public Long signup(SignupDTO dto) {
        CreateUserInput input = toCreateUserInput(dto);
        processPassword(input);
        setSignupDefaults(input);

        return userComponent.create(input);
    }

    private void processPassword(CreateUserInput input) {
        // validacion temprana de contrasena, la unica validacion afuera del componente ya que la contrasena al componente llega ya encriptada la validacion de contrasena siempre sera exitosa ( length > 8 ) es por eso que es la unica validacion afuera del componente
        String validPass = userComponent.isPassValidElseThrow(input.getPassword());

        // encriptacion de la contrasena
        String encodedPass = securityComponent.encodePassword(validPass);
        input.setPassword(encodedPass);
    }

    private void processPassword(ResetPasswordDTO input) {
        // validacion temprana de contrasena, la unica validacion afuera del componente ya que la contrasena al componente llega ya encriptada la validacion de contrasena siempre sera exitosa ( length > 8 ) es por eso que es la unica validacion afuera del componente
        String passwoed = userComponent.isPassValidElseThrow(input.getPassword());

        // encriptacion de la contrasena
        String encodedPass = securityComponent.encodePassword(passwoed);
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
        input.setAuthority(EAuthority.ROLE_USER);
    }


    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.MANDATORY)
    public LoginOutput login(LoginDTO loginDTO) {
        UserOutput output = userComponent.findByEmailAndEnabled(loginDTO.getEmail(), true).orElseThrow(() -> new ApplicationException(INVALID_CREDENTIALS));
        passMatches(loginDTO, output, INVALID_CREDENTIALS);
        return createLoginOutput(output);
    }

    private LoginOutput createLoginOutput(UserOutput userDTO) {
        Long id = userDTO.getId();
        String authority = userDTO.getAuthority();

        String accessToken = securityComponent.generateAccessToken(id, authority);
        String refreshToken = securityComponent.generateRefreshToken(id, authority);

        return new LoginOutput(accessToken, refreshToken);
    }


//    todo: agregar logs con AOP


    private void passMatches(LoginDTO loginDTO, UserOutput userOutput, ApplicationErrorCode errorCode) {
        if (!securityComponent.matches(loginDTO.getPassword(), userOutput.getPassword())) {
            throw new ApplicationException(errorCode);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.MANDATORY)
    public void verifyEmail(VerifyEmailDTO dto) {
        String actionType = EmailCodeActionType.VERIFY_EMAIL.name();

        existsEnabledUserByEmail(dto.getEmail());
        isCodeValid(dto.getEmail(), dto.getCode(), actionType, VALID_VERIFICATION_CODE_NOT_FOUND);
        userComponent.updateEmailVerifiedByEmail(dto.getEmail(), true);
        emailComponent.updateUsedByEmailAndActionType(dto.getEmail(), actionType, true);
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.MANDATORY)
    public void resetPassword(ResetPasswordDTO dto) {
        String actionType = EmailCodeActionType.RESET_PASSWORD.name();

        existsEnabledUserByEmail(dto.getEmail());
        processPassword(dto);
        isCodeValid(dto.getEmail(), dto.getCode(), actionType, VALID_VERIFICATION_CODE_NOT_FOUND);
        userComponent.updatePasswordByEmail(dto.getEmail(), dto.getPassword());
        emailComponent.updateUsedByEmailAndActionType(dto.getEmail(), actionType, true);
    }

    private void existsEnabledUserByEmail(String email) {
        if (!userComponent.existsByEmailAndEnabled(email, true)) {
            throw new ApplicationException(VALID_VERIFICATION_CODE_NOT_FOUND);
        }
    }


    public static UserDTO toUserDTO(UserOutput output) {
        return UserDTO.builder()
                .id(output.getId())
                .firstname(output.getFirstname())
                .lastname(output.getLastname())
                .email(output.getEmail())
                .authority(output.getAuthority())
                .enabled(output.isEnabled())
                .balance(output.getBalance())
                .emailVerified(output.isEmailVerified())
                .build();
    }

    @Override
    public String refreshAccessToken() {
        Long id = securityComponent.getCurrentUserId();
        String authority = securityComponent.getCurrentUserAuthority();
        existsEnabledUserByEmail(id);
        return securityComponent.generateAccessToken(id, authority);
    }

    @Override
    public Page<UserDTO> findAll(Pageable pageable) {
        return userComponent.findAll(pageable).map(UserFacadeImpl::toUserDTO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.MANDATORY)
    public void updateRole(Long id, String authority) {
        existsEnabledUserByEmail(id);
        userComponent.updateAuthorityById(id, EAuthority.valueOf(authority));
    }

//    @Override
//    public UserDTO me() {
//        Long id = securityComponent.getCurrentUserId();
//        return userComponent.findByIdAndEnable(id, true)
//                .map(UserFacadeImpl::toUserDTO)
//                .orElseThrow(()->new ApplicationException(ENABLED_USER_NOT_FOUND_BY_ID));
//    }

    @Override
    public UserDTO findById(Long id) {
        existsEnabledUserByEmail(id);
        return userComponent.findByIdAndEnable(id, true)
                .map(UserFacadeImpl::toUserDTO)
                .orElseThrow(() -> new ApplicationException(ENABLED_USER_NOT_FOUND));
    }

    @Override
    public void adjustBalance(Long id, BigDecimal delta) {
        existsEnabledUserByEmail(id);
        userComponent.adjustBalanceById(id, delta);
    }


    private void existsEnabledUserByEmail(Long id) {
        if (!userComponent.existsByIdAndEnabled(id, true)) {
            throw new ApplicationException(ENABLED_USER_NOT_FOUND);
        }
    }


    private void isCodeValid(String email, String code, String actionType, ApplicationErrorCode errorCode) {
        boolean valid = emailComponent.isCodeValid(email, code, actionType);
        if (!valid) throw new ApplicationException(errorCode);
    }

}
