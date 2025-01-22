package org.cris6h16.facades;

import org.cris6h16.facades.Exceptions.ApplicationErrorCode;
import org.cris6h16.facades.Exceptions.ApplicationException;
import org.cris6h16.user.UserComponent;

final class FacadesCommon {
    static void isUserEnabledById(Long userId, UserComponent userComponent) {
        if (!userComponent.existsByIdAndEnabled(userId, true)) {
            throw new ApplicationException(ApplicationErrorCode.ENABLED_USER_NOT_FOUND);
        }
    }
}
