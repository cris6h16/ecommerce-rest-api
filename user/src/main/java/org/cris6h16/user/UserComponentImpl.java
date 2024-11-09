package org.cris6h16.user;

import lombok.extern.slf4j.Slf4j;
import org.cris6h16.user.Exceptions.AlreadyExistsException.EmailAlreadyExistsException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;

import static org.cris6h16.user.EntityMapper.toUserDTO;
import static org.cris6h16.user.EntityMapper.toUserEntity;

@Service
@Slf4j
class UserComponentImpl implements UserComponent {

    private final UserRepository userRepository;
    private final UserValidator userValidator;
    private final AuthorityRepository authorityRepository;

    UserComponentImpl(UserRepository userRepository, UserValidator userValidator, AuthorityRepository authorityRepository) {
        this.userRepository = userRepository;
        this.userValidator = userValidator;
        this.authorityRepository = authorityRepository;
    }


    @Override
    public Long create(SignupDTO input) {
        input.prepare();
        isValid(input);
        checkDuplicates(input);
        UserEntity entity = toUserEntity(input);
        entity.setAuthorities(getOrCreateAuthority(SignupDTO.DEF_AUTHORITY));
        entity = userRepository.save(entity);
        return entity.getId();
    }

    //todo: agregar license & author

    @SuppressWarnings("SameParameterValue")
    private Set<AuthorityEntity> getOrCreateAuthority(String authority) {
        Supplier<AuthorityEntity> saved = () ->
                authorityRepository.save(AuthorityEntity.builder().name(authority).build());

        AuthorityEntity entity = authorityRepository
                .findByName(authority).orElseGet(saved);

        return new HashSet<>(List.of(entity));
    }

    private void checkDuplicates(SignupDTO user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new EmailAlreadyExistsException();
        }
    }

    private void isValid(SignupDTO user) {
        userValidator.validateFirstname(user.getFirstname());
        userValidator.validateLastname(user.getLastname());
        userValidator.validateEmail(user.getEmail());
        userValidator.validatePassword(user.getPassword());
    }


    @Override
    public Optional<UserDTO> findByEmailAndEnabled(String email, boolean enabled) {
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

}
