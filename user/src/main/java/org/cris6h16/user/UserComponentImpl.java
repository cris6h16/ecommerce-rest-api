package org.cris6h16.user;

import lombok.extern.slf4j.Slf4j;
import org.cris6h16.user.Exceptions.AlreadyExistsException.EmailAlreadyExistsException;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static org.cris6h16.user.EntityMapper.toUserDTO;
import static org.cris6h16.user.EntityMapper.toUserEntity;

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
            throw new EmailAlreadyExistsException();
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
        userValidator.validateEmail(email);

        UserEntity ue = userRepository.findByEmailAndEnabled(email, enabled).orElse(null);
        return Optional.ofNullable(toUserDTO(ue));
    }


    @Override
    public void updateEmailVerifiedByEmail(String email, boolean emailVerified) {
        userValidator.validateEmail(email);

        userRepository.updateEmailVerifiedByEmail(email, emailVerified);
    }

    @Override
    public void updatePasswordByEmail(String email, String password) {
        userValidator.validateEmail(email);
        userValidator.validatePassword(password);

        userRepository.updatePasswordByEmail(email, password);
    }

    @Override
    public boolean existsByEmail(String email) {
        userValidator.validateEmail(email);

        return userRepository.existsByEmail(email);
    }

    @Override
    public Optional<UserOutput> findByIdAndEnable(Long id, boolean enabled) {
        userValidator.validateId(id);
        return userRepository.findByIdAndEnabled(id, enabled).map(EntityMapper::toUserDTO);
    }

}
