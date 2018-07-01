package com.seeu.darkside.rs.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TeamMerge {

	@NotNull
	private Long idFirst;

	@NotNull
	private Long idSecond;
}