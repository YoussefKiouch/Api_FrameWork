package rest.Users;

import Pojos.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.Test;
import utils.RestClient;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

public class CRUDUser_Test extends BaseClass {
    //ARRANGE
    //SET UP BASEURL TO RESTASSURED
    RestClient restclient = new RestClient();

    @Test
    public void givenValidResponse_checkStatusCode() {


        //setting up baseUrl as in postman
        Response getResponse = RestAssured.given()
                .contentType(ContentType.JSON)
                .headers("Authorization", token)
                .accept(ContentType.JSON)
                .when()
                .get("/users");
        System.out.println(getResponse.asString());
        assertEquals(200, getResponse.statusCode());
    }

    @Test
    public void createUser_givenValidResponse_createdStatusCode() {

        // pojo - plain old java object
        // Class User
        // Serialization -> when u convert A to B.In our case from pojo to json format
        User user = new User();
        user.setName(faker.name().fullName());
        user.setEmail(user.getName().toLowerCase(Locale.ROOT).trim().replace(" ", "") + "@gmail.com");
        user.setGender("male");
        user.setStatus("active");

        System.out.println(gson.toJson(user));


        Response PostResponse = restclient.createUser(token, gson.toJson(user));
        System.out.println(PostResponse.asString());

        assertAll(
                () -> assertEquals(200, PostResponse.getStatusCode()),
                () -> assertTrue(PostResponse.asString().contains(user.getName())),
                () -> assertTrue(PostResponse.asString().contains(user.getEmail()))
        );
    }

    @Test
    public void getUser_givenValidResponse_okStatusCode() {

        User user = new User();
        user.setName(faker.name().fullName());
        user.setEmail(user.getName().toLowerCase(Locale.ROOT).trim().replace(" ", "") + "@gmail.com");
        user.setGender("male");
        user.setStatus("active");

        System.out.println(gson.toJson(user));

        Response PostResponse = restclient.createUser(token, gson.toJson(user));

        //Given and when are correct

        assumeTrue(200 == PostResponse.getStatusCode());

        System.out.println(PostResponse.asString());

        User userPost = gson.fromJson(PostResponse.asString(), User.class);

        Response getResponse = restclient.getUser(token, userPost.getId());

        assertAll(
                () -> assertEquals(200, getResponse.getStatusCode()),
                () -> assertTrue(getResponse.asString().contains(user.getName())),
                () -> assertTrue(getResponse.asString().contains(user.getEmail()))
        );
    }

    @Test
    public void getUser_givenValidResponse_okStatusCode1() {

        User user = new User();
        user.setName(faker.name().fullName());
        user.setEmail(user.getName().toLowerCase(Locale.ROOT).trim().replace(" ", "") + "@gmail.com");
        user.setGender("male");
        user.setStatus("active");

        System.out.println(gson.toJson(user));

        Response PostResponse = restclient.createUser(token, gson.toJson(user));

        //Given and when are correct

        assumeTrue(200 == PostResponse.getStatusCode());

        System.out.println(PostResponse.asString());
        //ACT
        //Task is to retrieve id from json response without using pojo
        //Covering diff ways to retrieve info from json response
        //Gson library, convert json to pojo
        //Address directly to values in json using key and jsonPath

        int postUserId = PostResponse.jsonPath().getInt("id");

        //Task: convert POJO object to JSON Without Using GSON
        User PostUser = PostResponse.as(new TypeRef<User>() {
        });
        System.out.println(PostUser.getName());
        System.out.println(PostUser.getEmail());
        System.out.println(PostUser.getStatus());

        Response getResponse = restclient.getUser(token, postUserId + "");

        assertAll(
                () -> assertEquals(200, getResponse.getStatusCode()),
                () -> assertTrue(getResponse.asString().contains(user.getName())),
                () -> assertTrue(getResponse.asString().contains(user.getEmail()))
        );
    }

    @Test
    public void SerializationWithGson_DeserializatiomWithRestAssuredAndJsonPath() {

        User user = new User();
        user.setName(faker.name().fullName());
        user.setEmail(user.getName().toLowerCase(Locale.ROOT).trim().replace(" ", "") + "@gmail.com");
        user.setGender("male");
        user.setStatus("active");

        System.out.println(gson.toJson(user));

        Response PostResponse = restclient.createUser(token, gson.toJson(user));

        //Given and when are correct

        assumeTrue(200 == PostResponse.getStatusCode());

        System.out.println(PostResponse.asString());
        //ACT
        //Task is to retrieve id from json response without using pojo
        //Covering diff ways to retrieve info from json response
        //Gson library, convert json to pojo
        //Address directly to values in json using key and jsonPath

        int postUserId = PostResponse.jsonPath().getInt("id");

        //Task: convert POJO object to JSON Without Using GSON
        User PostUser = PostResponse.as(new TypeRef<User>() {
        });
        System.out.println(PostUser.getName());
        System.out.println(PostUser.getEmail());
        System.out.println(PostUser.getStatus());

        Response getResponse = restclient.getUser(token, postUserId + "");

        assertAll(
                () -> assertEquals(200, getResponse.getStatusCode()),
                () -> assertTrue(getResponse.asString().contains(user.getName())),
                () -> assertTrue(getResponse.asString().contains(user.getEmail()))
        );
    }

    @Test
    public void SerializationAndDeserializationUsingJackson() throws JsonProcessingException {

        User user = new User();
        user.setName(faker.name().fullName());
        user.setEmail(user.getName().toLowerCase(Locale.ROOT).trim().replace(" ", "") + "@gmail.com");
        user.setGender("male");
        user.setStatus("active");

        //Serialization process
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonBody = objectMapper.writeValueAsString(user);

        System.out.println(jsonBody);

        Response PostResponse = restclient.createUser(token, jsonBody);

        User PostUser = objectMapper.readValue(PostResponse.asString(), User.class);

        System.out.println(PostUser.toString());
        System.out.println(PostUser.getId());

        //Given and when are correct

//        Assumptions.assumeTrue(201 == PostResponse.getStatusCode());
//
//        System.out.println(PostResponse.asString());


//        Response getResponse = restclient.getUser(token, postUserId + "");
//
//        assertAll(
//                () -> assertEquals(200, getResponse.getStatusCode()),
//                () -> assertTrue(getResponse.asString().contains(user.getName())),
//                () -> assertTrue(getResponse.asString().contains(user.getEmail()))
//        );
    }

    @Test
    public void PutUser_GivenValidResponse_OkStatusCode() {

        User user = new User(faker.name().fullName(), "", "male", "active");
        user.setEmail(user.getName().toLowerCase(Locale.ROOT).trim().replace(" ", "") + "@gmail.com");

        System.out.println(gson.toJson(user));

        Response PostResponse = restclient.createUser(token, gson.toJson(user));
        Assumptions.assumeTrue(200 == PostResponse.getStatusCode());
        System.out.println(PostResponse.asString());

        User UserPost = gson.fromJson(PostResponse.asString(), User.class);

        user.setStatus("inactive");
        user.setEmail(user.getName().toLowerCase(Locale.ROOT).trim().replace(" ", "") + "@yahoo.com");


        Response PutResponse = restclient.PutUser(token, UserPost.getId(), gson.toJson(user));
        Response getResponse = restclient.getUser(token, UserPost.getId());
        System.out.println(getResponse.asString());
        User UserGet = gson.fromJson(getResponse.asString(), User.class);

        assertAll(
                () -> assertEquals(200, PutResponse.getStatusCode()),
                () -> assertEquals(200, getResponse.getStatusCode()),
                () -> assertEquals(user.getStatus(), UserGet.getStatus()),
                () -> assertEquals(user.getEmail(), UserGet.getEmail())
        );


    }

    @Test
    public void DeleteUser_GivenValidResponse_NoContentStatusCode() {

        User user = new User(faker.name().fullName(), "", "male", "active");
        user.setEmail(user.getName().toLowerCase(Locale.ROOT).trim().replace(" ", "") + "@gmail.com");

        System.out.println(gson.toJson(user));

        Response PostResponse = restclient.createUser(token, gson.toJson(user));
        Assumptions.assumeTrue(201 == PostResponse.getStatusCode());

        System.out.println(PostResponse.asString());
        User UserPost = gson.fromJson(PostResponse.asString(), User.class);


        Response DeleteResponse = restclient.deleteUser(token, UserPost.getId());
        Response getResponse = restclient.getUser(token, UserPost.getId());

        assertAll(
                () -> assertEquals(200, DeleteResponse.getStatusCode()),
                () -> assertEquals(404, getResponse.getStatusCode()),
                () -> assertEquals("Resource not found", getResponse.jsonPath().getString("message"))
        );
    }
}


