package com.seeu.darkside.message;

import lombok.*;

import java.util.Date;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CompleteMessageDto<T> {

	private Long id;
	private String content;
	private T owner;

	private Date created;
	private Date updated;
}
