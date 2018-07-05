package com.seeu.darkside.message;

import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NewMessage<T> {

	@NotEmpty
	private String content;

	@NotNull
	private T owner;
}
