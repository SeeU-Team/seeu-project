package com.seeu.darkside.user;

import lombok.*;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class User {

	private Long id;

	private Long facebookId;

	private String firstname;

	private String lastname;

	private String email;

	private String password;

	private String description;

	private String profilePhotoUrl;

	private Date created;

	private Date updated;
}
