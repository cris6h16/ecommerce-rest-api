package org.cris6h16.user;

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
                .authority(userEntity.getAuthority().name())
                .balance(userEntity.getBalance())
                .build();
    }




    static UserEntity toUserEntity(CreateUserInput user) {
        return UserEntity.builder()
                .id(null)
                .firstname(user.getFirstname())
                .lastname(user.getLastname())
                .email(user.getEmail())
                .password(user.getPassword())
                .balance(user.getBalance())
                .enabled(user.isEnabled())
                .emailVerified(user.isEmailVerified())
                .authority(user.getAuthority())
                .build();
    }

}
