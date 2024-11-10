package org.cris6h16.facades;

import org.cris6h16.email.EmailComponent;
import org.cris6h16.user.UserComponent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Component
public class EmailFacadeImpl implements EmailFacade {

    private final EmailComponent emailComponent;
    private final UserComponent userComponent;

    public EmailFacadeImpl(EmailComponent emailComponent, UserComponent userComponent) {
        this.emailComponent = emailComponent;
        this.userComponent = userComponent;
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.MANDATORY)
    public void sendEmailVerificationCodeIfExists(String email) {
        if (!existsUserByEmail(email)) return; //todo: logs this kind of omissions
        emailComponent.removeOldCodesByEmail(email);
        emailComponent.sendEmailVerificationCode(email);
    }

    private boolean existsUserByEmail(String email) {
        return userComponent.existsByEmail(email);
    }
}
