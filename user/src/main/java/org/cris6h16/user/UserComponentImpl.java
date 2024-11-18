package org.cris6h16.user;

import lombok.extern.slf4j.Slf4j;
import org.cris6h16.user.Exceptions.UserComponentAttributeAlreadyExistsException;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static org.cris6h16.user.EntityMapper.toUserDTO;
import static org.cris6h16.user.EntityMapper.toUserEntity;
import static org.cris6h16.user.Exceptions.UserErrorCode.EMAIL_ALREADY_EXISTS;

@Service
@Slf4j
class UserComponentImpl implements UserComponent {

    private final UserRepository userRepository;
    private final UserValidator userValidator;
    private final AuthorityRepository authorityRepository;

    UserComponentImpl(UserRepository userRepository,
                      UserValidator userValidator,
                      AuthorityRepository authorityRepository) {
        this.userRepository = userRepository;
        this.userValidator = userValidator;
        this.authorityRepository = authorityRepository;
    }


    @Override
    public Long create(CreateUserInput input) {
        input.prepare();
        isValid(input);
        checkDuplicates(input);
        UserEntity entity = toUserEntity(input, authorityRepository);
        entity = userRepository.save(entity);
        return entity.getId();
    }

    //todo: agregar license & author



    private void checkDuplicates(CreateUserInput user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new UserComponentAttributeAlreadyExistsException(EMAIL_ALREADY_EXISTS);
        }
    }

    private void isValid(CreateUserInput user) {
        userValidator.validateFirstname(user.getFirstname());
        userValidator.validateLastname(user.getLastname());
        userValidator.validateEmail(user.getEmail());
        userValidator.validatePassword(user.getPassword());
    }


    @Override
    public Optional<UserOutput> findByEmailAndEnabled(String email, boolean enabled) {
        email = email == null ? "" : email.trim();
        userValidator.validateEmail(email);

        UserEntity ue = userRepository.findByEmailAndEnabled(email, enabled).orElse(null);
        return Optional.ofNullable(toUserDTO(ue));
    }


    @Override
    public void updateEmailVerifiedByEmail(String email, boolean emailVerified) {
        email = email == null ? "" : email.trim();
        userValidator.validateEmail(email);

        userRepository.updateEmailVerifiedByEmail(email, emailVerified);
    }

    @Override
    public void updatePasswordByEmail(String email, String password) {
        email = email == null ? "" : email.trim();
        password = password == null ? "" : password.trim();

        userValidator.validateEmail(email);
        userValidator.validatePassword(password);

        userRepository.updatePasswordByEmail(email, password);
    }

    @Override
    public boolean existsByEmail(String email) {
        email = email == null ? "" : email.trim();
        userValidator.validateEmail(email);

        return userRepository.existsByEmail(email);
    }

    @Override
    public Optional<UserOutput> findByIdAndEnable(Long id, boolean enabled) {
        userValidator.validateUserId(id);
        return userRepository.findByIdAndEnabled(id, enabled).map(EntityMapper::toUserDTO);
    }

    @Override
    public boolean existsByIdAndEnabled(Long id, boolean enabled) {
        userValidator.validateUserId(id);
        return userRepository.existsByIdAndEnabled(id, enabled);
    }

}
