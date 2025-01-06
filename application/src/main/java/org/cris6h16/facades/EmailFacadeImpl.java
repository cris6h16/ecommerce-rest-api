package org.cris6h16.facades;

import lombok.extern.slf4j.Slf4j;
import org.cris6h16.email.EmailComponent;
import org.cris6h16.facades.Exceptions.ApplicationException;
import org.cris6h16.user.UserComponent;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionTemplate;

import static org.cris6h16.facades.Exceptions.ApplicationErrorCode.UNSUPPORTED_ACTION_TYPE;

@Component
@Slf4j
public class EmailFacadeImpl implements EmailFacade {

    private final EmailComponent emailComponent;
    private final UserComponent userComponent;
    private final ThreadPoolTaskExecutor taskExecutor;
    private final TransactionTemplate transactionTemplate;


    public EmailFacadeImpl(EmailComponent emailComponent, UserComponent userComponent, ThreadPoolTaskExecutor taskExecutor, TransactionTemplate transactionTemplate) {
        this.emailComponent = emailComponent;
        this.userComponent = userComponent;
        this.taskExecutor = taskExecutor;
        this.transactionTemplate = transactionTemplate;
    }

    @Override
    public void sendEmailVerificationCodeIfExists(SendEmailVerificationDTO dto) {
        String email = dto.getEmail();
        String actionType = toActionTypeEnum(dto.getActionType());

        if (!existsUserByEmail(email)) {
            return;
        }

        taskExecutor.execute(() -> {
            transactionTemplate.execute((TransactionStatus status) -> {
                try {
                    emailComponent.sendEmailVerificationCode(email, actionType);

                } catch (Exception e) {
                    status.setRollbackOnly();
                }
                return null;
            });
        });
    }

    private String toActionTypeEnum(String actionType) {
        actionType = actionType.toUpperCase().trim();
        if (EmailCodeActionType.contains(actionType)) {
            return actionType;
        }
        throw new ApplicationException(UNSUPPORTED_ACTION_TYPE);
    }

    private boolean existsUserByEmail(String email) {
        return userComponent.existsByEmail(email);
    }
}
