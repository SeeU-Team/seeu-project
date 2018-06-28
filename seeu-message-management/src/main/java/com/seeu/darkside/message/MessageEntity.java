package com.seeu.darkside.message;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "message")
public class MessageEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column
	private String content;

	@Column
	private ConversationType type;

	@Column
	private Long from;

	@Column
	private Long dest;

	@Column
	@Temporal(TemporalType.TIMESTAMP)
	private Date created;

	@Column
	@Temporal(TemporalType.TIMESTAMP)
	private Date updated;

}
