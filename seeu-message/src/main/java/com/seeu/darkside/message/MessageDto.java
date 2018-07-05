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
	private String content;
	private ConversationType type;
	private Long from;
	private Long dest;
}
