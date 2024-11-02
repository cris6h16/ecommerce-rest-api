package org.cris6h16.UseCases;

import org.cris6h16.Exceptions.EmailNotVerifiedException;
import org.cris6h16.Events.EmailVerificationRequiredEvent;
import org.cris6h16.ErrorMsgProperties;
import org.cris6h16.Exceptions.NotFoundException;
import org.cris6h16.Exceptions.InvalidCredentialsException;
import org.cris6h16.GenAccessTokenInput;
import org.cris6h16.UseCases.Input.LoginInput;
import org.cris6h16.UseCases.Output.LoginOutput;
import org.cris6h16.SecurityService;
import org.cris6h16.UserOutput;
import org.cris6h16.UserService;
import org.springframework.context.ApplicationEventPublisher;

public class LoginUseCase {
    private final UserService userService;
    private final SecurityService securityService;
    private final ErrorMsgProperties errorMsgProperties;
    private final ApplicationEventPublisher applicationEventPublisher;

    public LoginUseCase(UserService userService, SecurityService securityService, ErrorMsgProperties errorMsgProperties, ApplicationEventPublisher applicationEventPublisher) {
        this.userService = userService;
        this.securityService = securityService;
        this.errorMsgProperties = errorMsgProperties;
        this.applicationEventPublisher = applicationEventPublisher;
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
        GenAccessTokenInput input = genAccessTokenInput(output);

        String accessToken = securityService.generateAccessToken(input);
        String refreshToken = securityService.generateRefreshToken(output.getId());
        return new LoginOutput(accessToken, refreshToken);
    }

    private GenAccessTokenInput genAccessTokenInput(UserOutput output) {
        return new GenAccessTokenInput(output.getId(), output.isEnabled(), output.getAuthorities());
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
