package com.seeu.darkside.user;

import com.amazonaws.services.s3.AmazonS3;
import com.seeu.darkside.facebook.FacebookRequestException;
import com.seeu.darkside.facebook.FacebookService;
import com.seeu.darkside.facebook.FacebookUser;
import com.seeu.darkside.message.MessageServiceProxy;
import com.seeu.darkside.utils.GenerateFileUrl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.*;

import static java.util.stream.Collectors.toList;
import static org.apache.commons.codec.binary.Base64.decodeBase64;

@Service
public class UserServiceImpl implements UserService {

	private static final String BUCKET_SOURCE = "seeu-bucket";
	private static final String EXT_PNG = ".png";

	private final UserRepository userRepository;
	private final UserAdapter userAdapter;
	private final FacebookService facebookService;
	private final MessageServiceProxy messageServiceProxy;
	private final AmazonS3 amazonS3;

	public UserServiceImpl(final UserRepository userRepository,
						   final UserAdapter userAdapter,
						   final FacebookService facebookService, MessageServiceProxy messageServiceProxy, AmazonS3 amazonS3) {

		this.userRepository = userRepository;
		this.userAdapter = userAdapter;
		this.facebookService = facebookService;
		this.messageServiceProxy = messageServiceProxy;
		this.amazonS3 = amazonS3;
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

		return getCompleteUserDto(userEntity);
	}

	@Override
	@Transactional(readOnly = true)
	public UserDto getUserByEmail(String email) {
		UserEntity userEntity = userRepository
				.findOneByEmail(email)
				.orElseThrow(UserNotFoundException::new);

		return getCompleteUserDto(userEntity);
	}

	@Override
	@Transactional(readOnly = true)
	public UserDto getUserByFacebookId(Long facebookId) {
		UserEntity userEntity = userRepository
				.findOneByFacebookId(facebookId)
				.orElseThrow(UserNotFoundException::new);

		return getCompleteUserDto(userEntity);
	}

	@Override
	@Transactional(readOnly = true)
	public List<UserDto> getFacebookFriends(String accessToken) throws FacebookRequestException {
		List<FacebookUser> facebookUserFriends = facebookService.getFacebookUserFriends(accessToken);

		return facebookUserFriends
				.stream()
				.map(facebookUser -> getUserByFacebookId(facebookUser.getId()))
				.filter(Objects::nonNull)
				.collect(toList());
	}

	@Override
	@Transactional(readOnly = true)
	public List<UserDto> getFriends(Long id) {
		// If already one message has been sent between this user and another user, it is a friend
		return messageServiceProxy.getFriendsOfUser(id)
				.stream()
				.map(this::getUser)
				.filter(Objects::nonNull)
				.collect(toList());
	}

	@Override
	@Transactional
	public UserDto createUser(UserDto userDto) {
		final Optional<UserEntity> optionalUser = userRepository.findOneByFacebookId(userDto.getFacebookId());
		if (optionalUser.isPresent())
			throw new UserAlreadyExistsException();

		// When we create a user, the profilePhotoUrl contains the user's facebook profile photo url
		String fileNameToSave = saveFacebookProfilePhotoInAmazonS3(userDto.getProfilePhotoUrl());

		final Date now = new Date();
		userDto.setProfilePhotoUrl(fileNameToSave);
		userDto.setCreated(now);
		userDto.setUpdated(now);
		UserEntity userEntity = userAdapter.dtoToEntity(userDto);
		userEntity = userRepository.save(userEntity);

		return getCompleteUserDto(userEntity);
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

	@Override
	public void update(UserDto userDto, String profilePicture) {
		UserEntity userEntity = userRepository
				.findById(userDto.getId())
				.orElseThrow(UserNotFoundException::new);

		String fileName = userEntity.getProfilePhotoUrl();

		if (null != profilePicture) {
			fileName = savePngInAmazonS3(profilePicture);
		}

		Date newUpdatedDate = new Date();

		userEntity.setName(userDto.getName());
		userEntity.setGender(userDto.getGender());
		userEntity.setEmail(userDto.getEmail());
		userEntity.setDescription(userDto.getDescription());
		userEntity.setProfilePhotoUrl(fileName);
		userEntity.setUpdated(newUpdatedDate);

		userRepository.save(userEntity);
	}

	private String savePngInAmazonS3(String profilePhotoBase64) {
		String fileNameToSave = UUID.randomUUID().getLeastSignificantBits() + EXT_PNG;
		byte[] bytes = decodeBase64(profilePhotoBase64);
		InputStream inputStream = new ByteArrayInputStream(bytes);
		amazonS3.putObject(BUCKET_SOURCE, fileNameToSave, inputStream, null);

		return fileNameToSave;
	}

	private String saveFacebookProfilePhotoInAmazonS3(String url) {
		String fileNameToSave;

		try {
			fileNameToSave = UUID.randomUUID().getLeastSignificantBits() + EXT_PNG;
			InputStream inputStream = new URL(url).openStream();
			amazonS3.putObject(BUCKET_SOURCE, fileNameToSave, inputStream, null);
		} catch (IOException e) {
			e.printStackTrace();
			fileNameToSave = null;
		}

		return fileNameToSave;
	}

	private UserDto getCompleteUserDto(UserEntity userEntity) {
		URL url = GenerateFileUrl.generateUrlFromFile(amazonS3, BUCKET_SOURCE, userEntity.getProfilePhotoUrl());

		UserDto userDto = userAdapter.entityToDto(userEntity);
		userDto.setProfilePhotoUrl(url.toExternalForm());

		return userDto;
	}
}

