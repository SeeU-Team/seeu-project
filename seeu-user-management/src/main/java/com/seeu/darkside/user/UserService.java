package com.seeu.darkside.user;

import com.seeu.darkside.facebook.FacebookRequestException;

import java.util.List;

public interface UserService {

    List<UserDto> getAllUsers();

	UserDto getUser(Long id) throws UserNotFoundException;
	UserDto getUserByEmail(String email) throws UserNotFoundException;
	UserDto getUserByFacebookId(Long facebookId) throws UserNotFoundException;
	List<UserDto> getFacebookFriends(String accessToken) throws FacebookRequestException;
	List<UserDto> getFriends(Long id);

    UserDto createUser(UserDto userDto) throws UserAlreadyExistsException;

    UserDto updateDescription(Long id, String description);

    void deleteUser(Long id) throws UserNotFoundException;
}
