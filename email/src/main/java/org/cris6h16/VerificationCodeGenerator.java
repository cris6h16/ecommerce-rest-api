package org.cris6h16;

import java.security.SecureRandom;

import static org.cris6h16.VerificationCodeEntity.CODE_LENGTH;

 class VerificationCodeGenerator {
    private static final String CHARACTERS = "abcdefghijklmnopqrstuvwxyz0123456789";
    private static final SecureRandom random = new SecureRandom();

    String genCode() {
        StringBuilder sb = new StringBuilder(CODE_LENGTH);
        for (int i = 0; i < CODE_LENGTH; i++) {
            int idx = random.nextInt(CHARACTERS.length()); // Secure random index
            sb.append(CHARACTERS.charAt(idx));
        }
        return sb.toString();
    }
}