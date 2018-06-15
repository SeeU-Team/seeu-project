package com.seeu.darkside.user;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserAdapter userAdapter;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserServiceImpl(final UserRepository userRepository, final UserAdapter userAdapter, final BCryptPasswordEncoder bCryptPasswordEncoder) {

        this.userRepository = userRepository;
        this.userAdapter = userAdapter;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
	@Transactional(readOnly = true)
    public List<UserDto> getAllUsers() {

        return userRepository.findAll()
            .stream()
            .map(userAdapter::entityToDto)
            .collect(toList());
    }

	@Override
	@Transactional(readOnly = true)
	public UserDto getUser(Long id) throws UserNotFoundException {
		UserEntity userEntity = userRepository
				.findById(id)
				.orElseThrow(UserNotFoundException::new);

		return userAdapter.entityToDto(userEntity);
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
	@Transactional(readOnly = true)
	public UserDto getUserByFacebookId(Long facebookId) throws UserNotFoundException {
		UserEntity userByEmail = userRepository.findOneByFacebookId(facebookId);
		if(userByEmail == null)
			throw new UserNotFoundException();
		return userAdapter.entityToDto(userByEmail);
	}

    @Override
    @Transactional
    public UserDto createUser(UserDto userDto) throws UserAlreadyExistsException {

        final UserEntity oneByEmail = userRepository.findOneByEmail(userDto.getEmail());
        if(oneByEmail != null)
            throw new UserAlreadyExistsException();

        final Date now = new Date();
        userDto.setCreated(now);
        userDto.setUpdated(now);
        userDto.setPassword(bCryptPasswordEncoder.encode(userDto.getPassword()));
        UserEntity userEntity = userAdapter.dtoToEntity(userDto);
        UserEntity userSaved = userRepository.save(userEntity);

        return userAdapter.entityToDto(userSaved);
    }

    @Override
	@Transactional
    public UserDto updateDescription(Long id, String description) {
        Date now = new Date();
        UserEntity userToUpdate = userRepository.getOne(id);
        userToUpdate.setDescription(description);
        userToUpdate.setUpdated(now);

        UserEntity save = userRepository.save(userToUpdate);

        return userAdapter.entityToDto(save);
    }

    @Override
	@Transactional
    public void deleteUser(Long id) throws UserNotFoundException {
        UserEntity userToDelete = userRepository
                .findById(id)
                .orElseThrow(UserNotFoundException::new);

        userRepository.delete(userToDelete);
    }
}
