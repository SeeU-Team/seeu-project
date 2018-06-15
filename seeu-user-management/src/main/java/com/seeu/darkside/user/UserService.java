package com.seeu.darkside.user;

import java.util.List;

public interface UserService {

    List<UserDto> getAllUsers();

	UserDto getUser(Long id) throws UserNotFoundException;
	UserDto getUserByEmail(String email) throws UserNotFoundException;
	UserDto getUserByFacebookId(Long facebookId) throws UserNotFoundException;

    UserDto createUser(UserDto userDto) throws UserAlreadyExistsException;

    UserDto updateDescription(Long id, String description);

    void deleteUser(Long id) throws UserNotFoundException;
}
