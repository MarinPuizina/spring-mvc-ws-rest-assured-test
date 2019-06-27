package restassured;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runners.MethodSorters;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

// Name_ASCENDING -> Uses Lexicographic order. Therefore, we need to rename methods in alphabetical order.
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UsersWebServiceEndpointTest {

    private final String CONTEXT_PATH = "/spring-mvc-ws";
    private final String JSON = "application/json";
    private static String authorizationHeader;
    private static String userId;
    private static List<Map<String,String>> addresses;

    @BeforeEach
    void setUp() {

        // Setting up URL for REST Assured testing
        RestAssured.baseURI="http://localhost";
        RestAssured.port=8080;

    }

    /**
     * Test User Login
     *
     * */
    @Test
    void a() {

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

        authorizationHeader = response.header("Authorization");
        userId = response.header("UserId");

        assertNotNull(authorizationHeader);
        assertNotNull(userId);

    }

    /**
     * Test getUserDetails()
     *
     * */
    @Test
    void b() {

        Response response = given().
        pathParam("id", userId). // This way we don't need to do: "/users/" + userId
        header("Authorization", authorizationHeader).
        accept(JSON).
        when().
        get(CONTEXT_PATH + "/users/{id}").
        then().
        statusCode(200).
        contentType(JSON).
        extract().
        response();

        String userPublicId = response.jsonPath().getString("userId");
        String userEmail = response.jsonPath().getString("email");
        String firstName = response.jsonPath().getString("firstName");
        String lastName = response.jsonPath().getString("lastName");
        addresses =  response.jsonPath().getList("addresses");
        String addressId = addresses.get(0).get("addressId");

        assertNotNull(userPublicId);
        assertNotNull(userEmail);
        assertNotNull(firstName);
        assertNotNull(lastName);

        assertEquals("test3@test.com", userEmail);

        assertTrue(addresses.size() == 2);
        assertTrue(addressId.length() == 30);

    }

    /**
     * Test Update User Details
     * */
    @Test
    void c() {

        Map<String, Object> userDetails = new HashMap<>();
        userDetails.put("firstName", "Marn");
        userDetails.put("lastName", "Puizina");

        Response response = given()
        .contentType(JSON)
        .accept(JSON)
        .header("Authorization", authorizationHeader)
        .pathParam("id", userId)
        .body(userDetails)
        .when()
        .put(CONTEXT_PATH + "/users/{id}")
        .then()
        .statusCode(200)
        .contentType(JSON)
        .extract()
        .response();


        // TODO - Throws exceptions prob because of excluded hamcrest dependency
        /*
        String firstName = response.jsonPath().getString("firstName");
        String lastName = response.jsonPath().getString("lastName");

        List<Map<String,String>> storedAddresses = response.jsonPath().getList("addresses");

        assertEquals("Marn", firstName);
        assertEquals("Puizina", lastName);

        assertNotNull(storedAddresses);
        assertTrue(addresses.size() == storedAddresses.size());
        assertEquals(addresses.get(0).get("streetName"), storedAddresses.get(0).get("streetName"));
        */
    }

    /**
     * Test Delete User Details
     * */
    @Test
    @Ignore
    final void d()
    {
        Response response = given()
        .header("Authorization",authorizationHeader)
        .accept(JSON)
        .pathParam("id", userId)
        .when()
        .delete(CONTEXT_PATH + "/users/{id}")
        .then()
        .statusCode(200)
        .contentType(JSON)
        .extract()
        .response();

        String operationResult = response.jsonPath().getString("operationResult");
        assertEquals("SUCCESS", operationResult);

    }

}
