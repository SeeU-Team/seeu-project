package com.seeu.media.tag;

import com.jayway.restassured.RestAssured;
import com.seeu.media.rs.dto.TagDTO;
import org.junit.Before;
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
import static org.hamcrest.Matchers.is;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
@TagData
@TestPropertySource(locations = "classpath:application-test.properties")
public class TagContollerIT {

    @LocalServerPort
    private int localServerPort;

    @Before
    public void init() {
        RestAssured.port = localServerPort;
    }

    @Test
    public void should_get_all_tags() {
        when()
                .get("/tags/list")
                .then()
                .statusCode(200)
                .body("$", hasSize(3));
    }

    @Test
    public void should_get_one_by_id() {
        when()
                .get("/tags?tagId=2")
        .then()
                .statusCode(200)
                .body("idTag", is(2))
                .body("name", is("tag2"));
    }

    @Test
    public void should_create_new_tags() {
        Date date = new Date();

        TagDTO tag = TagDTO.builder()
                .name("tag1")
                .build();

        given()
                .contentType(JSON)
                .body(tag)
                .when()
                .post("/tags")
                .then()
                .statusCode(201);
    }
}
