package restassured;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class UsersWebServiceEndpointTest {

    private final String CONTEXT_PATH = "/spring-mvc-ws";
    private final String JSON = "application/json";

    @BeforeEach
    void setUp() {

        // Setting up URL for REST Assured testing
        RestAssured.baseURI="http://localhost";
        RestAssured.port=8080;

    }

    @Test
    void userLogin() {

        Map<String, String> loginDetails = new HashMap<>();
        loginDetails.put("email","test3@test.com");
        loginDetails.put("password","123");

        Response response = given().
        contentType(JSON).
        accept(JSON).
        body(loginDetails).
        when().
        post(CONTEXT_PATH + "/users/login").
        then().
        statusCode(200).
        extract().
        response();

        String authorizationHeader = response.header("Authorization");
        String userId = response.header("UserId");

        assertNotNull(authorizationHeader);
        assertNotNull(userId);

    }

}
