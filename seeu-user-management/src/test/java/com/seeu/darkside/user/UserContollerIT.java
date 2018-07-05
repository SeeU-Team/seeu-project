package com.seeu.darkside.user;

import com.jayway.restassured.RestAssured;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
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
@UserData
public class UserContollerIT {

    @LocalServerPort
    private int localServerPort;

    @Before
    public void init() {
        RestAssured.port = localServerPort;
    }

    @Test
    public void should_get_all_users() {
        when()
                .get("/users")
                .then()
                .statusCode(200)
                .body("$", hasSize(3));
    }

    @Test
    public void should_get_one_user_by_id() {
        when()
                .get("/users/1")
                .then()
                .statusCode(200)
                .body("id", is(1));
    }

    @Test
    public void should_get_one_user_by_email() {
        when()
                .get("/users/?email=three@email.com")
                .then()
                .statusCode(200)
                .body("id", is(3));
    }

    @Test
    public void should_throw_UserNotfoundException_for_unknow_user_id() {
        when()
                .get("/users/662")
                .then()
                .statusCode(404);
    }

    @Test
    public void should_throw_UserNotfoundException_for_unknow_email() {
        when()
                .get("/users/?email=test@gmail.com")
                .then()
                .statusCode(404);
    }

    @Test
    public void should_insert_user() {
        Date date = new Date();

        UserDto user = UserDto.builder()
                .name("fourth")
                .email("fourth@email.com")
                .description("description4")
                .profilePhotoUrl("url/picture4.png")
                .created(date)
                .updated(date)
                .build();

        given()
                .contentType(JSON)
                .body(user)
        .when()
                .post("/users")
        .then()
                .statusCode(201);
    }

    @Test
    public void should_delete_an_user() {
        when()
                .delete("/users/1")
                .then()
                .statusCode(204);
    }

    @Test
    public void should_throw_UserNotfoundException_try_delete_unknow_user() {
        when()
                .delete("/users/6")
                .then()
                .statusCode(404);
    }

    @Test
    public void should_update_user_description() {
        String description = "My new description";

        given()
                .body(description)
                .when()
                .patch("/users/2")
                .then()
                .statusCode(200)
                .body("description", is(description));
    }
}
