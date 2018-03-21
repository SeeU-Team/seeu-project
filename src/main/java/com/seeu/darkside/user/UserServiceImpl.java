package com.seeu.darkside.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserAdapter userAdapter;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, UserAdapter userAdapter, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.userAdapter = userAdapter;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public List<UserDto> getAllUsers() {
        List<UserDto> userDtoList = userRepository.findAll()
                .stream()
                .map((user) -> {
                    UserDto userDto = userAdapter.entityToDto(user);
                    return userDto;
                })
                .collect(Collectors.toList());

        return userDtoList;
    }

    @Override
    public UserDto createUser(UserDto userDto) throws UserAlreadyExistsException {

        UserEntity oneByEmail = userRepository.findOneByEmail(userDto.getEmail());
        if(oneByEmail != null)
            throw new UserAlreadyExistsException();

        Date now = new Date();
        userDto.setCreated(now);
        userDto.setUpdated(now);
        userDto.setPassword(bCryptPasswordEncoder.encode(userDto.getPassword()));
        UserEntity userEntity = userAdapter.dtoToEntity(userDto);
        UserEntity userSaved = userRepository.save(userEntity);

        return userAdapter.entityToDto(userSaved);
    }

    @Override
    @Transactional(readOnly = true)
    public UserDto getUser(Long id) throws UserNotFoundException {
        UserEntity user = userRepository.findOne(id);
        if(user == null)
            throw new UserNotFoundException();
        return userAdapter.entityToDto(user);
    }

    @Override
    public UserDto updateDescription(Long id, String description) {
        Date now = new Date();
        UserEntity userToUpdate = userRepository.findOne(id);
        userToUpdate.setDescription(description);
        userToUpdate.setUpdated(now);
        UserEntity save = userRepository.save(userToUpdate);
        return userAdapter.entityToDto(save);
    }

    @Override
    @Transactional(readOnly = true)
    public UserDto getUserByEmail(String email) throws UserNotFoundException {
        UserEntity userByEmail = userRepository.findOneByEmail(email);
        if(userByEmail == null)
            throw new UserNotFoundException();
        return userAdapter.entityToDto(userByEmail);
    }

    @Override
    public void deleteUser(Long id) throws UserNotFoundException {
        UserEntity userToDelete = userRepository.findOne(id);
        if(userToDelete == null)
            throw new UserNotFoundException();
        userRepository.delete(userToDelete);
    }
}
