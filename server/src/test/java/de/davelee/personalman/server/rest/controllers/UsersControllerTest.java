package de.davelee.personalman.server.rest.controllers;

import de.davelee.personalman.api.PaidUserRequest;
import de.davelee.personalman.api.PayUsersResponse;
import de.davelee.personalman.api.UsersResponse;
import de.davelee.personalman.server.model.User;
import de.davelee.personalman.server.services.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

/**
 * Test cases for the users endpoints in the Personalman REST API.
 * @author Dave Lee
 */
@SpringBootTest
public class UsersControllerTest {

    @InjectMocks
    private UsersController usersController;

    @Mock
    private UserService userService;

    /**
     * Test case: attempt to find users for a company which has 1 user.
     * Expected Result: no content.
     */
    @Test
    public void testValidFindUsers() {
        //Mock the important methods in user service.
        Mockito.when(userService.checkAuthToken("max.mustermann-ghgkg")).thenReturn(true);
        Mockito.when(userService.findByCompany("MyNoCompany")).thenReturn(List.of(generateValidUser()));
        //Perform tests
        ResponseEntity<UsersResponse> responseEntity = usersController.getUsers("MyNoCompany", "max.mustermann-ghgkg");
        assertTrue(responseEntity.getStatusCodeValue() == HttpStatus.OK.value());
    }

    /**
     * Test case: attempt to find users for a company which has no users.
     * Expected Result: no content.
     */
    @Test
    public void testValidFindUsersNotFound() {
        //Mock the important methods in user service.
        Mockito.when(userService.checkAuthToken("max.mustermann-ghgkg")).thenReturn(true);
        Mockito.when(userService.findByCompany("MyNoCompany")).thenReturn(List.of());
        //Perform tests
        ResponseEntity<UsersResponse> responseEntity = usersController.getUsers("MyNoCompany", "max.mustermann-ghgkg");
        assertTrue(responseEntity.getStatusCodeValue() == HttpStatus.NO_CONTENT.value());
    }

    /**
     * Test case: attempt to find users without specifying a company.
     * Expected Result: bad request.
     */
    @Test
    public void testInvalidFindUsers() {
        //Mock the important methods in user service.
        Mockito.when(userService.checkAuthToken("max.mustermann-ghgkg")).thenReturn(true);
        Mockito.when(userService.checkAuthToken("max.mustermann-ghgkf")).thenReturn(false);
        //Perform tests
        ResponseEntity<UsersResponse> responseEntity = usersController.getUsers(null, "max.mustermann-ghgkg");
        assertTrue(responseEntity.getStatusCodeValue() == HttpStatus.BAD_REQUEST.value());
        ResponseEntity<UsersResponse> responseEntity2 = usersController.getUsers(null, "max.mustermann-ghgkf");
        assertTrue(responseEntity2.getStatusCodeValue() == HttpStatus.FORBIDDEN.value());
    }

    /**
     * Test case: pay users for a company.
     * Expected Result: success and table filled out.
     */
    @Test
    public void testValidPayUsers() {
        //Mock the important methods in user service.
        Mockito.when(userService.checkAuthToken("max.mustermann-ghgkg")).thenReturn(true);
        Mockito.when(userService.checkAuthToken("max.mustermann-ghgkf")).thenReturn(false);
        Mockito.when(userService.findByCompany("MyNoCompany")).thenReturn(List.of(generateValidUser()));
        Mockito.when(userService.findByCompany("MyEmptyCompany")).thenReturn(List.of());
        Mockito.when(userService.getHoursForDate(any(), any())).thenReturn(8);
        //Perform tests
        ResponseEntity<PayUsersResponse> responseEntity = usersController.payUsers("MyNoCompany", "max.mustermann-ghgkg", "01-01-2020","04-01-2020");
        assertTrue(responseEntity.getStatusCodeValue() == HttpStatus.OK.value());
        assertEquals(1, responseEntity.getBody().getEmployeePayTable().size());
        assertEquals(384.0, responseEntity.getBody().getTotalSum());
        //Test where token is invalid.
        ResponseEntity<PayUsersResponse> responseEntity2 = usersController.payUsers("MyNoCompany", "max.mustermann-ghgkf", "01-01-2020","04-01-2020");
        assertTrue(responseEntity2.getStatusCodeValue() == HttpStatus.FORBIDDEN.value());
        //Test where company is empty.
        ResponseEntity<PayUsersResponse> responseEntity3 = usersController.payUsers(null, "max.mustermann-ghgkg", "01-01-2020","04-01-2020");
        assertTrue(responseEntity3.getStatusCodeValue() == HttpStatus.BAD_REQUEST.value());
        //Test where company is empty.
        ResponseEntity<PayUsersResponse> responseEntity4 = usersController.payUsers("MyEmptyCompany", "max.mustermann-ghgkg", "01-01-2020","04-01-2020");
        assertTrue(responseEntity4.getStatusCodeValue() == HttpStatus.NO_CONTENT.value());
    }

    /**
     * Test case: mark users as paid for a company.
     * Expected Result: success or forbidden or bad request depending on request
     */
    @Test
    public void testValidPaidUsers() {
        //Mock the important methods in user service.
        Mockito.when(userService.checkAuthToken("max.mustermann-ghgkg")).thenReturn(true);
        Mockito.when(userService.checkAuthToken("max.mustermann-ghgkf")).thenReturn(false);
        Mockito.when(userService.findByCompany("MyNoCompany")).thenReturn(List.of(generateValidUser()));
        Mockito.when(userService.findByCompany("MyEmptyCompany")).thenReturn(List.of());
        Mockito.when(userService.getHoursForDate(any(), any())).thenReturn(8);
        Mockito.when(userService.addUserHistoryEntry(any(), any(), any(), any())).thenReturn(true);
        //Perform tests
        ResponseEntity<Void> responseEntity = usersController.paidUsers(PaidUserRequest.builder()
                .company("MyNoCompany")
                .employeePayTable(Map.of("max.mustermann", 400.0))
                .startDate("01-01-2020")
                .endDate("01-01-2020")
                .token("max.mustermann-ghgkg")
                .build());
        assertTrue(responseEntity.getStatusCodeValue() == HttpStatus.OK.value());
        //Perform test with invalid token.
        ResponseEntity<Void> responseEntity2 = usersController.paidUsers(PaidUserRequest.builder()
                .company("MyNoCompany")
                .employeePayTable(Map.of("max.mustermann", 400.0))
                .startDate("01-01-2020")
                .endDate("01-01-2020")
                .token("max.mustermann-ghgkf")
                .build());
        assertTrue(responseEntity2.getStatusCodeValue() == HttpStatus.FORBIDDEN.value());
        //Perform test with missing company.
        ResponseEntity<Void> responseEntity3 = usersController.paidUsers(PaidUserRequest.builder()
                .employeePayTable(Map.of("max.mustermann", 400.0))
                .startDate("01-01-2020")
                .endDate("01-01-2020")
                .token("max.mustermann-ghgkg")
                .build());
        assertTrue(responseEntity3.getStatusCodeValue() == HttpStatus.BAD_REQUEST.value());
    }

    /**
     * Test case: mark users as paid for a company when database is down.
     * Expected Result: 500.
     */
    @Test
    public void testInvalidPaidUsers() {
        //Mock the important methods in user service.
        Mockito.when(userService.checkAuthToken("max.mustermann-ghgkg")).thenReturn(true);
        Mockito.when(userService.findByCompany("MyNoCompany")).thenReturn(List.of(generateValidUser()));
        Mockito.when(userService.getHoursForDate(any(), any())).thenReturn(8);
        Mockito.when(userService.addUserHistoryEntry(any(), any(), any(), any())).thenReturn(false);
        //Perform tests
        ResponseEntity<Void> responseEntity = usersController.paidUsers(PaidUserRequest.builder()
                .company("MyNoCompany")
                .employeePayTable(Map.of("max.mustermann", 400.0))
                .startDate("01-01-2020")
                .endDate("01-01-2020")
                .token("max.mustermann-ghgkg")
                .build());
        System.out.println(responseEntity.getStatusCodeValue());
        assertTrue(responseEntity.getStatusCodeValue() == HttpStatus.INTERNAL_SERVER_ERROR.value());
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
                .hourlyWage(new BigDecimal(12))
                .startDate(LocalDate.of(2020,3,1))
                .workingDays(List.of(DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY, DayOfWeek.FRIDAY))
                .dateOfBirth(LocalDate.of(1992,12,31))
                .build();
    }

}
