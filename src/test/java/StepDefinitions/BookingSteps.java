package StepDefinitions;

import com.fasterxml.jackson.core.JsonProcessingException;
import constants.BookingEndPoints;
import entities.ClientRB;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import org.hamcrest.Matchers;
import utils.Request;

import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;

public class BookingSteps {
    Response response;
    ClientRB client = new ClientRB();
    int bookingId;

    @Given("I perform a GET call to the booking endpoint with id {string}")
    public void getBookingWithId(String id){
        response = Request.getWithId(BookingEndPoints.GET_BOOKING, id);
    }

    @Then("I verify that the status code is {int}")
    public void verifyStatusCode(int statusCode){
        response.then().assertThat().statusCode(statusCode);
    }

    @And("I verify that the body does not have size {int}")
    public void verifyResponseSize(int size){
        response.then().assertThat().body("size()", not(size));
    }

    @Given("I perform a GET call to the booking endpoint with this invalid character {string}")
    public void getBookingWithInvalidCharacter(String invalidCharacter)
    {
        response = Request.getWithId(BookingEndPoints.GET_BOOKING, invalidCharacter);
    }

    @When("I create a booking with the following details")
    public void createBooking(DataTable bookingDetails) throws io.cucumber.core.internal.com.fasterxml.jackson.core.JsonProcessingException {
        List<Map<String, String>> data = bookingDetails.asMaps(String.class, String.class);

        client.setFirstname(data.get(0).get("firstname"));
        client.setLastname(data.get(0).get("lastname"));
        client.setTotalprice(Integer.parseInt(data.get(0).get("totalprice")));
        client.setDepositpaid(Boolean.parseBoolean(data.get(0).get("depositpaid")));

        ClientRB.BookingDates bookingDates = new ClientRB.BookingDates();
        bookingDates.setCheckin(data.get(0).get("checkin"));
        bookingDates.setCheckout(data.get(0).get("checkout"));
        client.setBookingdates(bookingDates);

        client.setAdditionalneeds(data.get(0).get("additionalneeds"));

        response = Request.post(BookingEndPoints.POST_BOOKING, client);


    }
    @Then("I verify that the booking was created successfully")
    public void verifyBookingCreation() {
        // Verifica que el status code es 200
        response.then().assertThat().statusCode(200);

        // Verificar que los detalles en la respuesta coincidan con lo enviado
        response.then().assertThat().body("booking.firstname", equalTo(client.getFirstname()))
                .body("booking.lastname", equalTo(client.getLastname()))
                .body("booking.totalprice", equalTo(client.getTotalprice()))
                .body("booking.depositpaid", equalTo(client.getDepositpaid()))
                .body("booking.bookingdates.checkin", equalTo(client.getBookingdates().getCheckin()))
                .body("booking.bookingdates.checkout", equalTo(client.getBookingdates().getCheckout()))
                .body("booking.additionalneeds", equalTo(client.getAdditionalneeds()));
    }

    @When("I get the created booking by ID")
    public void getCreatedBookingById() {
        response = Request.getWithId(BookingEndPoints.GET_BOOKING, String.valueOf(bookingId));
    }

    @Then("I verify that the booking details are correct")
    public void verifyBookingDetails() {
        response.then().assertThat().statusCode(200);

        response.then().assertThat()
                .body("firstname", equalTo(client.getFirstname()))
                .body("lastname", equalTo(client.getLastname()))
                .body("totalprice", equalTo(client.getTotalprice()))
                .body("depositpaid", equalTo(client.getDepositpaid()))
                .body("bookingdates.checkin", equalTo(client.getBookingdates().getCheckin()))
                .body("bookingdates.checkout", equalTo(client.getBookingdates().getCheckout()))
                .body("additionalneeds", equalTo(client.getAdditionalneeds()));
    }

    @When("I create a new booking with the following details")
    public void createNewBooking(DataTable bookingDetails) throws io.cucumber.core.internal.com.fasterxml.jackson.core.JsonProcessingException {
        List<Map<String, String>> data = bookingDetails.asMaps(String.class, String.class);

        client.setFirstname(data.get(0).get("firstname"));
        client.setLastname(data.get(0).get("lastname"));
        client.setTotalprice(Integer.parseInt(data.get(0).get("totalprice")));
        client.setDepositpaid(Boolean.parseBoolean(data.get(0).get("depositpaid")));

        ClientRB.BookingDates bookingDates = new ClientRB.BookingDates();
        bookingDates.setCheckin(data.get(0).get("checkin"));
        bookingDates.setCheckout(data.get(0).get("checkout"));
        client.setBookingdates(bookingDates);

        client.setAdditionalneeds(data.get(0).get("additionalneeds"));

        response = Request.post(BookingEndPoints.POST_BOOKING, client);

        response.then().assertThat().statusCode(200);

        bookingId = response.jsonPath().getInt("bookingid");
    }
}

