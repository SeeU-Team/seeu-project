package com.seeu.darkside.gateway.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.persistence.Entity;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class User {

	private Long id;

	@JsonProperty(value = "id")
	private Long facebookId;
}
