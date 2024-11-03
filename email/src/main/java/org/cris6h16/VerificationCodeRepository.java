package org.cris6h16;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;
@Repository
 interface VerificationCodeRepository extends JpaRepository<VerificationCodeEntity, Long> {
    // expiresAt > comparisonTime
    boolean existsByEmailAndCodeAndExpiresAtAfter(String email, String code, LocalDateTime comparisonTime);
    int deleteByEmail(String email);
}
