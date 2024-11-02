package org.cris6h16;

import org.cris6h16.Exceptions.NotFoundException;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

public class UserUseCases {
    private final UserService userService;
    private final SecurityService securityService;
    private final ErrorMsgProperties errorMsgProperties;
    private final ApplicationEventPublisher applicationEventPublisher;

    public UserUseCases(UserService userService, EmailService emailService, SecurityService securityService, ErrorMsgProperties errorMsgProperties, ApplicationEventPublisher applicationEventPublisher) {
        this.userService = userService;
        this.securityService = securityService;
        this.errorMsgProperties = errorMsgProperties;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Transactional(rollbackFor = Exception.class, isolation = Isolation.READ_COMMITTED)
    public void createUser(CreateUserInput input) {
        encodePass(input);
        UserOutput output = userService.createUser(input);
        publishUserCreatedEvent(output);
    }

    private void publishUserCreatedEvent(UserOutput output) {
        applicationEventPublisher.publishEvent(new UserCreatedEvent(output.getEmail()));
    }

    private void encodePass(CreateUserInput input) {
        String encodedPass = securityService.encodePassword(input.getPassword());
        input.setPassword(encodedPass);
    }


    @SuppressWarnings("ConstantConditions")
    public LoginOutput login(LoginInput input) {
        UserOutput output = findByEmailIfNotFoundThenNull(input.getEmail());
        notNull(output);
        passMatch(input, output);
        isEnable(output);
        isEmailVerified(output);
        return createLoginOutput(output);
    }

    private void notNull(UserOutput output) {
        if (output == null) {
            throwInvalidCredentialsException();
        }
    }

    /**
     * Ignores not found exceptions
     *
     * @param email
     * @return
     */
    private UserOutput findByEmailIfNotFoundThenNull(String email) {
        try {
            return userService.findByEmail(email);
        } catch (NotFoundException e) {
            return null;
        }
    }

    private LoginOutput createLoginOutput(UserOutput output) {
        String accessToken = securityService.generateAccessToken(output.getEmail());
        String refreshToken = securityService.generateRefreshToken(output.getEmail());
        return new LoginOutput(accessToken, refreshToken);
    }

    private void isEmailVerified(UserOutput output) {
        if (!output.isEmailVerified()) {
            applicationEventPublisher.publishEvent(new EmailVerificationRequiredEvent(output.getEmail()));
            throw new EmailNotVerifiedException(errorMsgProperties.getEmailNotVerified());
        }
    }

    private void isEnable(UserOutput output) {
        if (!output.isEnabled()) {
            throwInvalidCredentialsException();
        }
    }

    private void passMatch(LoginInput input, UserOutput output) {
        if (!securityService.matches(input.getPassword(), output.getPassword())) {
            throwInvalidCredentialsException();
        }
    }

    // Would have been better with a functional approach, but is harder for debug/test the time i had is limited
    private void throwInvalidCredentialsException() {
        throw new InvalidCredentialsException(errorMsgProperties.getInvalidCredentials());
    }


}
