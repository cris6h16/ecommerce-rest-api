package org.cris6h16;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByEmail(String email);

    boolean existsByEmail(String email);

    Optional<UserEntity> findByEmailAndEnabled(String email, boolean isEnabled);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE users u SET u.emailVerified = ?2 WHERE u.email = ?1")
    void updateEmailVerifiedByEmail(String email, boolean isVerified);
}
