package com.seeu.darkside.notification;

import com.seeu.darkside.message.CompleteMessageDto;
import com.seeu.darkside.team.Team;
import com.seeu.darkside.user.User;

public interface NotificationService {

	void sendUserToUserMessageNotification(CompleteMessageDto<User> completeMessageDto, Long userId);

	void sendTeamMessageNotification(CompleteMessageDto<User> completeMessageDto, Long teamId);

	void sendBeforeMessageNotification(CompleteMessageDto<Team> completeMessageDto, Long teamId);
}
