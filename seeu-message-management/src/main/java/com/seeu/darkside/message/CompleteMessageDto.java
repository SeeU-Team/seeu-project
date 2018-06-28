package com.seeu.darkside.message;

import com.seeu.darkside.user.User;
import lombok.*;

import java.util.Date;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CompleteMessageDto {

	private Long id;
	private String content;
	private User owner;

	private Date created;
	private Date updated;
}
