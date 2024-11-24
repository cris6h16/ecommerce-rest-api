package org.cris6h16.user;

import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class EntityMapper {
    public   static UserOutput toUserOutput(UserEntity userEntity) {
        if (userEntity == null) return null;
        return UserOutput.builder()
                .id(userEntity.getId())
                .firstname(userEntity.getFirstname())
                .lastname(userEntity.getLastname())
                .email(userEntity.getEmail())
                .password(userEntity.getPassword())
                .enabled(userEntity.isEnabled())
                .emailVerified(userEntity.isEmailVerified())
                .authorities(toSetOfString(userEntity.getAuthorities()))
                .balance(userEntity.getBalance())
                .build();
    }


    private static Set<String> toSetOfString(Set<AuthorityEntity> authorities) {
        return authorities.stream()
                .map(AuthorityEntity::getName)
                .collect(Collectors.toSet());
    }


    static UserEntity toUserEntity(CreateUserInput user, AuthorityRepository authorityRepository) {
        return UserEntity.builder()
                .id(null)
                .firstname(user.getFirstname())
                .lastname(user.getLastname())
                .email(user.getEmail())
                .password(user.getPassword())
                .balance(user.getBalance())
                .enabled(user.isEnabled())
                .emailVerified(user.isEmailVerified())
                .authorities(getOrCreateAuthorities(user.getAuthorities(), authorityRepository))
                .build();
    }

    private static Set<AuthorityEntity> getOrCreateAuthorities(Set<String> authorities, AuthorityRepository authorityRepository) {
        return authorities.stream()
                .map(authority -> getOrCreateAuthority(authority, authorityRepository))
                .collect(Collectors.toSet());
    }


    private static AuthorityEntity getOrCreateAuthority(String authority, AuthorityRepository authorityRepository) {
        Supplier<AuthorityEntity> saved = () ->
                authorityRepository.save(AuthorityEntity.builder().name(authority).build());

        return authorityRepository
                .findByName(authority).orElseGet(saved);
    }

}
