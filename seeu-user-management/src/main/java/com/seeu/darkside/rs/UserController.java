package com.seeu.darkside.rs;

import com.seeu.darkside.facebook.FacebookRequestException;
import com.seeu.darkside.user.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.http.HttpStatus.NO_CONTENT;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/{id}")
    public UserDto getOneUser(@PathVariable("id") Long id) throws UserNotFoundException {
        return userService.getUser(id);
    }

    @GetMapping
    public List<UserDto> listUsers() {
        return userService.getAllUsers();
    }

    @GetMapping(params = "email")
    public UserDto getOneUserByEmail(@RequestParam(value = "email") String email ) throws UserNotFoundException {
        return userService.getUserByEmail(email);
    }

    @GetMapping(params = "facebookId")
    public UserDto getOneByFacebookId(@RequestParam(value = "facebookId") Long facebookId) throws UserNotFoundException {
        return userService.getUserByFacebookId(facebookId);
    }

    @GetMapping(value = "/facebookFriends", params = "access_token")
	public List<UserDto> getFacebookFriends(@RequestParam("access_token") String accessToken) throws FacebookRequestException {
		return userService.getFacebookFriends(accessToken);
	}

	@GetMapping(value = "/{id}/friends")
	public List<UserDto> getFriends(@PathVariable("id") Long id) {
		return userService.getFriends(id);
	}

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto createNewUser(@RequestBody UserDto userDto) throws UserAlreadyExistsException {
        return userService.createUser(userDto);
    }

    @PutMapping("{id}")
    public UserDto updateDescription(@PathVariable("id") Long id, @RequestBody String description) {
        return userService.updateDescription(id, description);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(NO_CONTENT)
    public void deleteUser(@PathVariable("id") Long id) throws UserNotFoundException {
        userService.deleteUser(id);
    }

}
