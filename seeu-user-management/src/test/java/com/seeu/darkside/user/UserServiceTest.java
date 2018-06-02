package com.seeu.darkside.user;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.fail;
import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

    @InjectMocks
    UserServiceImpl userService;

    @Mock
    UserRepository userRepository;

    @Mock
    UserAdapter userAdapter;

    @Mock
    BCryptPasswordEncoder bCryptPasswordEncoder;

    private UserEntity user1;

    private UserEntity user2;

    private UserEntity user3;

    private UserEntity user4;

    private UserDto userDto1;

    private UserDto userDto2;

    private UserDto userDto3;

    private UserDto newUserDto4;

    private Date date;

    public void initUsers() {

        date = new Date();

        user1 = UserEntity.builder()
                .idUser(1L)
                .firstname("first")
                .lastname("lastname1")
                .email("first@email.com")
                .password("password1")
                .description("description1")
                .profilePhotoUrl("url/picture1.png")
                .created(date)
                .updated(date)
                .build();

        user2 = UserEntity.builder()
                .idUser(2L)
                .firstname("second")
                .lastname("lastname2")
                .email("second@email.com")
                .password("password2")
                .description("description2")
                .profilePhotoUrl("url/picture2.png")
                .created(date)
                .updated(date)
                .build();

        user3 = UserEntity.builder()
                .idUser(3L)
                .firstname("third")
                .lastname("lastname1")
                .email("tird@email.com")
                .password("password3")
                .description("description3")
                .profilePhotoUrl("url/picture3.png")
                .created(date)
                .updated(date)
                .build();

        user4 = UserEntity.builder()
                .idUser(4L)
                .firstname("fourth")
                .lastname("lastname4")
                .email("fourth@email.com")
                .password("password4")
                .description("description4")
                .profilePhotoUrl("url/picture4.png")
                .created(date)
                .updated(date)
                .build();

        userDto1 = UserDto.builder()
                .idUser(1L)
                .firstname("first")
                .lastname("lastname1")
                .email("first@email.com")
                .password("password1")
                .description("description1")
                .profilePhotoUrl("url/picture1.png")
                .created(date)
                .updated(date)
                .build();

        userDto2 = UserDto.builder()
                .idUser(2L)
                .firstname("second")
                .lastname("lastname2")
                .email("second@email.com")
                .password("password2")
                .description("description2")
                .profilePhotoUrl("url/picture2.png")
                .created(date)
                .updated(date)
                .build();

        userDto3 = UserDto.builder()
                .idUser(3L)
                .firstname("third")
                .lastname("lastname1")
                .email("tird@email.com")
                .password("password3")
                .description("description3")
                .profilePhotoUrl("url/picture3.png")
                .created(date)
                .updated(date)
                .build();

        newUserDto4 = UserDto.builder()
                .idUser(4L)
                .firstname("fourth")
                .lastname("lastname4")
                .email("fourth@email.com")
                .password("passwordEncoded")
                .description("description ")
                .profilePhotoUrl("url/picture4.png")
                .created(date)
                .updated(date)
                .build();
    }

    @Before
    public void configureMock() {
        initUsers();
        ArrayList<UserEntity> list = new ArrayList<>();
        list.add(user1);
        list.add(user2);
        list.add(user3);

        when(userAdapter.entityToDto(user1)).thenReturn(userDto1);
        when(userAdapter.entityToDto(user2)).thenReturn(userDto2);
        when(userAdapter.entityToDto(user3)).thenReturn(userDto3);
        when(userAdapter.dtoToEntity(any(UserDto.class))).thenReturn(user4);
        when(userAdapter.entityToDto(user4)).thenReturn(newUserDto4);
        when(userRepository.findAll()).thenReturn(list);
        when(userRepository.findOneByEmail(user3.getEmail())).thenReturn(user3);
        when(userRepository.findOneByEmail(user4.getEmail())).thenReturn(null);
        when(bCryptPasswordEncoder.encode(user4.getPassword())).thenReturn("passwordEncoded");
        when(userRepository.findById(user2.getIdUser())).thenReturn(Optional.of(user2));
        // when(userRepository.getOne(4L)).thenReturn(null);
        when(userRepository.save(any(UserEntity.class))).thenReturn(user4);
    }

    @Test
    public void should_get_all_users() {
        List<UserDto> allUsers = userService.getAllUsers();
        assertThat(allUsers).hasSize(3);
    }

    @Test
    public void should_get_one_user_from_id() throws UserNotFoundException {
        Long idUser = 2L;
        try {
            UserDto userByEmail = userService.getUser(idUser);
            assertThat(userByEmail).isNotNull();
            assertThat(userByEmail.getIdUser()).isEqualTo(user2.getIdUser());
            assertThat(userByEmail.getFirstname()).isEqualTo(user2.getFirstname());
        } catch (UserNotFoundException e) {
            fail("Test failed : an unexpected exception has been thrown when trying to retrieve one user with id = " + idUser);
        }
    }

    @Test
    public void should_throws_UserNotFoundException_with_unknow_user_id() {
        Long unknowId = 526L;
        try {
            userService.getUser(unknowId);
            fail("Test failed : an exception should have been thrown when trying to retrieve one user with id = " + unknowId);
        } catch (UserNotFoundException e) {
        }
    }

    @Test
    public void should_get_one_user_from_email() throws UserNotFoundException {
        String email = "tird@email.com";
        try {
            UserDto userByEmail = userService.getUserByEmail(email);
            assertThat(userByEmail).isNotNull();
            assertThat(userByEmail.getIdUser()).isEqualTo(user3.getIdUser());
            assertThat(userByEmail.getFirstname()).isEqualTo(user3.getFirstname());
        } catch (UserNotFoundException e) {
            fail("Test failed : an exception should have been thrown when trying to retrieve one user with email = " + email);
        }
    }

    @Test
    public void should_throws_UserNotFoundException_with_unknow_email() {
        String email = "unknow@mail.com";
        try {
            userService.getUserByEmail(email);
            fail("Test failed : an exception should have been thrown when trying to retrieve one user with email = " + email);
        } catch (UserNotFoundException e) {
        }
    }

    @Test
    public void should_create_new_user() throws UserAlreadyExistsException {
        UserDto userDto4 = UserDto.builder()
                .idUser(4L)
                .firstname("fourth")
                .lastname("lastname4")
                .email("fourth@email.com")
                .password("password4")
                .description("description4")
                .profilePhotoUrl("url/picture4.png")
                .build();

        UserDto userSaved = userService.createUser(userDto4);
        assertThat(userSaved.getIdUser()).isEqualTo(userDto4.getIdUser());
    }

    @Test
    public void should_throws_UserAlreadyExistsException_when_create_new_user() {
        try {
            userService.createUser(userDto3);
            fail("Test failed : an exception should have been thrown when trying to create a new user with an existing email = " + userDto3.getEmail());
        } catch (UserAlreadyExistsException e) {
        }
    }

    @Test
    @Ignore
    public void should_update_user_description() {
        String newDescription = "My new description";

        UserDto userWithUpdatedDescription = userService.updateDescription(1L, newDescription);
        assertThat(userWithUpdatedDescription).isNotNull();
    }
}
