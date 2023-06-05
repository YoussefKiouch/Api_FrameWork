package utils;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class RestClient {

    public static Response createUser(String bearerToken, String jsonBody){

        return  RestAssured
                .given()
                .contentType(ContentType.JSON)
                .headers("Authorisation", "Bearer" + bearerToken)
                .accept(ContentType.JSON)
                .body(jsonBody)
                .when()
                .post("/users");
    }

    public static Response getUser(String bearerToken, String userId){
        return RestAssured
                //Arrange
                .given()
                //contentType is about type of data you send to server
                .contentType(ContentType.JSON)
                //Headers include authorisation and accept data is key value type
                .headers("Authorisation", "Bearer" + bearerToken)
                //this is about what type of data you receive as a client
                .accept(ContentType.JSON)
                //Act -> send is happening
                .when()
                .pathParam("userId", userId)
                .get("/users/{userId}");
    }

    public static Response PutUser(String bearerToken, String UserId, String JsonBody){
        return RestAssured
                .given()
                .contentType(ContentType.JSON)
                .headers("Authorisation", bearerToken)
                .body(JsonBody)
                .when()
                .pathParam("UserId", UserId)
                .put("/users/{UserId}");
    }

    public static Response deleteUser(String bearerToken, String userId){
        return RestAssured
                //Arrange
                .given()
                //contentType is about type of data you send to server
                .contentType(ContentType.JSON)
                //Headers include authorisation and accept data is key value type
                .headers("Authorisation", "Bearer" + bearerToken)
                //this is about what type of data you receive as a client
                .accept(ContentType.JSON)
                //Act -> send is happening
                .when()
                .pathParam("userId", userId)
                .delete("/users/{userId}");
    }
}
