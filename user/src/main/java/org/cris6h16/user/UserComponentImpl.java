package org.cris6h16.user;

import lombok.extern.slf4j.Slf4j;
import org.cris6h16.user.Exceptions.UserComponentException;
import org.cris6h16.user.Exceptions.UserErrorCode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

import static org.cris6h16.user.EntityMapper.toUserEntity;
import static org.cris6h16.user.EntityMapper.toUserOutput;
import static org.cris6h16.user.Exceptions.UserErrorCode.EMAIL_ALREADY_EXISTS;
import static org.cris6h16.user.Exceptions.UserErrorCode.EMAIL_NULL;
import static org.cris6h16.user.Exceptions.UserErrorCode.EMAIL_REGEX_MISMATCH;
import static org.cris6h16.user.Exceptions.UserErrorCode.EMAIL_TOO_LONG;
import static org.cris6h16.user.Exceptions.UserErrorCode.USER_NOT_FOUND_BY_ID;
import static org.cris6h16.user.UserEntity.EMAIL_MAX_LENGTH;

@Service
@Slf4j
class UserComponentImpl implements UserComponent {

    private final UserRepository userRepository;

    UserComponentImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public Long create(CreateUserInput input) {
        input.prepare(); // trim & null to empty
        validate(input);
        checkDuplicates(input);
        UserEntity entity = toUserEntity(input);
        entity = userRepository.save(entity);
        return entity.getId();
    }

    //todo: agregar license & author


    private void checkDuplicates(CreateUserInput user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new UserComponentException(EMAIL_ALREADY_EXISTS);
        }
    }

    private void validate(CreateUserInput user) {
        if (!user.getEmail().matches("^\\S+@\\S+\\.\\S+$")) {
            throw new UserComponentException(EMAIL_REGEX_MISMATCH);
        }
    }



    @Override
    public Optional<UserOutput> findByEmailAndEnabled(String email, boolean enabled) {
        email = userValidator.validateEmail(email);

        UserEntity ue = userRepository.findByEmailAndEnabled(email, enabled).orElse(null);
        return Optional.ofNullable(toUserOutput(ue));
    }


    @Override
    public void updateEmailVerifiedByEmail(String email, boolean emailVerified) {
        email = userValidator.validateEmail(email);

        userRepository.updateEmailVerifiedByEmail(email, emailVerified);
    }

    @Override
    public void updatePasswordByEmail(String email, String password) {
        email = userValidator.validateEmail(email);
        password = userValidator.validatePassword(password);

        userRepository.updatePasswordByEmail(email, password);
    }

    @Override
    public boolean existsByEmail(String email) {
        email = userValidator.validateEmail(email);
        return userRepository.existsByEmail(email);
    }

    @Override
    public Optional<UserOutput> findByIdAndEnable(Long id, boolean enabled) {
        userValidator.validateUserId(id);
        return userRepository.findByIdAndEnabled(id, enabled).map(EntityMapper::toUserOutput);
    }

    @Override
    public boolean existsByIdAndEnabled(Long id, boolean enabled) {
        userValidator.validateUserId(id);
        return userRepository.existsByIdAndEnabled(id, enabled);
    }

    @Override
    public void deleteAll() {
        userRepository.deleteAll();
    }
//
//    @Override
//    public boolean existsByEmailAndEnabled(String email, boolean enabled) {
//        email = userValidator.validateEmail(email);
//
//        return userRepository.existsByEmailAndEnabled(email, enabled);
//    }

    @Override
    public Page<UserOutput> findAll(Pageable pageable) {
        return userRepository.findAll(pageable).map(EntityMapper::toUserOutput);
    }

    @Override
    public void updateAuthorityById(Long id, EAuthority authority) {
        userValidator.validateUserId(id);
        userRepository.updateAuthoritiesById(id, authority);
    }

    @Override
    public void adjustBalanceById(Long id, BigDecimal delta) {
        userValidator.validateUserId(id);

        BigDecimal balance = userRepository.findBalanceById(id).orElseThrow(() -> new UserComponentException(USER_NOT_FOUND_BY_ID));
        balance = balance.add(delta);
        userValidator.validateBalance(balance);

        userRepository.updateBalanceById(id, balance);
    }

    @Override
    public String isPassValidElseThrow(String password) {
        return userValidator.validatePassword(password);
    }

    @Override
    public void deleteByEmail(String email) {
        email = userValidator.validateEmail(email);
        userRepository.deleteByEmail(email);
    }

    @Override
    public boolean existsByEmailAndEnabled(String email, boolean enabled) {
        email = userValidator.validateEmail(email);
        return userRepository.existsByEmailAndEnabled(email, enabled);
    }

}
