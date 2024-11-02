package org.cris6h16;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class VerificationCodeService {
    private final VerificationCodeGenerator codeGenerator;
    private final VerificationCodeRepository codeRepository;

    public VerificationCodeService(VerificationCodeGenerator codeGenerator, VerificationCodeRepository codeRepository) {
        this.codeGenerator = codeGenerator;
        this.codeRepository = codeRepository;
    }


    public String createAndSaveCode(String email) {
        var codeEntity = new VerificationCodeEntity(email, codeGenerator.genCode());
        codeRepository.save(codeEntity);
        return codeEntity.getCode();
    }
}


