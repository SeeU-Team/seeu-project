package com.seeu.darkside.team;

import org.springframework.stereotype.Component;

@Component
public class TeamAdapter {

    public TeamEntity dtoToEntity(TeamDto teamDto) {
        return TeamEntity.builder()
                .idTeam(teamDto.getIdTeam())
                .name(teamDto.getName())
                .description(teamDto.getDescription())
                .place(teamDto.getPlace())
                .profilePhotoUrl(teamDto.getProfilePhotoUrl())
                .created(teamDto.getCreated())
                .updated(teamDto.getUpdated())
                .build();
    }

    public TeamDto entityToDto(TeamEntity teamEntity) {
        return TeamDto.builder()
                .idTeam(teamEntity.getIdTeam())
                .name(teamEntity.getName())
                .description(teamEntity.getDescription())
                .place(teamEntity.getPlace())
                .profilePhotoUrl(teamEntity.getProfilePhotoUrl())
                .created(teamEntity.getCreated())
                .updated(teamEntity.getUpdated())
                .build();
    }
}