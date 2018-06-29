package com.seeu.darkside.message;

import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface MessageService {

	List<CompleteMessageDto> getAllMessagesForUserToUser(Long firstUserId, Long secondUserId);

	List<CompleteMessageDto> getAllMessagesForTeam(Long teamId);

	List<CompleteMessageDto> getAllMessagesForTeamToBefore(Long firstTeamId, Long secondTeamId);

	@Transactional(readOnly = true)
	List<Long> getFriendsOf(Long userId);

	CompleteMessageDto createMessage(MessageDto messageDto);
}
