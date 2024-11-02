package org.cris6h16.UseCases;

import org.cris6h16.CreateUserInput;
import org.cris6h16.ErrorMsgProperties;
import org.cris6h16.SecurityService;
import org.cris6h16.UserCreatedEvent;
import org.cris6h16.UserOutput;
import org.cris6h16.UserService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

public class CreateAccountUseCase {
    private final UserService userService;
    private final SecurityService securityService;
    private final ApplicationEventPublisher applicationEventPublisher;

    public CreateAccountUseCase(UserService userService, SecurityService securityService, ErrorMsgProperties errorMsgProperties, ApplicationEventPublisher applicationEventPublisher) {
        this.userService = userService;
        this.securityService = securityService;
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

}
