package com.seeu.darkside.message;

import com.seeu.darkside.team.Team;
import com.seeu.darkside.team.TeamServiceProxy;
import com.seeu.darkside.user.User;
import com.seeu.darkside.user.UserServiceProxy;
import feign.FeignException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static com.seeu.darkside.message.ConversationType.*;

@Service
public class MessageServiceImpl implements MessageService {

	private final MessageRepository messageRepository;
	private final MessageAdapter messageAdapter;
	private final UserServiceProxy userServiceProxy;
	private final TeamServiceProxy teamServiceProxy;

	@Autowired
	public MessageServiceImpl(MessageRepository messageRepository, MessageAdapter messageAdapter, UserServiceProxy userServiceProxy, TeamServiceProxy teamServiceProxy) {
		this.messageRepository = messageRepository;
		this.messageAdapter = messageAdapter;
		this.userServiceProxy = userServiceProxy;
		this.teamServiceProxy = teamServiceProxy;
	}

	@Override
	@Transactional(readOnly = true)
	public List<CompleteMessageDto> getAllMessagesForUserToUser(Long firstUserId, Long secondUserId) {
		return messageRepository.getAllBySwitchableFromAndDestAndType(firstUserId, secondUserId, USER_TO_USER)
				.stream()
				.map(this::getCompleteUserMessageDto)
				.collect(Collectors.toList());
	}

	@Override
	@Transactional(readOnly = true)
	public List<CompleteMessageDto> getAllMessagesForTeam(Long teamId) {
		return messageRepository.getAllByDestAndTypeOrderByCreatedAsc(teamId, USER_TO_TEAM)
				.stream()
				.map(this::getCompleteUserMessageDto)
				.collect(Collectors.toList());
	}

	@Override
	@Transactional(readOnly = true)
	public List<CompleteMessageDto> getAllMessagesForTeamToBefore(Long firstTeamId, Long secondTeamId) {
		return messageRepository.getAllBySwitchableFromAndDestAndType(firstTeamId, secondTeamId, TEAM_TO_BEFORE)
				.stream()
				.map(this::getCompleteTeamMessageDto)
				.collect(Collectors.toList());
	}

	@Override
	@Transactional(readOnly = true)
	public List<Long> getFriendsOf(Long userId) {
		return messageRepository.getDistinctMessagesWith(userId)
				.stream()
				.map(entity -> userId.equals(entity.getFrom()) ? entity.getDest() : entity.getFrom())
				.distinct()
				.collect(Collectors.toList());
	}

	@Override
	@Transactional
	public CompleteMessageDto createMessage(MessageDto messageDto) {
		MessageEntity entity = messageAdapter.dtoToEntity(messageDto);
		Date date = new Date();
		entity.setCreated(date);
		entity.setUpdated(date);

		entity = messageRepository.save(entity);

		return ConversationType.TEAM_TO_BEFORE.equals(entity.getType())
				? getCompleteTeamMessageDto(entity)
				: getCompleteUserMessageDto(entity);
	}

	private CompleteMessageDto<User> getCompleteUserMessageDto(MessageEntity entity) {
		User owner;

		try {
			owner = userServiceProxy.getOneUser(entity.getFrom());
		} catch (FeignException e) {
			Logger logger = Logger.getLogger(this.getClass().getName());
			logger.severe("Error while retrieving user information (userId=" + entity.getFrom() + ")");
			e.printStackTrace();

			owner = User.builder()
					.id(entity.getFrom())
					.build();
		}

		return CompleteMessageDto.<User>builder()
				.id(entity.getId())
				.content(entity.getContent())
				.created(entity.getCreated())
				.updated(entity.getUpdated())
				.owner(owner)
				.build();
	}

	private CompleteMessageDto<Team> getCompleteTeamMessageDto(MessageEntity entity) {
		Team owner;

		try {
			owner = teamServiceProxy.getTeamInfo(entity.getFrom());
		} catch (FeignException e) {
			Logger logger = Logger.getLogger(this.getClass().getName());
			logger.severe("Error while retrieving team information (teamId=" + entity.getFrom() + ")");
			e.printStackTrace();

			owner = Team.builder()
					.id(entity.getFrom())
					.build();
		}

		return CompleteMessageDto.<Team>builder()
				.id(entity.getId())
				.content(entity.getContent())
				.created(entity.getCreated())
				.updated(entity.getUpdated())
				.owner(owner)
				.build();
	}
}
