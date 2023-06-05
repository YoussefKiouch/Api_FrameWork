package rest.Users;

import com.github.javafaker.Faker;
import com.google.gson.Gson;
import io.restassured.RestAssured;
import org.junit.Before;
import utils.ConfigReader;

public abstract class BaseClass {

    public static final String apiHost = ConfigReader.getProperty("apiHost");
    public static final String apiVersion = ConfigReader.getProperty("apiVersion");
    public static final String token = ConfigReader.getProperty("token");
    public static final Faker faker = new Faker();
    public static final Gson gson =  new Gson();

    @Before
    public void baseUrlSetupBDD() {

        RestAssured.baseURI = apiHost + apiVersion;
    }
}
