package com.seeu.darkside.message;

import org.springframework.stereotype.Component;

@Component
public class MessageAdapter {

	public MessageEntity dtoToEntity(MessageDto dto) {
		return MessageEntity.builder()
				.id(dto.getId())
				.content(dto.getContent())
				.type(dto.getType())
				.from(dto.getFrom())
				.dest(dto.getDest())
				.created(dto.getCreated())
				.updated(dto.getUpdated())
				.build();
	}

	public MessageDto entityToDto(MessageEntity entity) {
		return MessageDto.builder()
				.id(entity.getId())
				.content(entity.getContent())
				.type(entity.getType())
				.from(entity.getFrom())
				.dest(entity.getDest())
				.created(entity.getCreated())
				.updated(entity.getUpdated())
				.build();
	}
}
