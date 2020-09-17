package de.davelee.personalman.server.rest.controllers;

import de.davelee.personalman.api.AbsenceRequest;
import de.davelee.personalman.api.UserRequest;
import io.restassured.RestAssured;
import org.apache.http.HttpStatus;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * Test cases for the PersonalMan REST API.
 * @author Dave Lee
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PersonalManRestControllerTest {

    @Value("${local.server.port}")
    private int port;

    @Before
    /**
     * Before starting the tests, ensure that the port is configured successfully.
     */
    public void setUp() {
        RestAssured.port = port;
    }

    @Test
    /**
     * Test case: add a user to the system based on a valid user request.
     * Expected Result: user added successfully.
     */
    public void testValidAdd() {
        //Add user so that test is successfully.
        UserRequest validUserRequest = generateValidUserRequest();
        assertEquals("David", validUserRequest.getFirstName());
        given()
                .contentType("application/json")
                .body(validUserRequest)
                .when()
                .post("/personalman/user")
                .then()
                .statusCode(HttpStatus.SC_CREATED);
        //Do actual test.
        AbsenceRequest validAbsenceRequest = generateValidAbsenceRequest();
        given()
                .contentType("application/json")
                .body(validAbsenceRequest)
                .when()
                .post("/personalman/absences")
                .then()
                .statusCode(HttpStatus.SC_CREATED);
        //Delete user.
        when()
                .delete("/personalman/user?company=MyCompany&username=dlee")
                .then()
                .statusCode(HttpStatus.SC_OK);
    }

    @Test
    /**
     * Test case: add an absence with an invalid date.
     * Expected Result: bad request.
     */
    public void testInvalidDate() {
        AbsenceRequest validAbsenceRequest = AbsenceRequest.builder()
                .company("MyCompany")
                .username("dlee")
                .startDate("30-11-2016")
                .endDate("30-11")
                .category("Holiday")
                .build();
        assertEquals("30-11-2016", validAbsenceRequest.getStartDate());
        given()
                .contentType("application/json")
                .body(validAbsenceRequest)
                .when()
                .post("/personalman/absences")
                .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST);
    }

    @Test
    /**
     * Test case: add an absence with an end date before the start date.
     * Expected Result: bad request.
     */
    public void testEndDateBeforeStartDate() {
        AbsenceRequest validAbsenceRequest = AbsenceRequest.builder()
                .company("MyCompany")
                .username("dlee")
                .startDate("30-11-2017")
                .endDate("30-11-2016")
                .category("Holiday")
                .build();
        assertEquals("30-11-2017", validAbsenceRequest.getStartDate());
        given()
                .contentType("application/json")
                .body(validAbsenceRequest)
                .when()
                .post("/personalman/absences")
                .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST);
    }

    @Test
    /**
     * Test case: add an absence without specifying the category.
     * Expected Result: bad request.
     */
    public void testMissingCategory() {
        AbsenceRequest validAbsenceRequest = AbsenceRequest.builder()
                .company("MyCompany")
                .username("dlee")
                .startDate("30-11-2016")
                .endDate("30-11-2016")
                .category("")
                .build();
        assertEquals("", validAbsenceRequest.getCategory());
        given()
                .contentType("application/json")
                .body(validAbsenceRequest)
                .when()
                .post("/personalman/absences")
                .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST);
    }

    @Test
    /**
     * Test case: add an absence without specifying a company.
     * Expected Result: bad request.
     */
    public void testMissingCompany() {
        AbsenceRequest validAbsenceRequest = AbsenceRequest.builder()
                .company("")
                .username("dlee")
                .startDate("30-11-2016")
                .endDate("30-11-2016")
                .category("Holiday")
                .build();
        assertEquals("", validAbsenceRequest.getCompany());
        given()
                .contentType("application/json")
                .body(validAbsenceRequest)
                .when()
                .post("/personalman/absences")
                .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST);
    }

    @Test
    /**
     * Test case: add an absence without an end date.
     * Expected Result: bad request.
     */
    public void testMissingEndDate() {
        AbsenceRequest validAbsenceRequest = AbsenceRequest.builder()
                .company("MyCompany")
                .username("dlee")
                .startDate("30-11-2016")
                .endDate("")
                .category("Holiday")
                .build();
        assertEquals("", validAbsenceRequest.getEndDate());
        given()
                .contentType("application/json")
                .body(validAbsenceRequest)
                .when()
                .post("/personalman/absences")
                .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST);
    }

    @Test
    /**
     * Test case: add an absence without a start date.
     * Expected Result: bad request.
     */
    public void testMissingStartDate() {
        AbsenceRequest validAbsenceRequest = AbsenceRequest.builder()
                .company("MyCompany")
                .username("dlee")
                .startDate("")
                .endDate("30-11-2016")
                .category("Holiday")
                .build();
        assertEquals("", validAbsenceRequest.getStartDate());
        given()
                .contentType("application/json")
                .body(validAbsenceRequest)
                .when()
                .post("/personalman/absences")
                .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST);
    }

    @Test
    /**
     * Test case: add an absence without a username.
     * Expected Result: bad request.
     */
    public void testMissingUsername() {
        AbsenceRequest validAbsenceRequest = AbsenceRequest.builder()
                .company("MyCompany")
                .username("")
                .startDate("30-11-2016")
                .endDate("30-11-2016")
                .category("Holiday")
                .build();
        assertEquals("", validAbsenceRequest.getUsername());
        given()
                .contentType("application/json")
                .body(validAbsenceRequest)
                .when()
                .post("/personalman/absences")
                .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST);
    }

    @Test
    /**
     * Test case: find absences for a particular company.
     * Expected Result: status ok.
     */
    public void testValidFindAbsencesWithoutUsername() {
        when()
                .get("/personalman/absences?company=MyCompany&startDate=30-11-2016&endDate=30-11-2016")
                .then()
                .statusCode(HttpStatus.SC_OK);
    }

    @Test
    /**
     * Test case: find absences for a particular company and username.
     * Expected Result: status ok.
     */
    public void testValidFindAbsencesWithUsername() {
        when()
                .get("/personalman/absences?company=MyCompany&startDate=30-11-2016&endDate=30-11-2016&username=dlee")
                .then()
                .statusCode(HttpStatus.SC_OK);
    }

    @Test
    /**
     * Test case: find absences for a particular company without specifying a date range.
     * Expected Result: bad request.
     */
    public void testMissingDatesFindAbsences() {
        when()
                .get("/personalman/absences?company=MyCompany")
                .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST);
    }

    @Test
    /**
     * Test case: find absences for a particular company with an end date before the start date.
     * Expected Result: bad request.
     */
    public void testEndDateBeforeStartDateFindAbsences() {
        when()
                .get("/personalman/absences?company=MyCompany&startDate=30-11-2017&endDate=30-11-2016")
                .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST);
    }

    @Test
    /**
     * Test case: count absences for a particular company.
     * Expected Result: status ok and count 0.
     */
    public void testCountAbsencesWithoutUsername() {
        when()
                .get("/personalman/absences?company=MyCompany&startDate=30-11-2016&endDate=30-11-2016&category=Holiday&onlyCount=true")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body("count", equalTo(0));
    }

    @Test
    /**
     * Test case: find absences for a particular company and username.
     * Expected Result: status ok.
     */
    public void testValidCountAbsencesWithUsername() {
        when()
                .get("/personalman/absences?company=MyCompany&startDate=30-11-2016&endDate=30-11-2016&username=dlee&category=Holiday&onlyCount=true")
                .then()
                .statusCode(HttpStatus.SC_OK);
    }

    @Test
    /**
     * Test case: count absences for a particular company and username where there are no known absences.
     * Expected Result: status ok and 0.
     */
    public void testValidCountAbsencesWithUsernameWhenNoAbsences() {
        when()
                .get("/personalman/absences?company=MyCompany&startDate=30-11-2016&endDate=30-11-2016&username=dlee&category=Illness&onlyCount=true")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body("count", equalTo(0));
    }

    @Test
    /**
     * Test case: count absences for a particular company without specifying a date range.
     * Expected Result: bad request.
     */
    public void testMissingDateCountAbsences() {
        when()
                .get("/personalman/absences?company=MyCompany&onlyCount=true")
                .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST);
    }

    @Test
    /**
     * Test case: count absences for a particular company with an end date before the start date.
     * Expected Result: bad request.
     */
    public void testEndDateBeforeStartDateCountAbsences() {
        when()
                .get("/personalman/absences?company=MyCompany&startDate=30-11-2017&endDate=30-11-2016&onlyCount=true")
                .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST);
    }

    @Test
    /**
     * Test case: count absences for a particular company without specifying a category.
     * Expected Result: bad request.
     */
    public void testMissingCategoryCountAbsence() {
        when()
                .get("/personalman/absences?company=MyCompany&startDate=30-11-2016&endDate=30-11-2016&username=dlee&onlyCount=true")
                .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST);
    }

    @Test
    /**
     * Test case: find absences for a particular company with an invalid category.
     * Expected Result: bad request.
     */
    public void testInvalidCategoryCountAbsence() {
        when()
                .get("/personalman/absences?company=MyCompany&startDate=30-11-2016&endDate=30-11-2016&username=dlee&category=MyHoliday&onlyCount=true")
                .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST);
    }

    @Test
    /**
     * Test case: delete absences for a particular company.
     * Expected Result: status ok.
     */
    public void testValidDeleteAbsencesWithoutUsername() {
        when()
                .delete("/personalman/absences?company=MyCompany&startDate=30-11-2016&endDate=30-11-2016")
                .then()
                .statusCode(HttpStatus.SC_OK);
    }

    @Test
    /**
     * Test case: delete absences for a particular company and username.
     * Expected Result: status ok.
     */
    public void testValidDeleteAbsencesWithUsername() {
        when()
                .delete("/personalman/absences?company=MyCompany&startDate=30-11-2016&endDate=30-11-2016&username=dlee")
                .then()
                .statusCode(HttpStatus.SC_OK);
    }

    @Test
    /**
     * Test case: delete absences for a particular company without specifying a date range.
     * Expected Result: bad request.
     */
    public void testMissingDatesDeleteAbsences() {
        when()
                .delete("/personalman/absences?company=MyCompany")
                .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST);
    }

    @Test
    /**
     * Test case: delete absences for a particular company with an end date before the start date.
     * Expected Result: bad request.
     */
    public void testEndDateBeforeStartDateDeleteAbsences() {
        when()
                .delete("/personalman/absences?company=MyCompany&startDate=30-11-2017&endDate=30-11-2016")
                .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST);
    }

    @Test
    /**
     * Test case: add a user to the system based on a valid user request.
     * Expected Result: user created successfully.
     */
     public void testValidUser() {
        UserRequest validUserRequest = generateValidUserRequest();
        assertEquals("David", validUserRequest.getFirstName());
        given()
                .contentType("application/json")
                .body(validUserRequest)
                .when()
                .post("/personalman/user")
                .then()
                .statusCode(HttpStatus.SC_CREATED);
    }

    @Test
    /**
     * Test case: Attempt to add a user to the system with a negative amount of annual leave.
     * Expected Result: bad request.
     */
    public void testUserInvalidLeave() {
        UserRequest validUserRequest = UserRequest.builder()
                .firstName("David")
                .surname("Lee")
                .username("dlee")
                .company("MyCompany")
                .leaveEntitlementPerYear(-1)
                .position("Tester")
                .startDate("01-12-2016")
                .workingDays("Monday,Tuesday,Wednesday")
                .build();
        assertEquals(-1, validUserRequest.getLeaveEntitlementPerYear());
        given()
                .contentType("application/json")
                .body(validUserRequest)
                .when()
                .post("/personalman/user")
                .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST);
    }

    @Test
    /**
     * Test case: attempt to add a user to the system with an invalid start date.
     * Expected Result: bad request.
     */
    public void testUserInvalidStartDate() {
        UserRequest validUserRequest = UserRequest.builder()
                .firstName("David")
                .surname("Lee")
                .username("dlee")
                .company("MyCompany")
                .leaveEntitlementPerYear(-1)
                .position("Tester")
                .startDate("33-11-2016")
                .workingDays("Monday,Tuesday,Wednesday")
                .build();
        assertEquals("33-11-2016", validUserRequest.getStartDate());
        given()
                .contentType("application/json")
                .body(validUserRequest)
                .when()
                .post("/personalman/user")
                .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST);
    }

    @Test
    /**
     * Test case: attempt to add a user to the system with no first name.
     * Expected Result: bad request.
     */
    public void testUserMissingFirstName() {
        UserRequest validUserRequest = UserRequest.builder()
                .surname("Lee")
                .username("dlee")
                .company("MyCompany")
                .leaveEntitlementPerYear(-1)
                .position("Tester")
                .startDate("01-12-2016")
                .workingDays("Monday,Tuesday,Wednesday")
                .build();
        assertNull(validUserRequest.getFirstName());
        given()
                .contentType("application/json")
                .body(validUserRequest)
                .when()
                .post("/personalman/user")
                .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST);
    }

    @Test
    /**
     * Test case: attempt to add a user to the system without leave entitlement.
     * Expected Result: bad request.
     */
    public void testUserMissingLeaveEntitlement() {
        UserRequest validUserRequest = UserRequest.builder()
                .firstName("David")
                .surname("Lee")
                .username("dlee")
                .company("MyCompany")
                .position("Tester")
                .startDate("01-12-2016")
                .workingDays("Monday,Tuesday,Wednesday")
                .build();
        assertEquals(0, validUserRequest.getLeaveEntitlementPerYear());
        given()
                .contentType("application/json")
                .body(validUserRequest)
                .when()
                .post("/personalman/user")
                .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST);
    }

    @Test
    /**
     * Test case: attempt to add a user to the system with no job title.
     * Expected Result: bad request.
     */
    public void testUserMissingPosition() {
        UserRequest validUserRequest = UserRequest.builder()
                .firstName("David")
                .surname("Lee")
                .username("dlee")
                .company("MyCompany")
                .leaveEntitlementPerYear(-1)
                .startDate("01-12-2016")
                .workingDays("Monday,Tuesday,Wednesday")
                .build();
        assertNull(validUserRequest.getPosition());
        given()
                .contentType("application/json")
                .body(validUserRequest)
                .when()
                .post("/personalman/user")
                .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST);
    }

    @Test
    /**
     * Test case: attempt to add a user to the system with no start date.
     * Expected Result: bad request.
     */
    public void testUserMissingStartDate() {
        UserRequest validUserRequest = UserRequest.builder()
                .firstName("David")
                .surname("Lee")
                .username("dlee")
                .company("MyCompany")
                .leaveEntitlementPerYear(-1)
                .position("Tester")
                .workingDays("Monday,Tuesday,Wednesday")
                .build();
        assertNull(validUserRequest.getStartDate());
        given()
                .contentType("application/json")
                .body(validUserRequest)
                .when()
                .post("/personalman/user")
                .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST);
    }

    @Test
    /**
     * Test case: attempt to add a user to the system with no last name.
     * Expected Result: bad request.
     */
    public void testUserMissingSurname() {
        UserRequest validUserRequest = UserRequest.builder()
                .firstName("David")
                .username("dlee")
                .company("MyCompany")
                .leaveEntitlementPerYear(-1)
                .position("Tester")
                .startDate("01-12-2016")
                .workingDays("Monday,Tuesday,Wednesday")
                .build();
        assertNull(validUserRequest.getSurname());
        given()
                .contentType("application/json")
                .body(validUserRequest)
                .when()
                .post("/personalman/user")
                .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST);
    }

    @Test
    /**
     * Test case: attempt to add a user to the system with no username.
     * Expected Result: bad request.
     */
     public void testUserMissingUsername() {
        UserRequest validUserRequest = UserRequest.builder()
                .firstName("David")
                .surname("Lee")
                .company("MyCompany")
                .leaveEntitlementPerYear(-1)
                .position("Tester")
                .startDate("01-12-2016")
                .workingDays("Monday,Tuesday,Wednesday")
                .build();
        assertNull(validUserRequest.getUsername());
        given()
                .contentType("application/json")
                .body(validUserRequest)
                .when()
                .post("/personalman/user")
                .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST);
    }

    @Test
    /**
     * Test case: attempt to add a user to the system with no company.
     * Expected Result: bad request.
     */
    public void testUserMissingCompany() {
        UserRequest validUserRequest = UserRequest.builder()
                .firstName("David")
                .surname("Lee")
                .username("dlee")
                .leaveEntitlementPerYear(-1)
                .position("Tester")
                .startDate("01-12-2016")
                .workingDays("Monday,Tuesday,Wednesday")
                .build();
        assertNull(validUserRequest.getCompany());
        given()
                .contentType("application/json")
                .body(validUserRequest)
                .when()
                .post("/personalman/user")
                .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST);
    }

    @Test
    /**
     * Test case: attempt to add a user to the system with no working days.
     * Expected Result: bad request.
     */
    public void testUserMissingWorkingDays() {
        UserRequest validUserRequest = UserRequest.builder()
                .firstName("David")
                .surname("Lee")
                .username("dlee")
                .company("MyCompany")
                .leaveEntitlementPerYear(-1)
                .position("Tester")
                .startDate("01-12-2016")
                .build();
        assertNull(validUserRequest.getWorkingDays());
        given()
                .contentType("application/json")
                .body(validUserRequest)
                .when()
                .post("/personalman/user")
                .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST);
    }

    @Test
    /**
     * Test case: attempt to find users for a company which has no users.
     * Expected Result: no content.
     */
    public void testValidFindUsersNotFound() {
        when()
                .get("/personalman/users?company=MyNoCompany")
                .then()
                .statusCode(HttpStatus.SC_NO_CONTENT);
    }

    @Test
    /**
     * Test case: attempt to find users without specifying a company.
     * Expected Result: bad request.
     */
    public void testInvalidFindUsers() {
        when()
                .get("/personalman/users")
                .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST);
    }

    @Test
    /**
     * Test case: attempt to find a user based on company & username, company & then delete a user based on username and company.
     * Expected Result: status ok.
     */
     public void testValidFindUserAndThenFindUsersAndThenDelete() {
        when()
                .get("/personalman/user?company=MyCompany&username=dlee")
                .then()
                .statusCode(HttpStatus.SC_OK);
        when()
                .get("/personalman/users?company=MyCompany")
                .then()
                .statusCode(HttpStatus.SC_OK);
        when()
                .delete("/personalman/user?company=MyCompany&username=dlee")
                .then()
                .statusCode(HttpStatus.SC_OK);
    }

    @Test
    /**
     * Test case: no user exists with the specified company and username.
     * Expected Result: no content.
     */
    public void testValidFindUserNotFound() {
        when()
                .get("/personalman/user?company=MyCompany&username=mlee")
                .then()
                .statusCode(HttpStatus.SC_NO_CONTENT);
    }

    @Test
    /**
     * Test case: attempt to find a user without specifying company or username.
     * Expected Result: bad request.
     */
    public void testInvalidFindUser() {
        when()
                .get("/personalman/user")
                .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST);
    }

    @Test
    /**
     * Test case: attempt to find a user without specifying username.
     * Expected Result: bad request.
     */
    public void testInvalidFindUserWithOnlyCompany() {
        when()
                .get("/personalman/user?company=MyCompany")
                .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST);
    }

    @Test
    /**
     * Test case: attempt to delete a user which does not exist.
     * Expected Result: no content.
     */
    public void testValidDeleteUserNotFound() {
        when()
                .delete("/personalman/user?company=MyCompany&username=mlee")
                .then()
                .statusCode(HttpStatus.SC_NO_CONTENT);
    }

    @Test
    /**
     * Test case: attempt to delete a user without specifying a username.
     * Expected Result: bad request.
     */
    public void testInvalidDeleteUser() {
        when()
                .delete("/personalman/user")
                .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST);
    }

    @Test
    /**
     * Test case: check the swagger ui works.
     * Expected Result: status ok.
     */
    public void testSwagger() {
        when()
                .get("/swagger-ui/")
                .then()
                .statusCode(200);
    }

    /**
     * Private helper method to generate a valid user request.
     * @return a <code>UserRequest</code> object containing valid test data.
     */
    private UserRequest generateValidUserRequest( ) {
        return UserRequest.builder()
                .firstName("David")
                .surname("Lee")
                .username("dlee")
                .company("MyCompany")
                .leaveEntitlementPerYear(16)
                .position("Tester")
                .startDate("01-12-2016")
                .workingDays("Monday,Tuesday,Wednesday")
                .build();
    }

    /**
     * Private helper method to generate a valid absence request.
     * @return a <code>AbsenceRequest</code> object containing valid test data.
     */
    private AbsenceRequest generateValidAbsenceRequest( ) {
        return AbsenceRequest.builder()
                .company("MyCompany")
                .username("dlee")
                .startDate("30-11-2016")
                .endDate("30-11-2016")
                .category("Holiday")
                .build();
    }

}
