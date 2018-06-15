package com.seeu.darkside.team;

import com.jayway.restassured.RestAssured;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.RestAssured.when;
import static com.jayway.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
@TeamData
@TestPropertySource(locations = "classpath:application-test.properties")
public class TeamContollerIT {

    @LocalServerPort
    private int localServerPort;

    @Before
    public void init() {
        RestAssured.port = localServerPort;
    }

    @Test
    public void should_get_all_teams() {
        when()
                .get("/teams/list")
                .then()
                .statusCode(200)
                .body("$", hasSize(3));
    }

    @Test
    @Ignore
    public void should_create_new_team() {
        Date date = new Date();

        TeamDto team = TeamDto.builder()
                .name("team 4")
                .description("Description 4")
                .place("Paris")
                .build();

        given()
                .contentType(JSON)
                .body(team)
                .when()
                .post("/teams")
                .then()
                .statusCode(201);
    }
}
