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

}
