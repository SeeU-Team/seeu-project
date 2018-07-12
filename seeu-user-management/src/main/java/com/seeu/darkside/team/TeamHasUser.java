package com.seeu.darkside.team;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TeamHasUser {

	private Long memberId;
	private TeamProfile team;
	private TeammateStatus status;
}
