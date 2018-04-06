package test.java.com.seeu.darkside.team;

import com.jayway.restassured.RestAssured;
import com.seeu.darkside.team.TeamData;
import jdk.nashorn.internal.runtime.QuotedStringTokenizer;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.RestAssured.when;
import static com.jayway.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
@TeamData
public class TeamContollerIT {

    @LocalServerPort
    private int localServerPort;

    @Before
    public void init() {
        RestAssured.port = localServerPort;
    }


}
