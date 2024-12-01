package org.cris6h16.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.nio.channels.FileChannel;
import java.util.Optional;
import java.util.Set;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByEmail(String email);

    boolean existsByEmail(String email);

    Optional<UserEntity> findByEmailAndEnabled(String email, boolean isEnabled);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE users u SET u.emailVerified = ?2 WHERE u.email = ?1")
    void updateEmailVerifiedByEmail(String email, boolean isVerified);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE users u SET u.password = ?2 WHERE u.email = ?1")
    void updatePasswordByEmail(String email, String password);

    Optional<UserEntity> findByIdAndEnabled(Long id, boolean enabled);

    boolean existsByIdAndEnabled(Long id, boolean enabled);

    boolean existsByEmailAndEnabled(String email, boolean enabled);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE users u SET u.authorities = ?2 WHERE u.id = ?1")
    void updateAuthoritiesById(Long id, Set<AuthorityEntity> authorities);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE users u SET u.balance = ?2 WHERE u.id = ?1")
    void updateBalanceById(Long id, BigDecimal balance);
}
