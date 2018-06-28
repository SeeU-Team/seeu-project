package com.seeu.darkside.message;

import com.seeu.darkside.user.User;
import com.seeu.darkside.user.UserServiceProxy;
import feign.FeignException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static com.seeu.darkside.message.ConversationType.*;

@Service
public class MessageServiceImpl implements MessageService {

	private final MessageRepository messageRepository;
	private final MessageAdapter messageAdapter;
	private final UserServiceProxy userServiceProxy;

	@Autowired
	public MessageServiceImpl(MessageRepository messageRepository, MessageAdapter messageAdapter, UserServiceProxy userServiceProxy) {
		this.messageRepository = messageRepository;
		this.messageAdapter = messageAdapter;
		this.userServiceProxy = userServiceProxy;
	}

	@Override
	@Transactional(readOnly = true)
	public List<CompleteMessageDto> getAllMessagesForUserToUser(Long firstUserId, Long secondUserId) {
		return messageRepository.getAllBySwitchableFromAndDestAndType(firstUserId, secondUserId, USER_TO_USER)
				.stream()
				.map(this::getCompleteMessageDto)
				.collect(Collectors.toList());
	}

	@Override
	@Transactional(readOnly = true)
	public List<CompleteMessageDto> getAllMessagesForTeam(Long teamId) {
		return messageRepository.getAllByDestAndType(teamId, USER_TO_TEAM)
				.stream()
				.map(this::getCompleteMessageDto)
				.collect(Collectors.toList());
	}

	@Override
	@Transactional(readOnly = true)
	public List<CompleteMessageDto> getAllMessagesForTeamToBefore(Long firstTeamId, Long secondTeamId) {
		return messageRepository.getAllBySwitchableFromAndDestAndType(firstTeamId, secondTeamId, TEAM_TO_BEFORE)
				.stream()
				.map(this::getCompleteMessageDto)
				.collect(Collectors.toList());
	}

	@Override
	@Transactional
	public CompleteMessageDto createMessage(MessageDto messageDto) {
		MessageEntity entity = messageAdapter.dtoToEntity(messageDto);

		entity = messageRepository.save(entity);

		return getCompleteMessageDto(entity);
	}

	private CompleteMessageDto getCompleteMessageDto(MessageEntity entity) {
		User owner;

		try {
			owner = userServiceProxy.getOneUser(entity.getFrom());
		} catch (FeignException e) {
			Logger logger = Logger.getLogger(this.getClass().getName());
			logger.severe("Error while retrieving user information (userId=" + entity.getFrom() + ")");
			e.printStackTrace();

			owner = User.builder()
					.id(entity.getId())
					.build();
		}

		return CompleteMessageDto.builder()
				.id(entity.getId())
				.content(entity.getContent())
				.created(entity.getCreated())
				.updated(entity.getUpdated())
				.owner(owner)
				.build();
	}
}
