package com.seeu.darkside.user;

import java.util.List;

public interface UserService {

    List<UserDto> getAllUsers();

	UserDto getUser(Long id);
	UserDto getUserByEmail(String email);
	UserDto getUserByFacebookId(Long facebookId);
	List<UserDto> getFacebookFriends(String accessToken);
	List<UserDto> getFriends(Long id);

    UserDto createUser(UserDto userDto);

    UserDto updateDescription(Long id, String description);

    void deleteUser(Long id);

	void update(UserDto userDto, String profilePicture);
}
