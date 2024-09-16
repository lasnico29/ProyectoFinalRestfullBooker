Feature: Booking Endpoint
  Background: Booking endopoints should allow to get and post clients

    Scenario: /booking/(any id that exists) should return the specific client
      Given I perform a GET call to the booking endpoint with id "68"
      Then I verify that the status code is 200
      And I verify that the body does not have size 0

      Scenario Outline: /booking/(invalidCharacter) should return status code 400
        Given  I perform a GET call to the booking endpoint with this invalid character "<invalidCharacter>"
        #probar con 404 y 400 y si funciona
        Then I verify that the status code is 400
        Examples:
          | invalidCharacter |
          | 1abc             |
          | %%               |
          | 1%%              |
          | abc1             |
          | abc%%            |

  Scenario: Create a booking and verify the details
    When I create a booking with the following details
      | firstname | lastname | totalprice | depositpaid | checkin    | checkout   | additionalneeds |
      | Nicolas   | Moscoso  | 1230       | true        | 2019-01-01 | 2020-02-03 | desayuno        |
    Then I verify that the booking was created successfully

  Scenario: Create a booking and verify the details with a GET call
    When I create a new booking with the following details
      | firstname | lastname | totalprice | depositpaid | checkin    | checkout   | additionalneeds |
      | Nicolas   | Moscoso  | 1230       | true        | 2019-01-01 | 2020-02-03 | desayuno        |
    And I get the created booking by ID
    Then I verify that the booking details are correct

  Scenario: Create a booking and verify the status code 400 if firstname is not completed
    When I create a booking with the following details
      | firstname | lastname | totalprice | depositpaid | checkin    | checkout   | additionalneeds |
      |           | Moscoso  | 1230       | true        | 2019-01-01 | 2020-02-03 | desayuno        |
    Then I verify that the status code is 400