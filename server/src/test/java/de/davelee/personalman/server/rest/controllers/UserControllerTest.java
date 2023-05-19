package de.davelee.personalman.server.rest.controllers;

import de.davelee.personalman.api.*;
import de.davelee.personalman.server.model.*;
import de.davelee.personalman.server.services.CompanyService;
import de.davelee.personalman.server.services.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;

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

    @Mock
    private CompanyService companyService;

    /**
     * Test case: add a user to the system based on a valid user request.
     * Expected Result: user added successfully.
     */
    @Test
    public void testValidAdd() {
        //Mock important methods in user service.
        Mockito.when(userService.save(any())).thenReturn(true);
        //Add user so that test is successfully.
        UserRequest validUserRequest = generateValidUserRequest();
        assertEquals("Max", validUserRequest.getFirstName());
        ResponseEntity<Void> responseEntity = userController.addUser(validUserRequest);
        assertEquals(responseEntity.getStatusCode(), HttpStatusCode.valueOf(201));
    }

    /**
     * Test case: Attempt to add a user to the system with a negative amount of annual leave.
     * Expected Result: default leave will be used so created status.
     */
    @Test
    public void testUserInvalidLeave() {
        // Mock important method in company service and user service.
        Mockito.when(companyService.getCompany("MyCompany")).thenReturn(Company.builder().defaultAnnualLeaveInDays(24).build());
        Mockito.when(userService.save(any())).thenReturn(true);
        // Do the actual request and test.
        UserRequest validUserRequest = UserRequest.builder()
                .firstName("David")
                .surname("Lee")
                .username("dlee")
                .company("MyCompany")
                .leaveEntitlementPerYear(-1)
                .position("Tester")
                .dateOfBirth("01-01-1990")
                .startDate("01-12-2016")
                .workingDays("Monday,Tuesday,Wednesday")
                .build();
        assertEquals(-1, validUserRequest.getLeaveEntitlementPerYear());
        ResponseEntity<Void> responseEntity = userController.addUser(validUserRequest);
        assertEquals(responseEntity.getStatusCode(), HttpStatusCode.valueOf(201));
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
                .leaveEntitlementPerYear(2)
                .position("Tester")
                .startDate("2016-11-2016")
                .workingDays("Monday,Tuesday,Wednesday")
                .build();
        assertEquals("2016-11-2016", validUserRequest.getStartDate());
        ResponseEntity<Void> responseEntity = userController.addUser(validUserRequest);
        assertEquals(responseEntity.getStatusCode(), HttpStatusCode.valueOf(400));
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
        assertEquals(responseEntity.getStatusCode(), HttpStatusCode.valueOf(400));
    }


    /**
     * Test case: attempt to add a user to the system without leave entitlement.
     * Expected Result: created since default leave entitlement will be used.
     */
    @Test
    public void testUserMissingLeaveEntitlement() {
        // Mock important method in company service and user service.
        Mockito.when(companyService.getCompany("MyCompany")).thenReturn(Company.builder().defaultAnnualLeaveInDays(24).build());
        Mockito.when(userService.save(any())).thenReturn(true);
        // Do the actual request and test.
        UserRequest validUserRequest = UserRequest.builder()
                .firstName("David")
                .surname("Lee")
                .username("dlee")
                .company("MyCompany")
                .position("Tester")
                .startDate("01-12-2016")
                .dateOfBirth("01-01-1990")
                .workingDays("Monday,Tuesday,Wednesday")
                .build();
        assertEquals(0, validUserRequest.getLeaveEntitlementPerYear());

        ResponseEntity<Void> responseEntity = userController.addUser(validUserRequest);
        assertEquals(responseEntity.getStatusCode(), HttpStatusCode.valueOf(201));
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
        assertEquals(responseEntity.getStatusCode(),HttpStatusCode.valueOf(400));
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
        assertEquals(responseEntity.getStatusCode(), HttpStatusCode.valueOf(400));
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
        assertEquals(responseEntity.getStatusCode(), HttpStatusCode.valueOf(400));
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
        assertEquals(responseEntity.getStatusCode(), HttpStatusCode.valueOf(400));
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
        assertEquals(responseEntity.getStatusCode(), HttpStatusCode.valueOf(400));
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
        assertEquals(responseEntity.getStatusCode(), HttpStatusCode.valueOf(400));
    }


    /**
     * Test case: attempt to find a user based on company & username & then delete a user based on username and company.
     * Expected Result: status ok.
     */
    @Test
    public void testValidFindUserAndThenFindUsersAndThenDelete() {
        //Mock the important methods in user service.
        Mockito.when(userService.checkAuthToken(anyString())).thenReturn(true);
        Mockito.when(userService.findByCompanyAndUserName("Example Company", "max.mustermann")).thenReturn(generateValidUser());
        Mockito.doNothing().when(userService).delete(generateValidUser());
        //Perform tests
        ResponseEntity<UserResponse> responseEntity = userController.getUser("Example Company", "max.mustermann", "max.mustermann-fjgkg");
        assertEquals(responseEntity.getStatusCode(), HttpStatusCode.valueOf(200));
        ResponseEntity<Void> responseEntity2 = userController.deleteUser("Example Company", "max.mustermann", "max.mustermann-gkgkgkgll");
        assertEquals(responseEntity2.getStatusCode(), HttpStatusCode.valueOf(200));
    }

    /**
     * Test case: no user exists with the specified company and username.
     * Expected Result: no content.
     */
    @Test
    public void testValidFindUserNotFound() {
        //Mock the important methods in user service.
        Mockito.when(userService.checkAuthToken(anyString())).thenReturn(true);
        //Perform tests
        ResponseEntity<UserResponse> responseEntity = userController.getUser("MyCompany", "mlee", "dlee-glglglggl");
        assertEquals(responseEntity.getStatusCode(), HttpStatusCode.valueOf(204));
    }

    /**
     * Test case: attempt to find a user without specifying company or username.
     * Expected Result: bad request.
     */
    @Test
    public void testInvalidFindUser() {
        ResponseEntity<UserResponse> responseEntity = userController.getUser(null, null, null);
        assertEquals(responseEntity.getStatusCode(), HttpStatusCode.valueOf(400));
    }

    /**
     * Test case: attempt to find a user without specifying username.
     * Expected Result: bad request.
     */
    @Test
    public void testInvalidFindUserWithOnlyCompany() {
        ResponseEntity<UserResponse> responseEntity = userController.getUser("MyCompany", null, null);
        assertEquals(responseEntity.getStatusCode(), HttpStatusCode.valueOf(400));
    }

    /**
     * Test case: attempt to delete a user which does not exist.
     * Expected Result: no content.
     */
    @Test
    public void testValidDeleteUserNotFound() {
        //Mock the important methods in user service.
        Mockito.when(userService.checkAuthToken(anyString())).thenReturn(true);
        //Perform tests
        ResponseEntity<Void> responseEntity = userController.deleteUser("MyCompany", "mlee", "dlee-fgtgogg");
        assertEquals(responseEntity.getStatusCode(), HttpStatusCode.valueOf(204));
    }

    /**
     * Test case: attempt to delete a user without specifying a username.
     * Expected Result: bad request.
     */
    @Test
    public void testInvalidDeleteUser() {
        ResponseEntity<Void> responseEntity = userController.deleteUser(null, null, null);
        assertEquals(responseEntity.getStatusCode(), HttpStatusCode.valueOf(400));
    }

    /**
     * Test case: update the salary information for the specified user.
     * Expected Result: forbidden or no content or ok depending on request.
     */
    @Test
    public void testUpdateSalaryInformation() {
        //Mock the important methods in user service.
        Mockito.when(userService.checkAuthToken("max.mustermann-ghgkg")).thenReturn(true);
        Mockito.when(userService.checkAuthToken("max.mustermann-ghgkf")).thenReturn(false);
        Mockito.when(userService.findByCompanyAndUserName("Example Company", "max.mustermann")).thenReturn(generateValidUser());
        Mockito.when(userService.updateSalaryInformation(any(), any(), eq(40))).thenReturn(true);
        //Perform tests - valid request
        UpdateSalaryRequest updateSalaryRequest = UpdateSalaryRequest.builder()
                .contractedHoursPerWeek(40)
                .hourlyWage(13.0)
                .username("max.mustermann")
                .token("max.mustermann-ghgkg")
                .company("Example Company")
                .build();
        ResponseEntity<Void> responseEntity = userController.updateSalaryInformation(updateSalaryRequest);
        assertEquals(responseEntity.getStatusCode(), HttpStatusCode.valueOf(200));
        //Perform tests - invalid token
        UpdateSalaryRequest updateSalaryRequest2 = UpdateSalaryRequest.builder()
                .contractedHoursPerWeek(40)
                .hourlyWage(13.0)
                .username("max.mustermann")
                .token("max.mustermann-ghgkf")
                .company("Example Company")
                .build();
        ResponseEntity<Void> responseEntity2 = userController.updateSalaryInformation(updateSalaryRequest2);
        assertEquals(responseEntity2.getStatusCode(), HttpStatusCode.valueOf(403));
        //Perform tests - no user
        UpdateSalaryRequest updateSalaryRequest3 = UpdateSalaryRequest.builder()
                .contractedHoursPerWeek(40)
                .hourlyWage(13.0)
                .username("max.a.mustermann")
                .token("max.mustermann-ghgkg")
                .company("Example Company")
                .build();
        ResponseEntity<Void> responseEntity3 = userController.updateSalaryInformation(updateSalaryRequest3);
        assertEquals(responseEntity3.getStatusCode(), HttpStatusCode.valueOf(204));
    }

    /**
     * Test case: add a training course for the specified user.
     * Expected Result: forbidden or no content or ok depending on request.
     */
    @Test
    public void testAddTrainingCourse() {
        //Mock the important methods in user service.
        Mockito.when(userService.checkAuthToken("max.mustermann-ghgkg")).thenReturn(true);
        Mockito.when(userService.checkAuthToken("max.mustermann-ghgkf")).thenReturn(false);
        Mockito.when(userService.findByCompanyAndUserName("Example Company", "max.mustermann")).thenReturn(generateValidUser());
        Mockito.when(userService.addTrainingCourse(any(), anyString())).thenReturn(true);
        //Perform tests - valid request
        AddTrainingRequest addTrainingRequest = AddTrainingRequest.builder()
                .username("max.mustermann")
                .token("max.mustermann-ghgkg")
                .company("Example Company")
                .trainingCourse("Certified Tester")
                .build();
        ResponseEntity<Void> responseEntity = userController.addTraining(addTrainingRequest);
        assertEquals(responseEntity.getStatusCode(), HttpStatusCode.valueOf(200));
        //Perform tests - invalid token
        AddTrainingRequest addTrainingRequest2 = AddTrainingRequest.builder()
                .username("max.mustermann")
                .token("max.mustermann-ghgkf")
                .company("Example Company")
                .trainingCourse("Certified Tester")
                .build();
        ResponseEntity<Void> responseEntity2 = userController.addTraining(addTrainingRequest2);
        assertEquals(responseEntity2.getStatusCode(), HttpStatusCode.valueOf(403));
        //Perform tests - no user
        AddTrainingRequest addTrainingRequest3 = AddTrainingRequest.builder()
                .username("max.a.mustermann")
                .token("max.mustermann-ghgkg")
                .company("Example Company")
                .trainingCourse("Certified Tester")
                .build();
        ResponseEntity<Void> responseEntity3 = userController.addTraining(addTrainingRequest3);
        assertEquals(responseEntity3.getStatusCode(), HttpStatusCode.valueOf(204));
    }

    /**
     * Test case: add a history entry for the specified user.
     * Expected Result: forbidden or no content or ok depending on request.
     */
    @Test
    public void testAddHistoryEntry() {
        //Mock the important methods in user service.
        Mockito.when(userService.checkAuthToken("max.mustermann-ghgkg")).thenReturn(true);
        Mockito.when(userService.checkAuthToken("max.mustermann-ghgkf")).thenReturn(false);
        Mockito.when(userService.findByCompanyAndUserName("Example Company", "max.mustermann")).thenReturn(generateValidUser());
        Mockito.when(userService.addUserHistoryEntry(any(), any(), any(), anyString())).thenReturn(true);
        //Perform tests - valid request
        AddHistoryEntryRequest addHistoryEntryRequest = AddHistoryEntryRequest.builder()
                .username("max.mustermann")
                .token("max.mustermann-ghgkg")
                .company("Example Company")
                .date("01-03-2020")
                .reason("JOINED")
                .comment("Welcome to the company!")
                .build();
        ResponseEntity<Void> responseEntity = userController.addHistoryEntry(addHistoryEntryRequest);
        assertEquals(responseEntity.getStatusCode(), HttpStatusCode.valueOf(200));
        //Perform tests - invalid token
        AddHistoryEntryRequest addHistoryEntryRequest2 = AddHistoryEntryRequest.builder()
                .username("max.mustermann")
                .token("max.mustermann-ghgkf")
                .company("Example Company")
                .date("01-03-2020")
                .reason("JOINED")
                .comment("Welcome to the company!")
                .build();
        ResponseEntity<Void> responseEntity2 = userController.addHistoryEntry(addHistoryEntryRequest2);
        assertEquals(responseEntity2.getStatusCode(), HttpStatusCode.valueOf(403));
        //Perform tests - no user
        AddHistoryEntryRequest addHistoryEntryRequest3 = AddHistoryEntryRequest.builder()
                .username("max.a.mustermann")
                .token("max.mustermann-ghgkg")
                .company("Example Company")
                .date("01-03-2020")
                .reason("JOINED")
                .comment("Welcome to the company!")
                .build();
        ResponseEntity<Void> responseEntity3 = userController.addHistoryEntry(addHistoryEntryRequest3);
        assertEquals(responseEntity3.getStatusCode(), HttpStatusCode.valueOf(204));
    }

    /**
     * Test case: add a timesheet date and hours for the specified user.
     * Expected Result: forbidden or no content or ok depending on request.
     */
    @Test
    public void testAddTimesheetForUser() {
        //Mock the important methods in user service.
        Mockito.when(userService.checkAuthToken("max.mustermann-ghgkg")).thenReturn(true);
        Mockito.when(userService.checkAuthToken("max.mustermann-ghgkf")).thenReturn(false);
        Mockito.when(userService.findByCompanyAndUserName("Example Company", "max.mustermann")).thenReturn(generateValidUser());
        Mockito.when(userService.addHoursForDate(any(), eq(8), any())).thenReturn(true);
        //Perform tests - valid request
        AddTimeSheetHoursRequest addTimeSheetHoursRequest = AddTimeSheetHoursRequest.builder()
                .username("max.mustermann")
                .token("max.mustermann-ghgkg")
                .company("Example Company")
                .hours(8)
                .date("01-03-2020")
                .build();
        ResponseEntity<Void> responseEntity = userController.addHoursForDate(addTimeSheetHoursRequest);
        assertEquals(responseEntity.getStatusCode(), HttpStatusCode.valueOf(200));
        //Perform tests - invalid token
        AddTimeSheetHoursRequest addTimeSheetHoursRequest2 = AddTimeSheetHoursRequest.builder()
                .username("max.mustermann")
                .token("max.mustermann-ghgkf")
                .company("Example Company")
                .hours(8)
                .date("01-03-2020")
                .build();
        ResponseEntity<Void> responseEntity2 = userController.addHoursForDate(addTimeSheetHoursRequest2);
        assertEquals(responseEntity2.getStatusCode(), HttpStatusCode.valueOf(403));
        //Perform tests - no user
        AddTimeSheetHoursRequest addTimeSheetHoursRequest3 = AddTimeSheetHoursRequest.builder()
                .username("max.a.mustermann")
                .token("max.mustermann-ghgkg")
                .company("Example Company")
                .hours(8)
                .date("01-03-2020")
                .build();
        ResponseEntity<Void> responseEntity3 = userController.addHoursForDate(addTimeSheetHoursRequest3);
        assertEquals(responseEntity3.getStatusCode(), HttpStatusCode.valueOf(204));
    }

    /**
     * Test case: retrieve timesheet date and hours for the specified user.
     * Expected Result: forbidden or no content or ok depending on request.
     */
    @Test
    public void testGetTimesheetForUser() {
        //Mock the important methods in user service.
        Mockito.when(userService.checkAuthToken("max.mustermann-ghgkg")).thenReturn(true);
        Mockito.when(userService.checkAuthToken("max.mustermann-ghgkf")).thenReturn(false);
        Mockito.when(userService.findByCompanyAndUserName("Example Company", "max.mustermann")).thenReturn(generateValidUser());
        //Perform tests - valid request
        ResponseEntity<Integer> responseEntity = userController.getHoursForDate("Example Company", "max.mustermann", "max.mustermann-ghgkg", "01-03-2020", "02-03-2020");
        assertEquals(responseEntity.getStatusCode(), HttpStatusCode.valueOf(200));
        //Perform tests - valid request with single date
        ResponseEntity<Integer> responseEntity1 = userController.getHoursForDate("Example Company", "max.mustermann", "max.mustermann-ghgkg", "01-03-2020", "01-03-2020");
        assertEquals(responseEntity1.getStatusCode(), HttpStatusCode.valueOf(200));
        //Perform tests - invalid token
        ResponseEntity<Integer> responseEntity2 = userController.getHoursForDate("Example Company", "max.mustermann", "max.mustermann-ghgkf", "01-03-2020", "02-03-2020");
        assertEquals(responseEntity2.getStatusCode(), HttpStatusCode.valueOf(403));
        //Perform tests - no user
        ResponseEntity<Integer> responseEntity3 = userController.getHoursForDate("Example Company", "max.a.mustermann", "max.mustermann-ghgkg", "01-03-2020", "02-03-2020");
        assertEquals(responseEntity3.getStatusCode(), HttpStatusCode.valueOf(204));
    }

    /**
     * Test case: change password for a user who exists and then one who does not exist.
     * Expected Result: forbidden or no content or ok depending on request.
     */
    @Test
    public void testChangePasswordForUser() {
        //Mock the important methods in user service.
        Mockito.when(userService.checkAuthToken("max.mustermann-ghgkg")).thenReturn(true);
        Mockito.when(userService.checkAuthToken("max.mustermann-ghgkf")).thenReturn(false);
        Mockito.when(userService.changePassword("Example Company", "max.mustermann", "test123", "123test")).thenReturn(true);
        Mockito.when(userService.changePassword("Example Company", "max.a.mustermann", "test123", "123test")).thenReturn(false);
        //Perform tests - valid request
        ResponseEntity<Void> responseEntity = userController.changePassword(ChangePasswordRequest.builder()
                        .company("Example Company")
                        .username("max.mustermann")
                        .currentPassword("test123")
                        .newPassword("123test")
                        .token("max.mustermann-ghgkg")
                        .build());
        assertEquals(responseEntity.getStatusCode(), HttpStatusCode.valueOf(200));
        //Perform tests - invalid token
        ResponseEntity<Void> responseEntity2 = userController.changePassword(ChangePasswordRequest.builder()
                .company("Example Company")
                .username("max.mustermann")
                .currentPassword("test123")
                .newPassword("123test")
                .token("max.mustermann-ghgkf")
                .build());
        assertEquals(responseEntity2.getStatusCode(),  HttpStatusCode.valueOf(403));
        //Perform tests - no user
        ResponseEntity<Void> responseEntity3 = userController.changePassword(ChangePasswordRequest.builder()
                .company("Example Company")
                .username("max.a.mustermann")
                .currentPassword("test123")
                .newPassword("123test")
                .token("max.mustermann-ghgkg")
                .build());
        assertEquals(responseEntity3.getStatusCode(), HttpStatusCode.valueOf(404));
    }

    /**
     * Test case: deactivate for a user who exists and then one who does not exist.
     * Expected Result: forbidden or no content or ok depending on request.
     */
    @Test
    public void testDeactivateForUser() {
        //Mock the important methods in user service.
        Mockito.when(userService.checkAuthToken("max.mustermann-ghgkg")).thenReturn(true);
        Mockito.when(userService.checkAuthToken("max.mustermann-ghgkf")).thenReturn(false);
        Mockito.when(userService.deactivate(any(),eq(LocalDate.of(2020,6,30)),eq(false),eq("Unprofessional behaviour"))).thenReturn(13);
        Mockito.when(userService.changePassword("Example Company", "max.a.mustermann", "test123", "123test")).thenReturn(false);
        Mockito.when(userService.findByCompanyAndUserName("Example Company", "max.mustermann")).thenReturn(generateValidUser());
        //Perform tests - valid request
        ResponseEntity<DeactivateUserResponse> responseEntity = userController.deactivateUser(DeactivateUserRequest.builder()
                .company("Example Company")
                .username("max.mustermann")
                .token("max.mustermann-ghgkg")
                .leavingDate("30-06-2020")
                .reason("Unprofessional behaviour")
                .resigned(false)
                .build());
        assertEquals(responseEntity.getStatusCode(), HttpStatusCode.valueOf(200));
        assertNotNull(responseEntity.getBody());
        assertEquals(13, responseEntity.getBody().getLeaveEntitlementForThisYear());
        //Perform tests - invalid token
        ResponseEntity<DeactivateUserResponse> responseEntity2 = userController.deactivateUser(DeactivateUserRequest.builder()
                .company("Example Company")
                .username("max.mustermann")
                .token("max.mustermann-ghgkf")
                .leavingDate("30-06-2020")
                .reason("Unprofessional behaviour")
                .resigned(false)
                .build());
        assertEquals(responseEntity2.getStatusCode(), HttpStatusCode.valueOf(403));
        //Perform tests - no user
        ResponseEntity<DeactivateUserResponse> responseEntity3 = userController.deactivateUser(DeactivateUserRequest.builder()
                .company("Example Company")
                .username("max.a.mustermann")
                .token("max.mustermann-ghgkg")
                .leavingDate("30-06-2020")
                .reason("Unprofessional behaviour")
                .resigned(false)
                .build());
        assertEquals(responseEntity3.getStatusCode(), HttpStatusCode.valueOf(204));
    }

    /**
     * Test case: login with a valid user/password and invalidation combinations.
     * Expected Result: forbidden or ok depending on request.
     */
    @Test
    public void testLogin() {
        //Mock the important methods in user service.
        Mockito.when(userService.findByCompanyAndUserName("Example Company", "max.mustermann")).thenReturn(generateValidUser());
        //Test with valid login
        LoginRequest validLoginRequest = LoginRequest.builder()
                .company("Example Company")
                .username("max.mustermann")
                .password("test")
                .build();
        ResponseEntity<LoginResponse> responseEntity = userController.login(validLoginRequest);
        assertEquals( responseEntity.getStatusCode(), HttpStatusCode.valueOf(200));
        //Test with incorrect password
        LoginRequest invalidLoginRequest = LoginRequest.builder()
                .company("Example Company")
                .username("max.mustermann")
                .password("123test")
                .build();
        ResponseEntity<LoginResponse> responseEntity2 = userController.login(invalidLoginRequest);
        assertEquals( responseEntity2.getStatusCode(), HttpStatusCode.valueOf(403));
        //Test with invalid username
        LoginRequest invalidLoginRequest2 = LoginRequest.builder()
                .company("Example Company")
                .username("max.a.mustermann")
                .password("123test")
                .build();
        ResponseEntity<LoginResponse> responseEntity3 = userController.login(invalidLoginRequest2);
        assertEquals( responseEntity3.getStatusCode(), HttpStatusCode.valueOf(403));
    }

    /**
     * Test case: logout.
     * Expected Result: ok.
     */
    @Test
    public void testLogout() {
        //Do actual test
        ResponseEntity<Void> responseEntity = userController.logout(LogoutRequest.builder()
                .token("max.mustermann-ghgkg")
                .build());
        assertEquals(responseEntity.getStatusCode(), HttpStatusCode.valueOf(200));
    }


    /**
     * Test case: reset user which exists or does not exist.
     * Expected Result: not found or ok depending on request.
     */
    @Test
    public void testReset() {
        //Mock the important methods in user service.
        Mockito.when(userService.checkAuthToken("max.mustermann-ghgkg")).thenReturn(true);
        Mockito.when(userService.checkAuthToken("max.mustermann-ghgkf")).thenReturn(false);
        Mockito.when(userService.resetUserPassword("Example Company", "max.mustermann", "test")).thenReturn(true);
        Mockito.when(userService.resetUserPassword("Example Company", "max.a.mustermann", "test")).thenReturn(false);
        //Test with valid user
        ResetUserRequest resetUserRequest = ResetUserRequest.builder()
                .company("Example Company")
                .username("max.mustermann")
                .password("test")
                .token("max.mustermann-ghgkg")
                .build();
        ResponseEntity<Void> responseEntity = userController.resetUser(resetUserRequest);
        assertEquals( responseEntity.getStatusCode(), HttpStatusCode.valueOf(200));
        //Test with invalid username
        ResetUserRequest resetUserRequest2 = ResetUserRequest.builder()
                .company("Example Company")
                .username("max.a.mustermann")
                .password("test")
                .token("max.mustermann-ghgkg")
                .build();
        ResponseEntity<Void> responseEntity2 = userController.resetUser(resetUserRequest2);
        assertEquals( responseEntity2.getStatusCode(), HttpStatusCode.valueOf(404));
        //Test with invalid token
        ResetUserRequest resetUserRequest3 = ResetUserRequest.builder()
                .company("Example Company")
                .username("max.mustermann")
                .password("test")
                .token("max.mustermann-ghgkf")
                .build();
        ResponseEntity<Void> responseEntity3 = userController.resetUser(resetUserRequest3);
        assertEquals( responseEntity3.getStatusCode(), HttpStatusCode.valueOf(403));
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
                .workingDays("Monday,Tuesday,Wednesday,Thursday,Friday,Saturday,Sunday")
                .dateOfBirth("31-12-1992")
                .build();
    }

    /**
     * Private helper method to generate a valid user.
     * @return a <code>User</code> object containing valid test data.
     */
    private User generateValidUser( ) {
        return User.builder()
                .company("Example Company")
                .firstName("Max")
                .lastName("Mustermann")
                .leaveEntitlementPerYear(25)
                .position("Secretary")
                .userName("max.mustermann")
                .password("test")
                .role("Employee")
                .accountStatus(UserAccountStatus.ACTIVE)
                .startDate(LocalDate.of(2020,3,1))
                .workingDays(List.of(DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY, DayOfWeek.FRIDAY, DayOfWeek.SATURDAY, DayOfWeek.SUNDAY))
                .dateOfBirth(LocalDate.of(1992,12,31))
                .hourlyWage(new BigDecimal(12))
                .contractedHoursPerWeek(40)
                .trainingsList(List.of("Certified Tester"))
                .userHistoryEntryList(List.of(UserHistoryEntry.builder()
                        .comment("Welcome to the company!")
                        .userHistoryReason(UserHistoryReason.JOINED)
                        .date(LocalDate.of(2020,3,1))
                        .build()))
                .build();
    }

}
