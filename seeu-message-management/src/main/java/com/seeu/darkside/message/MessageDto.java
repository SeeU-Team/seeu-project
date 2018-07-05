package com.seeu.darkside.message;

import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class MessageDto {

	private Long id;

	@NotNull
	@NotEmpty
	private String content;

	@NotNull
	private ConversationType type;

	@NotNull
	private Long from;

	@NotNull
	private Long dest;

	private Date created;
	private Date updated;
}
