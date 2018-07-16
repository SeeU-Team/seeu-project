package com.seeu.darkside.rs.dto;

import com.seeu.darkside.team.Team;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TeamsBody {

	private Team firstTeam;
	private Team secondTeam;
}
