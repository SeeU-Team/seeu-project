package test.java.com.seeu.darkside.team;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.fail;
import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;


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

    public void initUsers() {

    }

    @Before
    public void configureMock() {

    }
}
