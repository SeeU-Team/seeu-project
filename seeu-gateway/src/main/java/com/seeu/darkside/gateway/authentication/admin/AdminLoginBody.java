package com.seeu.darkside.gateway.authentication.admin;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class AdminLoginBody {

	@NotNull
	@Email
	private String email;

	@NotNull
	@Length(min = 8)
	private String password;
}
