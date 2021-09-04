package de.davelee.personalman.server.rest.controllers;

import de.davelee.personalman.api.UserRequest;
import de.davelee.personalman.api.UserResponse;
import de.davelee.personalman.server.services.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test cases for the User endpoints in the Personalman REST API.
 * @author Dave Lee
 */
@SpringBootTest
public class UserControllerTest {

    @InjectMocks
    private UserController userController;

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
        assertEquals("Max", validUserRequest.getFirstName());
        ResponseEntity<Void> responseEntity = userController.addUser(validUserRequest);
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
        ResponseEntity<Void> responseEntity = userController.addUser(validUserRequest);
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
        ResponseEntity<Void> responseEntity = userController.addUser(validUserRequest);
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
        ResponseEntity<Void> responseEntity = userController.addUser(validUserRequest);
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
        ResponseEntity<Void> responseEntity = userController.addUser(validUserRequest);
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
        ResponseEntity<Void> responseEntity = userController.addUser(validUserRequest);
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
        ResponseEntity<Void> responseEntity = userController.addUser(validUserRequest);
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
        ResponseEntity<Void> responseEntity = userController.addUser(validUserRequest);
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
        ResponseEntity<Void> responseEntity = userController.addUser(validUserRequest);
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
        ResponseEntity<Void> responseEntity = userController.addUser(validUserRequest);
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
        ResponseEntity<Void> responseEntity = userController.addUser(validUserRequest);
        assertTrue(responseEntity.getStatusCodeValue() == HttpStatus.BAD_REQUEST.value());
    }


    /**
     * Test case: attempt to find a user based on company & username & then delete a user based on username and company.
     * Expected Result: status ok.
     */
    @Test
    public void testValidFindUserAndThenFindUsersAndThenDelete() {
        ResponseEntity<UserResponse> responseEntity = userController.getUser("MyCompany", "dlee", "dlee-fjgkg");
        assertTrue(responseEntity.getStatusCodeValue() == HttpStatus.FORBIDDEN.value());
        ResponseEntity<Void> responseEntity2 = userController.deleteUser("MyCompany", "dlee", "dlee-gkgkgkgll");
        assertTrue(responseEntity2.getStatusCodeValue() == HttpStatus.FORBIDDEN.value());
    }

    /**
     * Test case: no user exists with the specified company and username.
     * Expected Result: no content.
     */
    @Test
    public void testValidFindUserNotFound() {
        ResponseEntity<UserResponse> responseEntity = userController.getUser("MyCompany", "mlee", "dlee-glglglggl");
        assertTrue(responseEntity.getStatusCodeValue() == HttpStatus.FORBIDDEN.value());
    }

    /**
     * Test case: attempt to find a user without specifying company or username.
     * Expected Result: bad request.
     */
    @Test
    public void testInvalidFindUser() {
        ResponseEntity<UserResponse> responseEntity = userController.getUser(null, null, null);
        assertTrue(responseEntity.getStatusCodeValue() == HttpStatus.FORBIDDEN.value());
    }

    /**
     * Test case: attempt to find a user without specifying username.
     * Expected Result: bad request.
     */
    @Test
    public void testInvalidFindUserWithOnlyCompany() {
        ResponseEntity<UserResponse> responseEntity = userController.getUser("MyCompany", null, null);
        assertTrue(responseEntity.getStatusCodeValue() == HttpStatus.FORBIDDEN.value());
    }

    /**
     * Test case: attempt to delete a user which does not exist.
     * Expected Result: no content.
     */
    @Test
    public void testValidDeleteUserNotFound() {
        ResponseEntity<Void> responseEntity = userController.deleteUser("MyCompany", "mlee", "dlee-fgtgogg");
        assertTrue(responseEntity.getStatusCodeValue() == HttpStatus.FORBIDDEN.value());
    }

    /**
     * Test case: attempt to delete a user without specifying a username.
     * Expected Result: bad request.
     */
    @Test
    public void testInvalidDeleteUser() {
        ResponseEntity<Void> responseEntity = userController.deleteUser(null, null, null);
        assertTrue(responseEntity.getStatusCodeValue() == HttpStatus.FORBIDDEN.value());
    }

    /**
     * Private helper method to generate a valid user request.
     * @return a <code>UserRequest</code> object containing valid test data.
     */
    private UserRequest generateValidUserRequest( ) {
        return UserRequest.builder()
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
    }

}
