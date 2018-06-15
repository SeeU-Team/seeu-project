package com.seeu.darkside.user;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

import static org.assertj.core.api.Java6Assertions.assertThat;

@RunWith(SpringRunner.class)
public class UserAdapterTest {

    @InjectMocks
    UserAdapter userAdapter;

    @Test
    public void should_return_an_user_entity() {
        Date date = new Date();
        UserDto userDto = UserDto.builder()
                .id(1L)
                .firstname("first")
                .lastname("last")
                .email("email")
                .password("password")
                .description("description")
                .profilePhotoUrl("url/picture.png")
                .created(date)
                .updated(date)
                .build();

        UserEntity userEntity = userAdapter.dtoToEntity(userDto);

        assertThat(userEntity.getId()).isEqualTo(userDto.getId());
        assertThat(userEntity.getFirstname()).isEqualTo(userDto.getFirstname());
        assertThat(userEntity.getLastname()).isEqualTo(userDto.getLastname());
        assertThat(userEntity.getEmail()).isEqualTo(userDto.getEmail());
        assertThat(userEntity.getDescription()).isEqualTo(userDto.getDescription());
        assertThat(userEntity.getPassword()).isEqualTo(userDto.getPassword());
        assertThat(userEntity.getProfilePhotoUrl()).isEqualTo(userDto.getProfilePhotoUrl());
        assertThat(userEntity.getCreated()).isEqualTo(userDto.getCreated());
        assertThat(userEntity.getUpdated()).isEqualTo(userDto.getUpdated());
    }

    @Test
    public void should_return_an_user_dto() {
        Date date = new Date();
        UserEntity userEntity = UserEntity.builder()
                .id(1L)
                .firstname("first")
                .lastname("last")
                .email("email")
                .profilePhotoUrl("url/picture.png")
                .password("password")
                .created(date)
                .updated(date)
                .build();

        UserDto userDto = userAdapter.entityToDto(userEntity);

        assertThat(userDto.getId()).isEqualTo(userEntity.getId());
        assertThat(userDto.getFirstname()).isEqualTo(userEntity.getFirstname());
        assertThat(userDto.getLastname()).isEqualTo(userEntity.getLastname());
        assertThat(userDto.getEmail()).isEqualTo(userEntity.getEmail());
        assertThat(userDto.getDescription()).isEqualTo(userEntity.getDescription());
        assertThat(userDto.getPassword()).isEqualTo(userEntity.getPassword());
        assertThat(userDto.getProfilePhotoUrl()).isEqualTo(userEntity.getProfilePhotoUrl());
        assertThat(userDto.getCreated()).isEqualTo(userEntity.getCreated());
        assertThat(userDto.getUpdated()).isEqualTo(userEntity.getUpdated());
    }

}
