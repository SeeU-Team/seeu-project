package com.seeu.darkside.team;

import org.springframework.stereotype.Component;

@Component
public class TeamAdapter {

    public TeamEntity dtoToEntity(TeamDto teamDto) {
        return TeamEntity.builder()
                .id(teamDto.getId())
                .name(teamDto.getName())
                .description(teamDto.getDescription())
                .place(teamDto.getPlace())
                .created(teamDto.getCreated())
                .updated(teamDto.getUpdated())
                .build();
    }

    public TeamDto entityToDto(TeamEntity teamEntity) {
        return TeamDto.builder()
                .id(teamEntity.getId())
                .name(teamEntity.getName())
                .description(teamEntity.getDescription())
                .place(teamEntity.getPlace())
                .created(teamEntity.getCreated())
                .updated(teamEntity.getUpdated())
                .build();
    }
}