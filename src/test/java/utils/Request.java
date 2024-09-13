package utils;

import constants.BookingEndPoints;
import entities.ClientRB;
import io.cucumber.core.internal.com.fasterxml.jackson.core.JsonProcessingException;
import io.cucumber.core.internal.com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class Request {
    public static Response getWithId(String endpoint, String id){
        RestAssured.baseURI = BookingEndPoints.BASE_URL;
        Response response = RestAssured
                .given().pathParam("id", id)
                .when().get(endpoint);
        response.then().log().body();
        return response;
    }

    public static Response post(String endpoint, ClientRB client) throws JsonProcessingException {
        RestAssured.baseURI = BookingEndPoints.BASE_URL;

        ObjectMapper mapper = new ObjectMapper();
        String miJson = mapper.writeValueAsString(client);

        Response response = RestAssured
                .given().contentType(ContentType.JSON).body(miJson)
                .when().post(endpoint);

        response.then().log().body();
        return response;
    }
}
