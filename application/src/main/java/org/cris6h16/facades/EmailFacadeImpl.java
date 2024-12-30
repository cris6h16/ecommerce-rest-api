package org.cris6h16.facades;

import lombok.extern.slf4j.Slf4j;
import org.cris6h16.email.EmailComponent;
import org.cris6h16.facades.Exceptions.ApplicationErrorCode;
import org.cris6h16.facades.Exceptions.ApplicationException;
import org.cris6h16.user.UserComponent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import static org.cris6h16.facades.Exceptions.ApplicationErrorCode.INVALID_ACTION_TYPE;

@Component
@Slf4j
public class EmailFacadeImpl implements EmailFacade {

    private final EmailComponent emailComponent;
    private final UserComponent userComponent;

    public EmailFacadeImpl(EmailComponent emailComponent, UserComponent userComponent) {
        this.emailComponent = emailComponent;
        this.userComponent = userComponent;
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.MANDATORY)
    public void sendEmailVerificationCodeIfExists(SendEmailVerificationDTO dto) {
        String email = dto.getEmail();
        String actionType = toActionTypeEnum(dto.getActionType());

        if (!existsUserByEmail(email)) {
            log.debug("User with email {} does not exist", email);
            return;
        }
        emailComponent.removeByEmailAndActionType(email, actionType);
        emailComponent.sendEmailVerificationCode(email, actionType);
    }

    private String toActionTypeEnum(String actionType) {
        actionType = actionType.toUpperCase().trim();
        if (EmailCodeActionType.contains(actionType)) {
            return actionType;
        }
        throw new ApplicationException(INVALID_ACTION_TYPE);
    }

    private boolean existsUserByEmail(String email) {
        return userComponent.existsByEmail(email);
    }
}
