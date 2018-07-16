package com.seeu.darkside.user;

import com.amazonaws.services.s3.AmazonS3;
import com.seeu.darkside.facebook.FacebookRequestException;
import com.seeu.darkside.facebook.FacebookService;
import com.seeu.darkside.message.MessageServiceProxy;
import com.seeu.darkside.notification.MessagingRegistrationServiceProxy;
import com.seeu.darkside.team.TeamHasUser;
import com.seeu.darkside.team.TeamServiceProxy;
import com.seeu.darkside.team.TeammateStatus;
import com.seeu.darkside.utils.GenerateFileUrl;
import feign.FeignException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
	private final TeamServiceProxy teamServiceProxy;
	private final MessagingRegistrationServiceProxy messagingRegistrationServiceProxy;

	@Autowired
	public UserServiceImpl(final UserRepository userRepository,
						   final UserAdapter userAdapter,
						   final FacebookService facebookService,
						   final MessageServiceProxy messageServiceProxy,
						   final AmazonS3 amazonS3,
						   final TeamServiceProxy teamServiceProxy,
						   MessagingRegistrationServiceProxy messagingRegistrationServiceProxy) {

		this.userRepository = userRepository;
		this.userAdapter = userAdapter;
		this.facebookService = facebookService;
		this.messageServiceProxy = messageServiceProxy;
		this.amazonS3 = amazonS3;
		this.teamServiceProxy = teamServiceProxy;
		this.messagingRegistrationServiceProxy = messagingRegistrationServiceProxy;
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

		return facebookService.getFacebookUserFriends(accessToken)
				.stream()
				.map(facebookUser -> getUserByFacebookId(facebookUser.getId()))
				.filter(Objects::nonNull)
				// remove all users that already belong to a team
				.filter(userDto -> !alreadyBelongsToATeam(userDto))
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

		updateRegistrationTopics(userEntity);

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
	@Transactional
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

	@Override
	@Transactional
	public void updateAppInstanceId(Long id, String appInstanceId) {
		UserEntity userEntity = userRepository
				.findById(id)
				.orElseThrow(UserNotFoundException::new);

		userEntity.setAppInstanceId(appInstanceId);

		userEntity = userRepository.save(userEntity);

		updateRegistrationTopics(userEntity);
	}

	@Override
	public void deletePictureById(Long id) {
		UserEntity userEntity = userRepository
				.findById(id)
				.orElseThrow(UserNotFoundException::new);
		userEntity.setProfilePhotoUrl(null);
		userRepository.save(userEntity);
	}

	@Override
	public List<UserPicture> getAllUsersPictures() {
		List<UserEntity> userEntities = userRepository.findAll();
		List<UserPicture> userPictures = new ArrayList<>();
		for (UserEntity userEntity : userEntities) {
			String pictureKey = userEntity.getProfilePhotoUrl();
			URL url = GenerateFileUrl.generateUrlFromFile(amazonS3, BUCKET_SOURCE, pictureKey);

			if (null != url) {
				UserPicture userPicture = new UserPicture(userEntity.getId(), pictureKey, url.toExternalForm());
				userPictures.add(userPicture);
			}
		}
		return userPictures;
	}

	@Override
	public void deleteAllUsers() {
		userRepository.deleteAll();
	}

	private void updateRegistrationTopics(UserEntity userEntity) {
		String appInstanceId = userEntity.getAppInstanceId();

		if (null == appInstanceId) {
			return;
		}

		try {
			messagingRegistrationServiceProxy.registerUserTopic(appInstanceId, userEntity.getId());
			TeamHasUser teamHasUser = teamServiceProxy.getTeamOfMember(userEntity.getId());
			messagingRegistrationServiceProxy.registerTeamTopic(Collections.singletonList(appInstanceId), teamHasUser.getTeam().getId());

			if (TeammateStatus.LEADER.equals(teamHasUser.getStatus())) {
				messagingRegistrationServiceProxy.registerLeaderTopic(appInstanceId, teamHasUser.getTeam().getId());
			}
		} catch (FeignException e) {
			e.printStackTrace();
		}
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

	/**
	 * Determines if the user belongs to a team or not.
	 *
	 * @param userDto the user
	 * @return true if the user already belongs to a team. Otherwise, return false
	 */
	private boolean alreadyBelongsToATeam(UserDto userDto) {
		try {
			teamServiceProxy.getTeamOfMember(userDto.getId());
			return true;
		} catch (FeignException e) {
			if (HttpStatus.NOT_FOUND.value() == e.status()) {
				return false;
			} else {
				e.printStackTrace();
			}
		}

		return false;
	}
}

