package com.seeu.darkside.rs;

import com.seeu.darkside.team.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/teams")
public class TeamController {

    @Autowired
    private TeamService teamService;

    /**
    @GetMapping(value = "/{id}", produces = APPLICATION_JSON_VALUE)
    public UserDto getOneUser(@PathVariable("id") Long id) throws UserNotFoundException {
        return userService.getUser(id);
    }

    @GetMapping(produces = APPLICATION_JSON_VALUE)
    public List<UserDto> listUsers() {
        return userService.getAllUsers();
    }

    @GetMapping(value = "/", produces = APPLICATION_JSON_VALUE)
    public UserDto getOneUserByEmail(@RequestParam(value="email") String email ) throws UserNotFoundException {
        return userService.getUserByEmail(email);
    }

    @PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto createNewUser(@RequestBody UserDto userDto) throws UserAlreadyExistsException {
        return userService.createUser(userDto);
    }

    @PatchMapping(value = "/{id}", produces = APPLICATION_JSON_VALUE)
    public UserDto updateDescription(@PathVariable("id") Long id, @RequestBody String description) {
        return userService.updateDescription(id, description);
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(NO_CONTENT)
    public void deleteUser(@PathVariable("id") Long id) throws UserNotFoundException {
        userService.deleteUser(id);
    }
    **/
}
