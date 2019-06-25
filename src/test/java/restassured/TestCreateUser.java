package restassured;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

public class TestCreateUser {

    private final String CONTEXT_PATH = "/spring-mvc-ws";

    @BeforeEach
    void setUp() {

        // Setting up URL for REST Assured testing
        RestAssured.baseURI="http://localhost";
        RestAssured.port=8080;

    }

    @Test
    void createUser() {

        List<Map<String, Object>> userAddresses = new ArrayList<>();

        Map<String, Object> shippingAddress = new HashMap<>();
        shippingAddress.put("city", "Split");
        shippingAddress.put("country", "Croatia");
        shippingAddress.put("streetName", "123 Street name");
        shippingAddress.put("postalCode", "123456");
        shippingAddress.put("type", "shipping");

        Map<String, Object> billingAddress = new HashMap<>();
        billingAddress.put("city", "Vancouver");
        billingAddress.put("country", "Canada");
        billingAddress.put("streetName", "123 Street name");
        billingAddress.put("postalCode", "123456");
        billingAddress.put("type", "billing");

        userAddresses.add(shippingAddress);
        userAddresses.add(billingAddress);

        // Mimic the request body for createUser
        Map<String, Object> userDetails = new HashMap<>();
        userDetails.put("firstName", "Marin");
        userDetails.put("lastName", "Puizina");
        userDetails.put("email", "test8@test.com");
        userDetails.put("password", "123");
        userDetails.put("addresses", userAddresses);

        // REST Assured
        Response response = given(). // given that we have a http request
        contentType("application/json"). // which has contentType of type application/json
        accept("application/json"). // which accepts application/json
        body(userDetails). // which has a body (atm null)
        when(). // when this http request is sent
        post(CONTEXT_PATH + "/users"). // as http post to the following url
        then(). // then we assert
        statusCode(200). // that the status code of http response is going to be 200
        contentType("application/json"). // and response contentType is of type application/json
        extract(). // extract
        response(); // the response body

        String userId = response.jsonPath().getString("userId"); // we expect to get the userId element as part of json response
        assertNotNull(userId);

        String responseBody = response.getBody().asString();
        try {

            JSONObject responseBodyJson = new JSONObject(responseBody);
            JSONArray addresses = responseBodyJson.getJSONArray("addresses");

            assertNotNull(addresses);
            assertTrue(addresses.length() == 2);

            String addressId = addresses.getJSONObject(0).getString("addressId");
            assertNotNull(addressId);
            assertTrue(addressId.length() == 30);

        } catch (JSONException e) {
            fail(e.getMessage());
        }

    }

}
