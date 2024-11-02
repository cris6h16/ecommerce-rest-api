package org.cris6h16;

import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

public class UserUseCases {
    private final UserService userService;
    private final EmailService emailService;

    public UserUseCases(UserService userService, EmailService emailService) {
        this.userService = userService;
        this.emailService = emailService;
    }

    @Transactional(rollbackFor = Exception.class, isolation = Isolation.READ_COMMITTED)
    public void createUser(CreateUserInput input) {
        UserOutput output = userService.createUser(input);
        emailService.send
    }


}
