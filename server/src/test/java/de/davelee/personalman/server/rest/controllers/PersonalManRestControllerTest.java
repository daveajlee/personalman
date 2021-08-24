package de.davelee.personalman.server.rest.controllers;

import de.davelee.personalman.api.*;
import de.davelee.personalman.server.services.AbsenceService;
import de.davelee.personalman.server.services.CompanyService;
import de.davelee.personalman.server.services.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test cases for the PersonalMan REST API.
 * @author Dave Lee
 */
@SpringBootTest
public class PersonalManRestControllerTest {

    @InjectMocks
    private PersonalManRestController personalManRestController;

    @Mock
    private AbsenceService absenceService;

    @Mock
    private CompanyService companyService;

    @Mock
    private UserService userService;

    /**
     * Test case: add a user to the system based on a valid user request.
     * Expected Result: user added successfully.
     */
    @Test
    public void testValidAdd() {
        //Add user so that test is successfully.
        UserRequest validUserRequest = generateValidUserRequest();
        assertEquals("David", validUserRequest.getFirstName());
        ResponseEntity<Void> responseEntity = personalManRestController.addUser(validUserRequest);
        assertTrue(responseEntity.getStatusCodeValue() == HttpStatus.CREATED.value());
        //Do actual test.
        AbsenceRequest validAbsenceRequest = generateValidAbsenceRequest();
        responseEntity = personalManRestController.addAbsence(validAbsenceRequest);
        assertTrue(responseEntity.getStatusCodeValue() == HttpStatus.FORBIDDEN.value());
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
        ResponseEntity<Void> responseEntity = personalManRestController.addAbsence(validAbsenceRequest);
        assertTrue(responseEntity.getStatusCodeValue() == HttpStatus.FORBIDDEN.value());
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
        ResponseEntity<Void> responseEntity = personalManRestController.addAbsence(validAbsenceRequest);
        assertTrue(responseEntity.getStatusCodeValue() == HttpStatus.FORBIDDEN.value());
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
        ResponseEntity<Void> responseEntity = personalManRestController.addAbsence(validAbsenceRequest);
        assertTrue(responseEntity.getStatusCodeValue() == HttpStatus.FORBIDDEN.value());
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
        ResponseEntity<Void> responseEntity = personalManRestController.addAbsence(validAbsenceRequest);
        assertTrue(responseEntity.getStatusCodeValue() == HttpStatus.FORBIDDEN.value());
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
        ResponseEntity<Void> responseEntity = personalManRestController.addAbsence(validAbsenceRequest);
        assertTrue(responseEntity.getStatusCodeValue() == HttpStatus.FORBIDDEN.value());
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
        ResponseEntity<Void> responseEntity = personalManRestController.addAbsence(validAbsenceRequest);
        assertTrue(responseEntity.getStatusCodeValue() == HttpStatus.FORBIDDEN.value());
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
        ResponseEntity<Void> responseEntity = personalManRestController.addAbsence(validAbsenceRequest);
        assertTrue(responseEntity.getStatusCodeValue() == HttpStatus.FORBIDDEN.value());
    }

    /**
     * Test case: find absences for a particular company.
     * Expected Result: status ok.
     */
    @Test
    public void testValidFindAbsencesWithoutUsername() {
        ResponseEntity<AbsencesResponse> responseEntity = personalManRestController.findAbsence("MyCompany", null, "30-11-2016", "30-11-2016", null, false,"dlee-fkgfgg");
        assertTrue(responseEntity.getStatusCodeValue() == HttpStatus.FORBIDDEN.value());
    }

    /**
     * Test case: find absences for a particular company and username.
     * Expected Result: status ok.
     */
    @Test
    public void testValidFindAbsencesWithUsername() {
        ResponseEntity<AbsencesResponse> responseEntity = personalManRestController.findAbsence("MyCompany", "dlee", "30-11-2016", "30-11-2016", null, false, "dlee-fkmggkgk");
        assertTrue(responseEntity.getStatusCodeValue() == HttpStatus.FORBIDDEN.value());
    }

    /**
     * Test case: find absences for a particular company without specifying a date range.
     * Expected Result: bad request.
     */
    @Test
    public void testMissingDatesFindAbsences() {
        ResponseEntity<AbsencesResponse> responseEntity = personalManRestController.findAbsence("MyCompany", null, null, null, null, false, null);
        assertTrue(responseEntity.getStatusCodeValue() == HttpStatus.FORBIDDEN.value());
    }

    /**
     * Test case: find absences for a particular company with an end date before the start date.
     * Expected Result: bad request.
     */
    @Test
    public void testEndDateBeforeStartDateFindAbsences() {
        ResponseEntity<AbsencesResponse> responseEntity = personalManRestController.findAbsence("MyCompany", null, "30-11-2017", "30-11-2016", null, false, null);
        assertTrue(responseEntity.getStatusCodeValue() == HttpStatus.FORBIDDEN.value());
    }

    /**
     * Test case: count absences for a particular company.
     * Expected Result: status ok and count 0.
     */
    @Test
    public void testCountAbsencesWithoutUsername() {
        ResponseEntity<AbsencesResponse> responseEntity = personalManRestController.findAbsence("MyCompany", null, "30-11-2016", "30-11-2016", "Holiday", true, "dlee-fkgkgkgk");
        assertTrue(responseEntity.getStatusCodeValue() == HttpStatus.FORBIDDEN.value());
    }

    /**
     * Test case: find absences for a particular company and username.
     * Expected Result: status ok.
     */
    @Test
    public void testValidCountAbsencesWithUsername() {
        ResponseEntity<AbsencesResponse> responseEntity = personalManRestController.findAbsence("MyCompany", "dlee", "30-11-2016", "30-11-2016", "Holiday", true, "dlee-fgltglgtl");
        assertTrue(responseEntity.getStatusCodeValue() == HttpStatus.FORBIDDEN.value());
    }

    /**
     * Test case: count absences for a particular company and username where there are no known absences.
     * Expected Result: status ok and 0.
     */
    @Test
    public void testValidCountAbsencesWithUsernameWhenNoAbsences() {
        ResponseEntity<AbsencesResponse> responseEntity = personalManRestController.findAbsence("MyCompany", "dlee", "30-11-2016", "30-11-2016", "Illness", true, "dlee-gkgtgtl");
        assertTrue(responseEntity.getStatusCodeValue() == HttpStatus.FORBIDDEN.value());
    }

    /**
     * Test case: count absences for a particular company without specifying a date range.
     * Expected Result: bad request.
     */
    @Test
    public void testMissingDateCountAbsences() {
        ResponseEntity<AbsencesResponse> responseEntity = personalManRestController.findAbsence("MyCompany", null, null, null, null, true, null);
        assertTrue(responseEntity.getStatusCodeValue() == HttpStatus.FORBIDDEN.value());
    }

    /**
     * Test case: count absences for a particular company with an end date before the start date.
     * Expected Result: bad request.
     */
    @Test
    public void testEndDateBeforeStartDateCountAbsences() {
        ResponseEntity<AbsencesResponse> responseEntity = personalManRestController.findAbsence("MyCompany", null, "30-11-2017", "30-11-2016", null, true, null);
        assertTrue(responseEntity.getStatusCodeValue() == HttpStatus.FORBIDDEN.value());
    }

    /**
     * Test case: count absences for a particular company without specifying a category.
     * Expected Result: bad request.
     */
    @Test
    public void testMissingCategoryCountAbsence() {
        ResponseEntity<AbsencesResponse> responseEntity = personalManRestController.findAbsence("MyCompany", "dlee", "30-11-2016", "30-11-2016", null, true, null);
        assertTrue(responseEntity.getStatusCodeValue() == HttpStatus.FORBIDDEN.value());
    }

    /**
     * Test case: find absences for a particular company with an invalid category.
     * Expected Result: bad request.
     */
    @Test
    public void testInvalidCategoryCountAbsence() {
        ResponseEntity<AbsencesResponse> responseEntity = personalManRestController.findAbsence("MyCompany", "dlee", "30-11-2016", "30-11-2016", "MyHoliday", true, null);
        assertTrue(responseEntity.getStatusCodeValue() == HttpStatus.FORBIDDEN.value());
    }

    /**
     * Test case: delete absences for a particular company.
     * Expected Result: status ok.
     */
    @Test
    public void testValidDeleteAbsencesWithoutUsername() {
        ResponseEntity<Void> responseEntity = personalManRestController.deleteAbsences("MyCompany", null, "30-11-2016", "30-11-2016", "dlee-ffpggoog");
        assertTrue(responseEntity.getStatusCodeValue() == HttpStatus.FORBIDDEN.value());
    }

    /**
     * Test case: delete absences for a particular company and username.
     * Expected Result: status ok.
     */
    @Test
    public void testValidDeleteAbsencesWithUsername() {
        ResponseEntity<Void> responseEntity = personalManRestController.deleteAbsences("MyCompany", "dlee", "30-11-2016", "30-11-2016", "dlee-fgglggtlg");
        assertTrue(responseEntity.getStatusCodeValue() == HttpStatus.FORBIDDEN.value());
    }

    /**
     * Test case: delete absences for a particular company without specifying a date range.
     * Expected Result: bad request.
     */
    @Test
    public void testMissingDatesDeleteAbsences() {
        ResponseEntity<Void> responseEntity = personalManRestController.deleteAbsences("MyCompany", null, null, null, null);
        assertTrue(responseEntity.getStatusCodeValue() == HttpStatus.FORBIDDEN.value());
    }

    /**
     * Test case: delete absences for a particular company with an end date before the start date.
     * Expected Result: bad request.
     */
    @Test
    public void testEndDateBeforeStartDateDeleteAbsences() {
        ResponseEntity<Void> responseEntity = personalManRestController.deleteAbsences("MyCompany", null, "30-11-2017", "30-11-2016", null);
        assertTrue(responseEntity.getStatusCodeValue() == HttpStatus.FORBIDDEN.value());
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
                .dateOfBirth("31-12-1992")
                .build();
        assertEquals("Max", validUserRequest.getFirstName());
        ResponseEntity<Void> responseEntity = personalManRestController.addUser(validUserRequest);
        assertTrue(responseEntity.getStatusCodeValue() == HttpStatus.CREATED.value());
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
        ResponseEntity<Void> responseEntity = personalManRestController.addUser(validUserRequest);
        assertTrue(responseEntity.getStatusCodeValue() == HttpStatus.BAD_REQUEST.value());
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
        ResponseEntity<Void> responseEntity = personalManRestController.addUser(validUserRequest);
        assertTrue(responseEntity.getStatusCodeValue() == HttpStatus.BAD_REQUEST.value());
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
        ResponseEntity<Void> responseEntity = personalManRestController.addUser(validUserRequest);
        assertTrue(responseEntity.getStatusCodeValue() == HttpStatus.BAD_REQUEST.value());
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
        ResponseEntity<Void> responseEntity = personalManRestController.addUser(validUserRequest);
        assertTrue(responseEntity.getStatusCodeValue() == HttpStatus.BAD_REQUEST.value());
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
        ResponseEntity<Void> responseEntity = personalManRestController.addUser(validUserRequest);
        assertTrue(responseEntity.getStatusCodeValue() == HttpStatus.BAD_REQUEST.value());
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
        ResponseEntity<Void> responseEntity = personalManRestController.addUser(validUserRequest);
        assertTrue(responseEntity.getStatusCodeValue() == HttpStatus.BAD_REQUEST.value());
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
        ResponseEntity<Void> responseEntity = personalManRestController.addUser(validUserRequest);
        assertTrue(responseEntity.getStatusCodeValue() == HttpStatus.BAD_REQUEST.value());
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
        ResponseEntity<Void> responseEntity = personalManRestController.addUser(validUserRequest);
        assertTrue(responseEntity.getStatusCodeValue() == HttpStatus.BAD_REQUEST.value());
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
        ResponseEntity<Void> responseEntity = personalManRestController.addUser(validUserRequest);
        assertTrue(responseEntity.getStatusCodeValue() == HttpStatus.BAD_REQUEST.value());
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
        ResponseEntity<Void> responseEntity = personalManRestController.addUser(validUserRequest);
        assertTrue(responseEntity.getStatusCodeValue() == HttpStatus.BAD_REQUEST.value());
    }

    /**
     * Test case: attempt to find users for a company which has no users.
     * Expected Result: no content.
     */
    @Test
    public void testValidFindUsersNotFound() {
        ResponseEntity<UsersResponse> responseEntity = personalManRestController.getUsers("MyNoCompany", "dlee-gkgtkgtgl");
        assertTrue(responseEntity.getStatusCodeValue() == HttpStatus.FORBIDDEN.value());
    }

    /**
     * Test case: attempt to find users without specifying a company.
     * Expected Result: bad request.
     */
    @Test
    public void testInvalidFindUsers() {
        ResponseEntity<UsersResponse> responseEntity = personalManRestController.getUsers(null, null);
        assertTrue(responseEntity.getStatusCodeValue() == HttpStatus.FORBIDDEN.value());
    }

    /**
     * Test case: attempt to find a user based on company & username, company & then delete a user based on username and company.
     * Expected Result: status ok.
     */
    @Test
    public void testValidFindUserAndThenFindUsersAndThenDelete() {
        ResponseEntity<UserResponse> responseEntity = personalManRestController.getUser("MyCompany", "dlee", "dlee-fjgkg");
        assertTrue(responseEntity.getStatusCodeValue() == HttpStatus.FORBIDDEN.value());
        ResponseEntity<UsersResponse> responseEntity1 = personalManRestController.getUsers("MyCompany", "dlee-gfkggkogt");
        assertTrue(responseEntity.getStatusCodeValue() == HttpStatus.FORBIDDEN.value());
        ResponseEntity<Void> responseEntity2 = personalManRestController.deleteUser("MyCompany", "dlee", "dlee-gkgkgkgll");
        assertTrue(responseEntity2.getStatusCodeValue() == HttpStatus.FORBIDDEN.value());
    }

    /**
     * Test case: no user exists with the specified company and username.
     * Expected Result: no content.
     */
    @Test
    public void testValidFindUserNotFound() {
        ResponseEntity<UserResponse> responseEntity = personalManRestController.getUser("MyCompany", "mlee", "dlee-glglglggl");
        assertTrue(responseEntity.getStatusCodeValue() == HttpStatus.FORBIDDEN.value());
    }

    /**
     * Test case: attempt to find a user without specifying company or username.
     * Expected Result: bad request.
     */
    @Test
    public void testInvalidFindUser() {
        ResponseEntity<UserResponse> responseEntity = personalManRestController.getUser(null, null, null);
        assertTrue(responseEntity.getStatusCodeValue() == HttpStatus.FORBIDDEN.value());
    }

    /**
     * Test case: attempt to find a user without specifying username.
     * Expected Result: bad request.
     */
    @Test
    public void testInvalidFindUserWithOnlyCompany() {
        ResponseEntity<UserResponse> responseEntity = personalManRestController.getUser("MyCompany", null, null);
        assertTrue(responseEntity.getStatusCodeValue() == HttpStatus.FORBIDDEN.value());
    }

    /**
     * Test case: attempt to delete a user which does not exist.
     * Expected Result: no content.
     */
    @Test
    public void testValidDeleteUserNotFound() {
        ResponseEntity<Void> responseEntity = personalManRestController.deleteUser("MyCompany", "mlee", "dlee-fgtgogg");
        assertTrue(responseEntity.getStatusCodeValue() == HttpStatus.FORBIDDEN.value());
    }

    /**
     * Test case: attempt to delete a user without specifying a username.
     * Expected Result: bad request.
     */
    @Test
    public void testInvalidDeleteUser() {
        ResponseEntity<Void> responseEntity = personalManRestController.deleteUser(null, null, null);
        assertTrue(responseEntity.getStatusCodeValue() == HttpStatus.FORBIDDEN.value());
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
                .dateOfBirth("31-12-1992")
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
