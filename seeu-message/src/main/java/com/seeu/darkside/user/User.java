package com.seeu.darkside.user;

import lombok.*;

import java.util.Date;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User {

	private Long id;

	private Long facebookId;

	private String appInstanceId;

	private String name;

	private Gender gender;

	private String email;

	private String description;

	private String profilePhotoUrl;

	private Date created;

	private Date updated;
}
