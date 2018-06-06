package com.seeu.media.asset;

import com.jayway.restassured.RestAssured;
import com.seeu.media.rs.dto.TagDTO;
import com.seeu.media.tag.TagData;
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
@AssetData
@TestPropertySource(locations = "classpath:application-test.properties")
public class AssetContollerIT {

    @LocalServerPort
    private int localServerPort;

    @Before
    public void init() {
        RestAssured.port = localServerPort;
    }

    @Test
    public void should_get_all_assets() {
        when()
                .get("/assets/list")
                .then()
                .statusCode(200)
                .body("$", hasSize(3));
    }

    @Test
    public void should_get_one_by_id() {
        when()
                .get("/assets?assetId=2")
        .then()
                .statusCode(200)
                .body("idAsset", is(2))
                .body("name", is("asset2"))
                .body("mediaId", is(222));
    }

    @Test
    public void should_create_new_tags() {
        Date date = new Date();

        TagDTO tag = TagDTO.builder()
                .name("asset4")
                .build();

        given()
                .contentType(JSON)
                .body(tag)
                .when()
                .post("/assets")
                .then()
                .statusCode(201);
    }
}
