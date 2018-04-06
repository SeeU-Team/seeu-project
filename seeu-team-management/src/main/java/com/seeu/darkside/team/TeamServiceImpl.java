package com.seeu.darkside.team;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TeamServiceImpl implements TeamService {

    private final TeamRepository teamRepository;
    private final TeamAdapter teamAdapter;

    @Autowired
    public TeamServiceImpl(TeamRepository teamRepository, TeamAdapter teamAdapter) {
        this.teamRepository = teamRepository;
        this.teamAdapter = teamAdapter;
    }

    @Override
    public List<TeamDto> getAllTeams() {
        List<TeamDto> teamDtoList = teamRepository.findAll()
                .stream()
                .map((teamEntity) -> {
                    TeamDto teamDto = teamAdapter.entityToDto(teamEntity);
                    return teamDto;
                }).collect(Collectors.toList());
        return teamDtoList;
    }

    @Override
    public TeamDto createTeam(TeamDto teamDto) {
        Date now = new Date();
        teamDto.setCreated(now);
        teamDto.setUpdated(now);

        TeamEntity teamEntity = teamAdapter.dtoToEntity(teamDto);
        TeamEntity teamSaved = teamRepository.save(teamEntity);

        return teamAdapter.entityToDto(teamSaved);
    }

}
