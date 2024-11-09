package org.cris6h16.user;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

class EntityMapper {
    static UserDTO toUserDTO(UserEntity userEntity) {
        if (userEntity == null) return null;
        return UserDTO.builder()
                .id(userEntity.getId())
                .firstname(userEntity.getFirstname())
                .lastname(userEntity.getLastname())
                .email(userEntity.getEmail())
                .password(userEntity.getPassword())
                .enabled(userEntity.isEnabled())
                .emailVerified(userEntity.isEmailVerified())
                .authorities(toSetOfString(userEntity.getAuthorities()))
                .build();
    }
    private static Set<String> toSetOfString(Set<AuthorityEntity> authorities) {
        return authorities.stream()
                .map(AuthorityEntity::getName)
                .collect(Collectors.toSet());
    }



    static UserEntity toUserEntity(SignupDTO user) {
        return UserEntity.builder()
                .id(null)
                .firstname(user.getFirstname())
                .lastname(user.getLastname())
                .email(user.getEmail())
                .password(user.getPassword())
                .balance(BigDecimal.valueOf(0))
                .enabled(true)
                .emailVerified(false)
                .authorities(getOrCreateAuthority(SignupDTO.DEF_AUTHORITY))
                .build();
    }



}
