package com.seeu.darkside.user;

import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface UserService {

    List<UserDto> getAllUsers();

    UserDto createUser(UserDto userDto) throws UserAlreadyExistsException;

    UserDto getUser(Long id) throws UserNotFoundException;

    UserDto updateDescription(Long id, String description);

    UserDto getUserByEmail(String email) throws UserNotFoundException;

    void deleteUser(Long id) throws UserNotFoundException;
}
