package com.seeu.darkside.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserEntity {

    private Long id;

    @NotNull
    private Long facebookId;

	@NotEmpty
    private String name;

	@NotNull
	private Gender gender;

//	@NotEmpty
	@Email
    private String email;

	private String description;

    private String profilePhotoUrl;

	private TeammateStatus status;

    private Date created;

    private Date updated;
}
