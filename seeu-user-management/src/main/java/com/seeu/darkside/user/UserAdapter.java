package com.seeu.darkside.user;

import org.springframework.stereotype.Component;

@Component
public class UserAdapter {

    public UserEntity dtoToEntity(UserDto userDto) {
        return UserEntity.builder()
                .id(userDto.getId())
                .facebookId(userDto.getFacebookId())
                .appInstanceId(userDto.getAppInstanceId())
                .name(userDto.getName())
                .gender(userDto.getGender())
                .email(userDto.getEmail())
                .description(userDto.getDescription())
                .profilePhotoUrl(userDto.getProfilePhotoUrl())
                .created(userDto.getCreated())
                .updated(userDto.getUpdated())
                .build();
    }

    public UserDto entityToDto(UserEntity userEntity) {
        return UserDto.builder()
                .id(userEntity.getId())
                .facebookId(userEntity.getFacebookId())
                .appInstanceId(userEntity.getAppInstanceId())
                .name(userEntity.getName())
                .gender(userEntity.getGender())
                .email(userEntity.getEmail())
                .description(userEntity.getDescription())
                .profilePhotoUrl(userEntity.getProfilePhotoUrl())
                .created(userEntity.getCreated())
                .updated(userEntity.getUpdated())
                .build();
    }
}