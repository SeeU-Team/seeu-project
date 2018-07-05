package com.seeu.darkside.rs.dto;

import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TeamUpdateRoot {

	private String profilePicture;

	private TeamUpdate team;
}
