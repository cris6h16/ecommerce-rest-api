package org.cris6h16.user;

import java.util.Optional;

public interface UserComponent {
    Long create(CreateUserInput user);
    Optional<UserOutput> findByEmailAndEnabled(String email, boolean enabled);
    void updateEmailVerifiedByEmail(String email, boolean emailVerified);
    void updatePasswordByEmail(String email, String password);

    boolean existsByEmail(String email);

    Optional<UserOutput> findByIdAndEnable(Long id, boolean enabled);
}
