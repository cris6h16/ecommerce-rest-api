package org.cris6h16;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

 interface VerificationCodeRepository extends JpaRepository<VerificationCodeEntity, Long> {
    boolean existsByEmailAndCodeAndExpiresAtBefore(String email, String code, LocalDateTime comparisonTime);
    int deleteByEmail(String email);
}
