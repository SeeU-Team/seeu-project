package com.seeu.darkside.user;

import org.springframework.stereotype.Component;

@Component
public class UserAdapter {

    public UserEntity dtoToEntity(UserDto userDto) {
        return UserEntity.builder()
                .idUser(userDto.getIdUser())
                .firstname(userDto.getFirstname())
                .lastname(userDto.getLastname())
                .email(userDto.getEmail())
                .description(userDto.getDescription())
                .password(userDto.getPassword())
                .profilePhotoUrl(userDto.getProfilePhotoUrl())
                .created(userDto.getCreated())
                .updated(userDto.getUpdated())
                .build();
    }

    public UserDto entityToDto(UserEntity userEntity) {
        return UserDto.builder()
                .idUser(userEntity.getIdUser())
                .firstname(userEntity.getFirstname())
                .lastname(userEntity.getLastname())
                .email(userEntity.getEmail())
                .description(userEntity.getDescription())
                .password(userEntity.getPassword())
                .profilePhotoUrl(userEntity.getProfilePhotoUrl())
                .created(userEntity.getCreated())
                .updated(userEntity.getUpdated())
                .build();
    }
}