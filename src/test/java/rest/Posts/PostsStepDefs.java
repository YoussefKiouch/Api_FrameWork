package rest.Posts;


import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import utils.RestClientBDD;


public class PostsStepDefs {
    RestClientBDD restClientBDD = new RestClientBDD();

    @When("user creates a post with title {string} and body {string}")
    public void userCreatesAPostWithTitleAndBody(String title, String body) {
        restClientBDD.CreatPostUsingUserId(title, body);
    }

    @Then("check if post is created with title {string} and body {string}")
    public void checkIfPostIsCreatedWithTitleAndBody(String title, String body) {
        restClientBDD.ValidatePostWithTitleAndBodyIsCreated(title,body);
    }
}
