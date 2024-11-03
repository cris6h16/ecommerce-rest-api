package org.cris6h16;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
public interface VerificationCodeService {
    String createAndSaveCode(String email);
    int deleteByEmail(String email);
    boolean existsByEmailAndCodeAndExpiresAtAfter(String email, String code, LocalDateTime comparisonTime);
}
