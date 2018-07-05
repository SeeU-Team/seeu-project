package com.seeu.darkside.user;

import com.seeu.darkside.facebook.FacebookRequestException;
import com.seeu.darkside.facebook.FacebookService;
import com.seeu.darkside.facebook.FacebookUser;
import com.seeu.darkside.message.MessageServiceProxy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserAdapter userAdapter;
    private final FacebookService facebookService;
    private final MessageServiceProxy messageServiceProxy;

    public UserServiceImpl(final UserRepository userRepository,
						   final UserAdapter userAdapter,
						   final FacebookService facebookService, MessageServiceProxy messageServiceProxy) {

        this.userRepository = userRepository;
        this.userAdapter = userAdapter;
        this.facebookService = facebookService;
		this.messageServiceProxy = messageServiceProxy;
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
	public UserDto getUser(Long id) {
		UserEntity userEntity = userRepository
				.findById(id)
				.orElseThrow(UserNotFoundException::new);

		return userAdapter.entityToDto(userEntity);
	}

	@Override
	@Transactional(readOnly = true)
	public UserDto getUserByEmail(String email) {
		UserEntity userByEmail = userRepository
				.findOneByEmail(email)
				.orElseThrow(UserNotFoundException::new);

		return userAdapter.entityToDto(userByEmail);
	}

	@Override
	@Transactional(readOnly = true)
	public UserDto getUserByFacebookId(Long facebookId) {
		UserEntity userByEmail = userRepository
				.findOneByFacebookId(facebookId)
				.orElseThrow(UserNotFoundException::new);

		return userAdapter.entityToDto(userByEmail);
	}

	@Override
	@Transactional(readOnly = true)
	public List<UserDto> getFacebookFriends(String accessToken) throws FacebookRequestException {
		List<FacebookUser> facebookUserFriends = facebookService.getFacebookUserFriends(accessToken);

		return facebookUserFriends
				.stream()
				.map(facebookUser -> {
					try {
						return getUserByFacebookId(facebookUser.getId());
					} catch (UserNotFoundException e) {
						return null;
					}
				})
				.filter(Objects::nonNull)
				.collect(toList());
	}

	@Override
	@Transactional(readOnly = true)
	public List<UserDto> getFriends(Long id) {
		// If already one message has been sent between this user and another user, it is a friend
		return messageServiceProxy.getFriendsOfUser(id)
				.stream()
				.map(userId -> {
					try {
						return this.getUser(userId);
					} catch (UserNotFoundException e) {
						return null;
					}
				})
				.filter(Objects::nonNull)
				.collect(toList());
	}

	@Override
    @Transactional
    public UserDto createUser(UserDto userDto) {

        final Optional<UserEntity> optionalUser = userRepository.findOneByFacebookId(userDto.getFacebookId());
        if(optionalUser.isPresent())
            throw new UserAlreadyExistsException();

        final Date now = new Date();
        userDto.setCreated(now);
        userDto.setUpdated(now);
        UserEntity userEntity = userAdapter.dtoToEntity(userDto);
        userEntity = userRepository.save(userEntity);

        return userAdapter.entityToDto(userEntity);
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
    public void deleteUser(Long id) {
        UserEntity userToDelete = userRepository
                .findById(id)
                .orElseThrow(UserNotFoundException::new);

        userRepository.delete(userToDelete);
    }
}
