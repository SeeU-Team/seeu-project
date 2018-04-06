package com.seeu.darkside.team;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class TeamServiceTest {

    @InjectMocks
    TeamServiceImpl teamService;

    @Mock
    TeamRepository teamRepository;

    @Mock
    TeamAdapter teamAdapter;

    private TeamEntity team1;

    private TeamEntity team2;

    private TeamEntity team3;

    private TeamEntity team4;

    private TeamDto teamDto1;

    private TeamDto teamDto2;

    private TeamDto teamDto3;

    private TeamDto newTeamDto4;

    private Date date;

    public void initTeams() {
        date = new Date();

        team1 = TeamEntity.builder()
                .idTeam(1L)
                .name("team 1")
                .description("desc 1")
                .place("palce 1")
                .created(date)
                .updated(date)
                .build();

        team2 = TeamEntity.builder()
                .idTeam(2L)
                .name("team 2")
                .description("desc 2")
                .place("palce 2")
                .created(date)
                .updated(date)
                .build();

        team3 = TeamEntity.builder()
                .idTeam(3L)
                .name("team 3")
                .description("desc 3")
                .place("palce 3")
                .created(date)
                .updated(date)
                .build();

        team4 = TeamEntity.builder()
                .idTeam(4L)
                .name("team 4")
                .description("desc 4")
                .place("palce 4")
                .created(date)
                .updated(date)
                .build();

        teamDto1 = TeamDto.builder()
                .idTeam(1L)
                .name("team 1")
                .description("desc 1")
                .place("palce 1")
                .created(date)
                .updated(date)
                .build();

        teamDto2 = TeamDto.builder()
                .idTeam(2L)
                .name("team 2")
                .description("desc 2")
                .place("palce 2")
                .created(date)
                .updated(date)
                .build();

        teamDto3 = TeamDto.builder()
                .idTeam(3L)
                .name("team 3")
                .description("desc 3")
                .place("palce 3")
                .created(date)
                .updated(date)
                .build();

        newTeamDto4 = TeamDto.builder()
                .idTeam(4L)
                .name("team 4")
                .description("desc 4")
                .place("palce 4")
                .created(date)
                .updated(date)
                .build();
    }

    @Before
    public void configureMock() {
        initTeams();
        ArrayList<TeamEntity> list = new ArrayList<>();
        list.add(team1);
        list.add(team2);
        list.add(team3);

        when(teamAdapter.entityToDto(team1)).thenReturn(teamDto1);
        when(teamAdapter.entityToDto(team2)).thenReturn(teamDto2);
        when(teamAdapter.entityToDto(team3)).thenReturn(teamDto3);
        when(teamRepository.findAll()).thenReturn(list);

        when(teamAdapter.dtoToEntity(any(TeamDto.class))).thenReturn(team4);
        when(teamAdapter.entityToDto(team4)).thenReturn(newTeamDto4);
        when(teamRepository.save(any(TeamEntity.class))).thenReturn(team4);
    }

    @Test
    public void should_get_all_teams() {
        List<TeamDto> allTeams = teamService.getAllTeams();
        assertThat(allTeams).hasSize(3);
    }

    /**
    @Test
    public void should_create_new_team() {
        TeamDto createdTeam = teamService.createTeam(newTeamDto4);
        assertThat(createdTeam.getIdTeam()).isEqualTo(newTeamDto4.getIdTeam());
    }**/
}
