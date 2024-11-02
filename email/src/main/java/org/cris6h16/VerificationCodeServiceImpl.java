package org.cris6h16;

import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

@Slf4j
public class VerificationCodeServiceImpl implements VerificationCodeService{
    private final VerificationCodeGenerator codeGenerator;
    private final VerificationCodeRepository codeRepository;

    public VerificationCodeServiceImpl(VerificationCodeGenerator codeGenerator, VerificationCodeRepository codeRepository) {
        this.codeGenerator = codeGenerator;
        this.codeRepository = codeRepository;
    }


    @Override
    public String createAndSaveCode(String email) {
        var codeEntity = create(email);
        return save(codeEntity).getCode();
    }

    private VerificationCodeEntity save(VerificationCodeEntity codeEntity) {
        codeEntity = codeRepository.save(codeEntity);
        return codeEntity;
    }


    private VerificationCodeEntity create(String email) {
        return new VerificationCodeEntity(email, codeGenerator.genCode());
    }

    @Override
    public int deleteByEmail(String email) {
        return codeRepository.deleteByEmail(email);
    }

    @Override
    public boolean existsByEmailAndCodeAndExpiresAtBefore(String email, String code, LocalDateTime comparisonTime) {
        return codeRepository.existsByEmailAndCodeAndExpiresAtBefore(email, code, comparisonTime);
    }


}


