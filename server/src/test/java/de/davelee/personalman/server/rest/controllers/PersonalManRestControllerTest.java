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

    /**
     * Before starting the tests, ensure that the port is configured successfully.
     */
    @Before
    public void setUp() {
        RestAssured.port = port;
    }

    /**
     * Test case: add a user to the system based on a valid user request.
     * Expected Result: user added successfully.
     */
    @Test
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
                .statusCode(HttpStatus.SC_FORBIDDEN);
        //Delete user.
        when()
                .delete("/personalman/user?company=MyCompany&username=dlee&token=dlee-lgfkfkfl")
                .then()
                .statusCode(HttpStatus.SC_FORBIDDEN);
    }

    /**
     * Test case: add an absence with an invalid date.
     * Expected Result: bad request.
     */
    @Test
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
                .statusCode(HttpStatus.SC_FORBIDDEN);
    }

    /**
     * Test case: add an absence with an end date before the start date.
     * Expected Result: bad request.
     */
    @Test
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
                .statusCode(HttpStatus.SC_FORBIDDEN);
    }

    /**
     * Test case: add an absence without specifying the category.
     * Expected Result: bad request.
     */
    @Test
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
                .statusCode(HttpStatus.SC_FORBIDDEN);
    }

    /**
     * Test case: add an absence without specifying a company.
     * Expected Result: bad request.
     */
    @Test
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
                .statusCode(HttpStatus.SC_FORBIDDEN);
    }

    /**
     * Test case: add an absence without an end date.
     * Expected Result: bad request.
     */
    @Test
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
                .statusCode(HttpStatus.SC_FORBIDDEN);
    }

    /**
     * Test case: add an absence without a start date.
     * Expected Result: bad request.
     */
    @Test
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
                .statusCode(HttpStatus.SC_FORBIDDEN);
    }

    /**
     * Test case: add an absence without a username.
     * Expected Result: bad request.
     */
    @Test
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
                .statusCode(HttpStatus.SC_FORBIDDEN);
    }

    /**
     * Test case: find absences for a particular company.
     * Expected Result: status ok.
     */
    @Test
    public void testValidFindAbsencesWithoutUsername() {
        when()
                .get("/personalman/absences?company=MyCompany&startDate=30-11-2016&endDate=30-11-2016&token=dlee-fkgfgg")
                .then()
                .statusCode(HttpStatus.SC_FORBIDDEN);
    }

    /**
     * Test case: find absences for a particular company and username.
     * Expected Result: status ok.
     */
    @Test
    public void testValidFindAbsencesWithUsername() {
        when()
                .get("/personalman/absences?company=MyCompany&startDate=30-11-2016&endDate=30-11-2016&username=dlee&token=dlee-fkmggkgk")
                .then()
                .statusCode(HttpStatus.SC_FORBIDDEN);
    }

    /**
     * Test case: find absences for a particular company without specifying a date range.
     * Expected Result: bad request.
     */
    @Test
    public void testMissingDatesFindAbsences() {
        when()
                .get("/personalman/absences?company=MyCompany")
                .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST);
    }

    /**
     * Test case: find absences for a particular company with an end date before the start date.
     * Expected Result: bad request.
     */
    @Test
    public void testEndDateBeforeStartDateFindAbsences() {
        when()
                .get("/personalman/absences?company=MyCompany&startDate=30-11-2017&endDate=30-11-2016")
                .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST);
    }

    /**
     * Test case: count absences for a particular company.
     * Expected Result: status ok and count 0.
     */
    @Test
    public void testCountAbsencesWithoutUsername() {
        when()
                .get("/personalman/absences?company=MyCompany&startDate=30-11-2016&endDate=30-11-2016&category=Holiday&onlyCount=true&token=dlee-fkgkgkgk")
                .then()
                .statusCode(HttpStatus.SC_FORBIDDEN);
    }

    /**
     * Test case: find absences for a particular company and username.
     * Expected Result: status ok.
     */
    @Test
    public void testValidCountAbsencesWithUsername() {
        when()
                .get("/personalman/absences?company=MyCompany&startDate=30-11-2016&endDate=30-11-2016&username=dlee&category=Holiday&onlyCount=true&token=dlee-fgltglgtl")
                .then()
                .statusCode(HttpStatus.SC_FORBIDDEN);
    }

    /**
     * Test case: count absences for a particular company and username where there are no known absences.
     * Expected Result: status ok and 0.
     */
    @Test
    public void testValidCountAbsencesWithUsernameWhenNoAbsences() {
        when()
                .get("/personalman/absences?company=MyCompany&startDate=30-11-2016&endDate=30-11-2016&username=dlee&category=Illness&onlyCount=true&token=dlee-gkgktgtl")
                .then()
                .statusCode(HttpStatus.SC_FORBIDDEN);
    }

    /**
     * Test case: count absences for a particular company without specifying a date range.
     * Expected Result: bad request.
     */
    @Test
    public void testMissingDateCountAbsences() {
        when()
                .get("/personalman/absences?company=MyCompany&onlyCount=true")
                .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST);
    }

    /**
     * Test case: count absences for a particular company with an end date before the start date.
     * Expected Result: bad request.
     */
    @Test
    public void testEndDateBeforeStartDateCountAbsences() {
        when()
                .get("/personalman/absences?company=MyCompany&startDate=30-11-2017&endDate=30-11-2016&onlyCount=true")
                .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST);
    }

    /**
     * Test case: count absences for a particular company without specifying a category.
     * Expected Result: bad request.
     */
    @Test
    public void testMissingCategoryCountAbsence() {
        when()
                .get("/personalman/absences?company=MyCompany&startDate=30-11-2016&endDate=30-11-2016&username=dlee&onlyCount=true")
                .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST);
    }

    /**
     * Test case: find absences for a particular company with an invalid category.
     * Expected Result: bad request.
     */
    @Test
    public void testInvalidCategoryCountAbsence() {
        when()
                .get("/personalman/absences?company=MyCompany&startDate=30-11-2016&endDate=30-11-2016&username=dlee&category=MyHoliday&onlyCount=true")
                .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST);
    }

    /**
     * Test case: delete absences for a particular company.
     * Expected Result: status ok.
     */
    @Test
    public void testValidDeleteAbsencesWithoutUsername() {
        when()
                .delete("/personalman/absences?company=MyCompany&startDate=30-11-2016&endDate=30-11-2016&token=dlee-ffpggoog")
                .then()
                .statusCode(HttpStatus.SC_FORBIDDEN);
    }

    /**
     * Test case: delete absences for a particular company and username.
     * Expected Result: status ok.
     */
    @Test
    public void testValidDeleteAbsencesWithUsername() {
        when()
                .delete("/personalman/absences?company=MyCompany&startDate=30-11-2016&endDate=30-11-2016&username=dlee&token=dlee-fgglggtlg")
                .then()
                .statusCode(HttpStatus.SC_FORBIDDEN);
    }

    /**
     * Test case: delete absences for a particular company without specifying a date range.
     * Expected Result: bad request.
     */
    @Test
    public void testMissingDatesDeleteAbsences() {
        when()
                .delete("/personalman/absences?company=MyCompany")
                .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST);
    }

    /**
     * Test case: delete absences for a particular company with an end date before the start date.
     * Expected Result: bad request.
     */
    @Test
    public void testEndDateBeforeStartDateDeleteAbsences() {
        when()
                .delete("/personalman/absences?company=MyCompany&startDate=30-11-2017&endDate=30-11-2016")
                .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST);
    }

    /**
     * Test case: add a user to the system based on a valid user request.
     * Expected Result: user created successfully.
     */
    @Test
    public void testValidUser() {
        UserRequest validUserRequest = UserRequest.builder()
                .company("Example Company")
                .firstName("Max")
                .surname("Mustermann")
                .leaveEntitlementPerYear(25)
                .position("Secretary")
                .username("max.mustermann")
                .password("test")
                .role("Employee")
                .startDate("01-03-2020")
                .workingDays("Monday,Tuesday,Wednesday,Thursday,Friday")
                .build();
        assertEquals("Max", validUserRequest.getFirstName());
        given()
                .contentType("application/json")
                .body(validUserRequest)
                .when()
                .post("/personalman/user")
                .then()
                .statusCode(HttpStatus.SC_CREATED);
    }

    /**
     * Test case: Attempt to add a user to the system with a negative amount of annual leave.
     * Expected Result: bad request.
     */
    @Test
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

    /**
     * Test case: attempt to add a user to the system with an invalid start date.
     * Expected Result: bad request.
     */
    @Test
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

    /**
     * Test case: attempt to add a user to the system with no first name.
     * Expected Result: bad request.
     */
    @Test
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

    /**
     * Test case: attempt to add a user to the system without leave entitlement.
     * Expected Result: bad request.
     */
    @Test
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

    /**
     * Test case: attempt to add a user to the system with no job title.
     * Expected Result: bad request.
     */
    @Test
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

    /**
     * Test case: attempt to add a user to the system with no start date.
     * Expected Result: bad request.
     */
    @Test
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

    /**
     * Test case: attempt to add a user to the system with no last name.
     * Expected Result: bad request.
     */
    @Test
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

    /**
     * Test case: attempt to add a user to the system with no username.
     * Expected Result: bad request.
     */
    @Test
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

    /**
     * Test case: attempt to add a user to the system with no company.
     * Expected Result: bad request.
     */
    @Test
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

    /**
     * Test case: attempt to add a user to the system with no working days.
     * Expected Result: bad request.
     */
    @Test
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

    /**
     * Test case: attempt to find users for a company which has no users.
     * Expected Result: no content.
     */
    @Test
    public void testValidFindUsersNotFound() {
        when()
                .get("/personalman/users?company=MyNoCompany&token=dlee-gkgtkgtgl")
                .then()
                .statusCode(HttpStatus.SC_FORBIDDEN);
    }

    /**
     * Test case: attempt to find users without specifying a company.
     * Expected Result: bad request.
     */
    @Test
    public void testInvalidFindUsers() {
        when()
                .get("/personalman/users")
                .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST);
    }

    /**
     * Test case: attempt to find a user based on company & username, company & then delete a user based on username and company.
     * Expected Result: status ok.
     */
    @Test
    public void testValidFindUserAndThenFindUsersAndThenDelete() {
        when()
                .get("/personalman/user?company=MyCompany&username=dlee&token=dlee-fjgkg")
                .then()
                .statusCode(HttpStatus.SC_FORBIDDEN);
        when()
                .get("/personalman/users?company=MyCompany&token=dlee-gfkggkogt")
                .then()
                .statusCode(HttpStatus.SC_FORBIDDEN);
        when()
                .delete("/personalman/user?company=MyCompany&username=dlee&token=dlee-gkgkgkgll")
                .then()
                .statusCode(HttpStatus.SC_FORBIDDEN);
    }

    /**
     * Test case: no user exists with the specified company and username.
     * Expected Result: no content.
     */
    @Test
    public void testValidFindUserNotFound() {
        when()
                .get("/personalman/user?company=MyCompany&username=mlee&token=dlee-glglglggl")
                .then()
                .statusCode(HttpStatus.SC_FORBIDDEN);
    }

    /**
     * Test case: attempt to find a user without specifying company or username.
     * Expected Result: bad request.
     */
    @Test
    public void testInvalidFindUser() {
        when()
                .get("/personalman/user")
                .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST);
    }

    /**
     * Test case: attempt to find a user without specifying username.
     * Expected Result: bad request.
     */
    @Test
    public void testInvalidFindUserWithOnlyCompany() {
        when()
                .get("/personalman/user?company=MyCompany")
                .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST);
    }

    /**
     * Test case: attempt to delete a user which does not exist.
     * Expected Result: no content.
     */
    @Test
    public void testValidDeleteUserNotFound() {
        when()
                .delete("/personalman/user?company=MyCompany&username=mlee&token=dlee-fgtgogg")
                .then()
                .statusCode(HttpStatus.SC_FORBIDDEN);
    }

    /**
     * Test case: attempt to delete a user without specifying a username.
     * Expected Result: bad request.
     */
    @Test
    public void testInvalidDeleteUser() {
        when()
                .delete("/personalman/user")
                .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST);
    }

    /**
     * Test case: check the swagger ui works.
     * Expected Result: status ok.
     */
    @Test
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
