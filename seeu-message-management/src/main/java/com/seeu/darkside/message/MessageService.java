package com.seeu.darkside.message;

import java.util.List;

public interface MessageService {

	List<CompleteMessageDto> getAllMessagesForUserToUser(Long firstUserId, Long secondUserId);

	List<CompleteMessageDto> getAllMessagesForTeam(Long teamId);

	List<CompleteMessageDto> getAllMessagesForTeamToBefore(Long firstTeamId, Long secondTeamId);

	CompleteMessageDto createMessage(MessageDto messageDto);
}
