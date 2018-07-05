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
                .name("first")
                .email("email")
                .description("description")
                .profilePhotoUrl("url/picture.png")
                .created(date)
                .updated(date)
                .build();

        UserEntity userEntity = userAdapter.dtoToEntity(userDto);

        assertThat(userEntity.getId()).isEqualTo(userDto.getId());
        assertThat(userEntity.getName()).isEqualTo(userDto.getName());
        assertThat(userEntity.getEmail()).isEqualTo(userDto.getEmail());
        assertThat(userEntity.getDescription()).isEqualTo(userDto.getDescription());
        assertThat(userEntity.getProfilePhotoUrl()).isEqualTo(userDto.getProfilePhotoUrl());
        assertThat(userEntity.getCreated()).isEqualTo(userDto.getCreated());
        assertThat(userEntity.getUpdated()).isEqualTo(userDto.getUpdated());
    }

    @Test
    public void should_return_an_user_dto() {
        Date date = new Date();
        UserEntity userEntity = UserEntity.builder()
                .id(1L)
                .name("first")
                .email("email")
                .profilePhotoUrl("url/picture.png")
                .created(date)
                .updated(date)
                .build();

        UserDto userDto = userAdapter.entityToDto(userEntity);

        assertThat(userDto.getId()).isEqualTo(userEntity.getId());
        assertThat(userDto.getName()).isEqualTo(userEntity.getName());
        assertThat(userDto.getEmail()).isEqualTo(userEntity.getEmail());
        assertThat(userDto.getDescription()).isEqualTo(userEntity.getDescription());
        assertThat(userDto.getProfilePhotoUrl()).isEqualTo(userEntity.getProfilePhotoUrl());
        assertThat(userDto.getCreated()).isEqualTo(userEntity.getCreated());
        assertThat(userDto.getUpdated()).isEqualTo(userEntity.getUpdated());
    }

}
