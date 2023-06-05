package utils;

import Pojos.Posts;
import Pojos.User;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import static org.junit.jupiter.api.Assertions.assertAll;
import rest.Users.BaseClass;

import java.util.ArrayList;
import java.util.Date;

public class RestClientBDD extends BaseClass {
    RestClient resClient = new RestClient();
    Date date = new Date();

    public void createUserValidateCreated() {

        User user = new User();
        user.setName("DevxTestingAccount");
        user.setEmail("devxtestingaccount"  + date.getTime() + "@gmail.com");
        user.setGender("male");
        user.setStatus("active");

        String JsonUser = gson.toJson(user);

        Response PostResponse = RestAssured
                .given()
                .contentType(ContentType.JSON)
                .headers("Authorisation", "Bearer" + token)
                .accept(ContentType.JSON)
                .body(JsonUser)
                .when()
                .post("/users");

                assumeTrue(PostResponse.getStatusCode() == 201);

    }
    public int getUserIdFromTestAccount(){
        Response getResponse = RestAssured
                .given()
                .contentType(ContentType.JSON)
                .headers("Authorisation", "Bearer" + token)
                .accept(ContentType.JSON)
                .when()
                .post("/users?=DevxTestingAccount");

        assumeTrue(getResponse.getStatusCode() == 200);
        ArrayList ids = (ArrayList) getResponse.jsonPath().getList("id");
        System.out.println(ids);

        return (int) ids.get(0);
    }

    public void CreatPostUsingUserId(String title, String body){

        Posts post =new Posts(title,body);
        String jsonPost = gson.toJson(post);
        Response PostResponse = RestAssured
                .given()
                .contentType(ContentType.JSON)
                .headers("Authorisation", "Bearer" + token)
                .accept(ContentType.JSON)
                .body(jsonPost)
                .when()
                .pathParam("userId", getUserIdFromTestAccount())
                .post("/users/{userId}/posts");

        assumeTrue(PostResponse.getStatusCode() == 201);

    }

    public void ValidatePostWithTitleAndBodyIsCreated(String title, String body){

        Response GetResponse = RestAssured
                .given()
                .contentType(ContentType.JSON)
                .headers("Authorisation", "Bearer" + token)
                .accept(ContentType.JSON)
                .when()
                .pathParam("userId", getUserIdFromTestAccount())
                .get("/users/{userId}/posts");
        String getJson = GetResponse.asString().replace("[","").replace("]","");
        System.out.println(getJson);

        Posts getPost = gson.fromJson(GetResponse.asString(), Posts.class);
        assertAll(
                ()  ->assertEquals(getUserIdFromTestAccount() + "", getPost.getUserId()),
                ()  ->assertEquals(title,getPost.getTitle()),
                ()  ->assertEquals(body,getPost.getBody())
        );
    }
}
