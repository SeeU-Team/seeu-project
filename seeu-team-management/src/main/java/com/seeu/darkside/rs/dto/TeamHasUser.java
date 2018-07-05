package com.seeu.darkside.rs.dto;

import com.seeu.darkside.user.TeammateStatus;
import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TeamHasUser {

	private Long memberId;
	private TeamProfile team;
	private TeammateStatus status;
}
