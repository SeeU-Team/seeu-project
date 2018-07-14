package com.seeu.darkside.gateway.authentication.app;

import lombok.*;

import javax.validation.constraints.NotNull;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoginBody {

	@NotNull
	private String accessToken;

	private String appInstanceId;
}
