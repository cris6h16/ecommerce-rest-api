package org.cris6h16.email;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
 interface VerificationCodeRepository extends JpaRepository<VerificationCodeEntity, Long> {
    // expiresAt > comparisonTime
    boolean existsByEmailAndCodeAndExpiresAtAfter(String email, String code, LocalDateTime comparisonTime);
    int deleteByEmail(String email);
}