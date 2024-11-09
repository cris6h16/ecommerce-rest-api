package org.cris6h16.user;

import java.util.Optional;

public interface UserComponent {
    Long create(SignupDTO user);
    Optional<UserDTO> findByEmailAndEnabled(String email, boolean enabled);
    void updateEmailVerifiedByEmail(String email, boolean emailVerified);
    void updatePasswordByEmail(String email, String password);
}
