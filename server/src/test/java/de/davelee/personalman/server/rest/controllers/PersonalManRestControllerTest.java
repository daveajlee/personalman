package de.davelee.personalman.server.rest.controllers;

import com.jayway.restassured.RestAssured;
import de.davelee.personalman.api.AbsenceRequest;
import de.davelee.personalman.api.UserRequest;
import org.apache.http.HttpStatus;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.RestAssured.when;
import static org.hamcrest.Matchers.equalTo;

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
        UserRequest validUserRequest = new UserRequest();
        validUserRequest.setFirstName("David");
        validUserRequest.setLeaveEntitlementPerYear(16);
        validUserRequest.setPosition("Tester");
        validUserRequest.setStartDate("01-12-2016");
        validUserRequest.setSurname("Lee");
        validUserRequest.setUsername("dlee");
        validUserRequest.setCompany("MyCompany");
        validUserRequest.setWorkingDays("Monday,Tuesday,Wednesday");
        assertEquals("David", validUserRequest.getFirstName());
        given()
                .contentType("application/json")
                .body(validUserRequest)
                .when()
                .post("/personalman/user")
                .then()
                .statusCode(HttpStatus.SC_CREATED);
        //Do actual test.
        AbsenceRequest validAbsenceRequest = new AbsenceRequest("MyCompany", "dlee", "30-11-2016", "30-11-2016", "Holiday");
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
        AbsenceRequest validAbsenceRequest = new AbsenceRequest("MyCompany", "dlee", "30-11-2016", "30-11", "Holiday");
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
        AbsenceRequest validAbsenceRequest = new AbsenceRequest("MyCompany", "dlee", "30-11-2017", "30-11-2016", "Holiday");
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
        AbsenceRequest validAbsenceRequest = new AbsenceRequest("MyCompany", "dlee", "30-11-2017", "30-11-2016", "");
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
        AbsenceRequest validAbsenceRequest = new AbsenceRequest("", "dlee", "30-11-2017", "30-11-2016", "Holiday");
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
        AbsenceRequest validAbsenceRequest = new AbsenceRequest("MyCompany", "dlee", "30-11-2017", "", "Holiday");
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
        AbsenceRequest validAbsenceRequest = new AbsenceRequest("MyCompany", "dlee", "", "30-11-2016", "Holiday");
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
        AbsenceRequest validAbsenceRequest = new AbsenceRequest("MyCompany", "", "30-11-2017", "30-11-2016", "Holiday");
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
        assertNotNull(when()
                .get("/personalman/absences?company=MyCompany&startDate=30-11-2016&endDate=30-11-2016")
                .then()
                .statusCode(HttpStatus.SC_OK));
    }

    @Test
    /**
     * Test case: find absences for a particular company and username.
     * Expected Result: status ok.
     */
    public void testValidFindAbsencesWithUsername() {
        assertNotNull(when()
                .get("/personalman/absences?company=MyCompany&startDate=30-11-2016&endDate=30-11-2016&username=dlee")
                .then()
                .statusCode(HttpStatus.SC_OK));
    }

    @Test
    /**
     * Test case: find absences for a particular company without specifying a date range.
     * Expected Result: bad request.
     */
    public void testMissingDatesFindAbsences() {
        assertNotNull(when()
                .get("/personalman/absences?company=MyCompany")
                .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST));
    }

    @Test
    /**
     * Test case: find absences for a particular company with an end date before the start date.
     * Expected Result: bad request.
     */
    public void testEndDateBeforeStartDateFindAbsences() {
        assertNotNull(when()
                .get("/personalman/absences?company=MyCompany&startDate=30-11-2017&endDate=30-11-2016")
                .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST));
    }

    @Test
    /**
     * Test case: count absences for a particular company.
     * Expected Result: status ok and count 0.
     */
    public void testCountAbsencesWithoutUsername() {
        assertNotNull(when()
                .get("/personalman/absences?company=MyCompany&startDate=30-11-2016&endDate=30-11-2016&category=Holiday&onlyCount=true")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body("count", equalTo(0)));
    }

    @Test
    /**
     * Test case: find absences for a particular company and username.
     * Expected Result: status ok.
     */
    public void testValidCountAbsencesWithUsername() {
        assertNotNull(when()
                .get("/personalman/absences?company=MyCompany&startDate=30-11-2016&endDate=30-11-2016&username=dlee&category=Holiday&onlyCount=true")
                .then()
                .statusCode(HttpStatus.SC_OK));
    }

    @Test
    /**
     * Test case: count absences for a particular company and username where there are no known absences.
     * Expected Result: status ok and 0.
     */
    public void testValidCountAbsencesWithUsernameWhenNoAbsences() {
        assertNotNull(when()
                .get("/personalman/absences?company=MyCompany&startDate=30-11-2016&endDate=30-11-2016&username=dlee&category=Illness&onlyCount=true")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body("count", equalTo(0)));
    }

    @Test
    /**
     * Test case: count absences for a particular company without specifying a date range.
     * Expected Result: bad request.
     */
    public void testMissingDateCountAbsences() {
        assertNotNull(when()
                .get("/personalman/absences?company=MyCompany&onlyCount=true")
                .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST));
    }

    @Test
    /**
     * Test case: count absences for a particular company with an end date before the start date.
     * Expected Result: bad request.
     */
    public void testEndDateBeforeStartDateCountAbsences() {
        assertNotNull(when()
                .get("/personalman/absences?company=MyCompany&startDate=30-11-2017&endDate=30-11-2016&onlyCount=true")
                .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST));
    }

    @Test
    /**
     * Test case: count absences for a particular company without specifying a category.
     * Expected Result: bad request.
     */
    public void testMissingCategoryCountAbsence() {
        assertNotNull(when()
                .get("/personalman/absences?company=MyCompany&startDate=30-11-2016&endDate=30-11-2016&username=dlee&onlyCount=true")
                .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST));
    }

    @Test
    /**
     * Test case: find absences for a particular company with an invalid category.
     * Expected Result: bad request.
     */
    public void testInvalidCategoryCountAbsence() {
        assertNotNull(when()
                .get("/personalman/absences?company=MyCompany&startDate=30-11-2016&endDate=30-11-2016&username=dlee&category=MyHoliday&onlyCount=true")
                .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST));
    }

    @Test
    /**
     * Test case: delete absences for a particular company.
     * Expected Result: status ok.
     */
    public void testValidDeleteAbsencesWithoutUsername() {
        assertNotNull(when()
                .delete("/personalman/absences?company=MyCompany&startDate=30-11-2016&endDate=30-11-2016")
                .then()
                .statusCode(HttpStatus.SC_OK));
    }

    @Test
    /**
     * Test case: delete absences for a particular company and username.
     * Expected Result: status ok.
     */
    public void testValidDeleteAbsencesWithUsername() {
        assertNotNull(when()
                .delete("/personalman/absences?company=MyCompany&startDate=30-11-2016&endDate=30-11-2016&username=dlee")
                .then()
                .statusCode(HttpStatus.SC_OK));
    }

    @Test
    /**
     * Test case: delete absences for a particular company without specifying a date range.
     * Expected Result: bad request.
     */
    public void testMissingDatesDeleteAbsences() {
        assertNotNull(when()
                .delete("/personalman/absences?company=MyCompany")
                .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST));
    }

    @Test
    /**
     * Test case: delete absences for a particular company with an end date before the start date.
     * Expected Result: bad request.
     */
    public void testEndDateBeforeStartDateDeleteAbsences() {
        assertNotNull(when()
                .delete("/personalman/absences?company=MyCompany&startDate=30-11-2017&endDate=30-11-2016")
                .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST));
    }

    @Test
    /**
     * Test case: add a user to the system based on a valid user request.
     * Expected Result: user created successfully.
     */
     public void testValidUser() {
        UserRequest validUserRequest = new UserRequest();
        validUserRequest.setFirstName("David");
        validUserRequest.setLeaveEntitlementPerYear(16);
        validUserRequest.setPosition("Tester");
        validUserRequest.setStartDate("01-12-2016");
        validUserRequest.setSurname("Lee");
        validUserRequest.setUsername("dlee");
        validUserRequest.setCompany("MyCompany");
        validUserRequest.setWorkingDays("Monday,Tuesday,Wednesday");
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
        UserRequest validUserRequest = new UserRequest();
        validUserRequest.setFirstName("David");
        validUserRequest.setLeaveEntitlementPerYear(-1);
        validUserRequest.setPosition("Tester");
        validUserRequest.setStartDate("01-12-2016");
        validUserRequest.setSurname("Lee");
        validUserRequest.setUsername("dlee");
        validUserRequest.setCompany("MyCompany");
        validUserRequest.setWorkingDays("Monday,Tuesday,Wednesday");
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
        UserRequest validUserRequest = new UserRequest();
        validUserRequest.setFirstName("David");
        validUserRequest.setLeaveEntitlementPerYear(16);
        validUserRequest.setPosition("Tester");
        validUserRequest.setStartDate("33-11-2016");
        validUserRequest.setSurname("Lee");
        validUserRequest.setUsername("dlee");
        validUserRequest.setCompany("MyCompany");
        validUserRequest.setWorkingDays("Monday,Tuesday,Wednesday");
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
        UserRequest validUserRequest = new UserRequest();
        validUserRequest.setLeaveEntitlementPerYear(16);
        validUserRequest.setPosition("Tester");
        validUserRequest.setStartDate("01-12-2016");
        validUserRequest.setSurname("Lee");
        validUserRequest.setUsername("dlee");
        validUserRequest.setCompany("MyCompany");
        validUserRequest.setWorkingDays("Monday,Tuesday,Wednesday");
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
        UserRequest validUserRequest = new UserRequest();
        validUserRequest.setFirstName("David");
        validUserRequest.setPosition("Tester");
        validUserRequest.setStartDate("01-12-2016");
        validUserRequest.setSurname("Lee");
        validUserRequest.setUsername("dlee");
        validUserRequest.setCompany("MyCompany");
        validUserRequest.setWorkingDays("Monday,Tuesday,Wednesday");
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
        UserRequest validUserRequest = new UserRequest();
        validUserRequest.setFirstName("David");
        validUserRequest.setLeaveEntitlementPerYear(16);
        validUserRequest.setStartDate("01-12-2016");
        validUserRequest.setSurname("Lee");
        validUserRequest.setUsername("dlee");
        validUserRequest.setCompany("MyCompany");
        validUserRequest.setWorkingDays("Monday,Tuesday,Wednesday");
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
        UserRequest validUserRequest = new UserRequest();
        validUserRequest.setFirstName("David");
        validUserRequest.setLeaveEntitlementPerYear(16);
        validUserRequest.setPosition("Tester");
        validUserRequest.setSurname("Lee");
        validUserRequest.setUsername("dlee");
        validUserRequest.setCompany("MyCompany");
        validUserRequest.setWorkingDays("Monday,Tuesday,Wednesday");
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
        UserRequest validUserRequest = new UserRequest();
        validUserRequest.setFirstName("David");
        validUserRequest.setLeaveEntitlementPerYear(16);
        validUserRequest.setPosition("Tester");
        validUserRequest.setStartDate("01-12-2016");
        validUserRequest.setUsername("dlee");
        validUserRequest.setCompany("MyCompany");
        validUserRequest.setWorkingDays("Monday,Tuesday,Wednesday");
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
        UserRequest validUserRequest = new UserRequest();
        validUserRequest.setFirstName("David");
        validUserRequest.setLeaveEntitlementPerYear(16);
        validUserRequest.setPosition("Tester");
        validUserRequest.setStartDate("01-12-2016");
        validUserRequest.setSurname("Lee");
        validUserRequest.setCompany("MyCompany");
        validUserRequest.setWorkingDays("Monday,Tuesday,Wednesday");
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
        UserRequest validUserRequest = new UserRequest();
        validUserRequest.setFirstName("David");
        validUserRequest.setLeaveEntitlementPerYear(16);
        validUserRequest.setPosition("Tester");
        validUserRequest.setStartDate("01-12-2016");
        validUserRequest.setSurname("Lee");
        validUserRequest.setUsername("dlee");
        validUserRequest.setWorkingDays("Monday,Tuesday,Wednesday");
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
        UserRequest validUserRequest = new UserRequest();
        validUserRequest.setFirstName("David");
        validUserRequest.setLeaveEntitlementPerYear(16);
        validUserRequest.setPosition("Tester");
        validUserRequest.setStartDate("01-12-2016");
        validUserRequest.setSurname("Lee");
        validUserRequest.setUsername("dlee");
        validUserRequest.setCompany("MyCompany");
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
        assertNotNull(when()
                .get("/personalman/users?company=MyNoCompany")
                .then()
                .statusCode(HttpStatus.SC_NO_CONTENT));
    }

    @Test
    /**
     * Test case: attempt to find users without specifying a company.
     * Expected Result: bad request.
     */
    public void testInvalidFindUsers() {
        assertNotNull(when()
                .get("/personalman/users")
                .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST));
    }

    @Test
    /**
     * Test case: attempt to find a user based on company & username, company & then delete a user based on username and company.
     * Expected Result: status ok.
     */
     public void testValidFindUserAndThenFindUsersAndThenDelete() {
        assertNotNull(when()
                .get("/personalman/user?company=MyCompany&username=dlee")
                .then()
                .statusCode(HttpStatus.SC_OK));
        assertNotNull(when()
                .get("/personalman/users?company=MyCompany")
                .then()
                .statusCode(HttpStatus.SC_OK));
        assertNotNull(when()
                .delete("/personalman/user?company=MyCompany&username=dlee")
                .then()
                .statusCode(HttpStatus.SC_OK));
    }

    @Test
    /**
     * Test case: no user exists with the specified company and username.
     * Expected Result: no content.
     */
    public void testValidFindUserNotFound() {
        assertNotNull(when()
                .get("/personalman/user?company=MyCompany&username=mlee")
                .then()
                .statusCode(HttpStatus.SC_NO_CONTENT));
    }

    @Test
    /**
     * Test case: attempt to find a user without specifying company or username.
     * Expected Result: bad request.
     */
    public void testInvalidFindUser() {
        assertNotNull(when()
                .get("/personalman/user")
                .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST));
    }

    @Test
    /**
     * Test case: attempt to find a user without specifying username.
     * Expected Result: bad request.
     */
    public void testInvalidFindUserWithOnlyCompany() {
        assertNotNull(when()
                .get("/personalman/user?company=MyCompany")
                .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST));
    }

    @Test
    /**
     * Test case: attempt to delete a user which does not exist.
     * Expected Result: no content.
     */
    public void testValidDeleteUserNotFound() {
        assertNotNull(when()
                .delete("/personalman/user?company=MyCompany&username=mlee")
                .then()
                .statusCode(HttpStatus.SC_NO_CONTENT));
    }

    @Test
    /**
     * Test case: attempt to delete a user without specifying a username.
     * Expected Result: bad request.
     */
    public void testInvalidDeleteUser() {
        assertNotNull(when()
                .delete("/personalman/user")
                .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST));
    }

    @Test
    /**
     * Test case: check the swagger ui works.
     * Expected Result: status ok.
     */
    public void testSwagger() {
        assertNotNull(when()
                .get("/swagger-ui.html")
                .then()
                .statusCode(200));
    }

    private void assertEquals ( final String expected, final String actual ) {
        Assertions.assertEquals(expected, actual);
    }

    private void assertEquals ( final int expected, final int actual ) {
        Assertions.assertEquals(expected, actual);
    }

    private void assertNull ( final Object actual ) {
        Assertions.assertNull(actual);
    }

    private void assertNotNull ( final Object actual ) {
        Assertions.assertNull(actual);
    }

}
