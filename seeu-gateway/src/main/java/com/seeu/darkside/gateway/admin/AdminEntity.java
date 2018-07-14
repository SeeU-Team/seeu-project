package com.seeu.darkside.gateway.admin;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "admin")
public class AdminEntity {

	@Id
	@GeneratedValue
	private Long id;

	@Email
	@Column
	private String email;

	@Column
	private String password;
}
