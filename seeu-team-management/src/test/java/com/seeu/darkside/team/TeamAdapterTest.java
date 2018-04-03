package com.seeu.darkside.team;

import com.seeu.darkside.team.TeamAdapter;
import com.seeu.darkside.team.TeamDto;
import com.seeu.darkside.team.TeamEntity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
public class TeamAdapterTest {

    @InjectMocks
    TeamAdapter teamAdapter;

    @Test
    public void should_return_an_user_entity() {
        Date date = new Date();
        TeamDto teamDto = TeamDto.builder()
                .id(1L)
                .name("My Test Team")
                .description("description")
                .place("Paris")
                .created(date)
                .updated(date)
                .build();

        TeamEntity teamEntity = teamAdapter.dtoToEntity(teamDto);

        assertThat(teamEntity.getId()).isEqualTo(teamDto.getId());
        assertThat(teamEntity.getName()).isEqualTo(teamDto.getName());
        assertThat(teamEntity.getDescription()).isEqualTo(teamDto.getDescription());
        assertThat(teamEntity.getPlace()).isEqualTo(teamDto.getPlace());
        assertThat(teamEntity.getCreated()).isEqualTo(teamDto.getCreated());
        assertThat(teamEntity.getUpdated()).isEqualTo(teamDto.getUpdated());
    }

    @Test
    public void should_return_an_user_dto() {
        Date date = new Date();
        TeamEntity teamEntity = TeamEntity.builder()
                .id(1L)
                .name("My Test Team")
                .description("description")
                .place("Paris")
                .created(date)
                .updated(date)
                .build();

        TeamDto teamDto = teamAdapter.entityToDto(teamEntity);

        assertThat(teamDto.getId()).isEqualTo(teamEntity.getId());
        assertThat(teamDto.getName()).isEqualTo(teamEntity.getName());
        assertThat(teamDto.getDescription()).isEqualTo(teamEntity.getDescription());
        assertThat(teamDto.getPlace()).isEqualTo(teamEntity.getPlace());
        assertThat(teamDto.getCreated()).isEqualTo(teamEntity.getCreated());
        assertThat(teamDto.getUpdated()).isEqualTo(teamEntity.getUpdated());
    }

}
