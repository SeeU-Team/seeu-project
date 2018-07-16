package com.seeu.darkside.notification;

import com.seeu.darkside.team.TeamDto;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TeamsBody {

	private TeamDto firstTeam;
	private TeamDto secondTeam;
}
