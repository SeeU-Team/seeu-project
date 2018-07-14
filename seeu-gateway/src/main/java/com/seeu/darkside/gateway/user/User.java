package com.seeu.darkside.gateway.user;

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

	private String appInstanceId;

	private String name;

	private Gender gender;

	private String email;

	private String description;

	private String profilePhotoUrl;

	private Date created;

	private Date updated;
}
