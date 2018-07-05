package com.seeu.darkside.message;

import com.seeu.darkside.team.Team;
import com.seeu.darkside.user.User;

public interface MessageService {
	CompleteMessageDto<User> createUserMessage(Long userId, NewMessage<User> newMessage);

	CompleteMessageDto<User> createTeamMessage(Long teamId, NewMessage<User> newMessage);

	CompleteMessageDto<Team> createBeforeMessage(Long teamId, NewMessage<Team> newMessage);
}
