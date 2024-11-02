package org.cris6h16;

import java.time.LocalDateTime;

public interface VerificationCodeService {
    String createAndSaveCode(String email);
    int deleteByEmail(String email);
    boolean existsByEmailAndCodeAndExpiresAtBefore(String email, String code, LocalDateTime comparisonTime);
}
