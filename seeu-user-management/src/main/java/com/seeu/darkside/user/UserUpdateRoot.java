package com.seeu.darkside.user;

import lombok.*;

import javax.validation.constraints.NotNull;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserUpdateRoot {

	private String profilePicture;

	@NotNull
	private UserDto member;
}
