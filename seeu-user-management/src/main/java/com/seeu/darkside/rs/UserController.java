package com.seeu.darkside.rs;

import com.seeu.darkside.user.UserAlreadyExistsException;
import com.seeu.darkside.user.UserDto;
import com.seeu.darkside.user.UserNotFoundException;
import com.seeu.darkside.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.List;

import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("{id}")
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
    public UserDto getOneByFacebookId(@RequestParam(value = "facebookId") Long id) throws UserNotFoundException {
        return UserDto.builder()
                .idUser(id)
                .build();
        //throw new NotImplementedException();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto createNewUser(@RequestBody UserDto userDto) throws UserAlreadyExistsException {
        return userService.createUser(userDto);
    }

    @PatchMapping("{id}")
    public UserDto updateDescription(@PathVariable("id") Long id, @RequestBody String description) {
        return userService.updateDescription(id, description);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(NO_CONTENT)
    public void deleteUser(@PathVariable("id") Long id) throws UserNotFoundException {
        userService.deleteUser(id);
    }

}
