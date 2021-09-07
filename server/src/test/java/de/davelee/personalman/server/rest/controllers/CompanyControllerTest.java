package de.davelee.personalman.server.rest.controllers;

import de.davelee.personalman.api.CompanyResponse;
import de.davelee.personalman.api.RegisterCompanyRequest;
import de.davelee.personalman.server.model.Company;
import de.davelee.personalman.server.services.CompanyService;
import de.davelee.personalman.server.services.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Test cases for the company endpoints in the Personalman REST API.
 * @author Dave Lee
 */
@SpringBootTest
public class CompanyControllerTest {

    @InjectMocks
    private CompanyController companyController;

    @Mock
    private CompanyService companyService;

    @Mock
    private UserService userService;

    /**
     * Test case: add a new company
     * Expected Result: created.
     */
    @Test
    public void testAddCompany() {
        //Do actual test.
        RegisterCompanyRequest registerCompanyRequest = RegisterCompanyRequest.builder()
                .country("Germany")
                .name("Example Company")
                .defaultAnnualLeaveInDays(25)
                .build();
        ResponseEntity<Void> responseEntity = companyController.addCompany(registerCompanyRequest);
        assertTrue(responseEntity.getStatusCodeValue() == HttpStatus.CREATED.value());
    }

    /**
     * Test case: get a company which exists and which does not exist.
     * Expected Result: ok and not found depending on whether company exists.
     */
    @Test
    public void testGetCompany() {
        //Mock the important methods in user service.
        Mockito.when(userService.checkAuthToken("max.mustermann-ghgkg")).thenReturn(true);
        Mockito.when(userService.checkAuthToken("max.mustermann-ghgkf")).thenReturn(false);
        Mockito.when(companyService.getCompany("Example Company")).thenReturn(Company.builder()
                .defaultAnnualLeaveInDays(25)
                .country("Germany")
                .name("Example Company")
                .build());
        Mockito.when(companyService.getCompany("Example Company 2")).thenReturn(null);
        //Do actual test - positive case
        ResponseEntity<CompanyResponse> responseEntity = companyController.getCompany("Example Company", "max.mustermann-ghgkg");
        assertTrue(responseEntity.getStatusCodeValue() == HttpStatus.OK.value());
        //Do actual test - negative case
        ResponseEntity<CompanyResponse> responseEntity2 = companyController.getCompany("Example Company 2", "max.mustermann-ghgkg");
        assertTrue(responseEntity2.getStatusCodeValue() == HttpStatus.NOT_FOUND.value());
        //Do actual test - name not supplied
        ResponseEntity<CompanyResponse> responseEntity3 = companyController.getCompany(null, "max.mustermann-ghgkg");
        assertTrue(responseEntity3.getStatusCodeValue() == HttpStatus.BAD_REQUEST.value());
        //Do actual test - token invalid
        ResponseEntity<CompanyResponse> responseEntity4 = companyController.getCompany("Example Company 3", "max.mustermann-ghgkf");
        assertTrue(responseEntity4.getStatusCodeValue() == HttpStatus.FORBIDDEN.value());
    }

    /**
     * Test case: delete a company which exists and which does not exist.
     * Expected Result: ok and not found depending on whether company exists.
     */
    @Test
    public void testDeleteCompany() {
        //Mock the important methods in user service.
        Mockito.when(userService.checkAuthToken("max.mustermann-ghgkg")).thenReturn(true);
        Mockito.when(userService.checkAuthToken("max.mustermann-ghgkf")).thenReturn(false);
        Mockito.when(companyService.delete("Example Company")).thenReturn(true);
        Mockito.when(companyService.delete("Example Company 2")).thenReturn(false);
        //Do actual test - positive case
        ResponseEntity<Void> responseEntity = companyController.deleteCompany("Example Company", "max.mustermann-ghgkg");
        assertTrue(responseEntity.getStatusCodeValue() == HttpStatus.OK.value());
        //Do actual test - negative case
        ResponseEntity<Void> responseEntity2 = companyController.deleteCompany("Example Company 2", "max.mustermann-ghgkg");
        assertTrue(responseEntity2.getStatusCodeValue() == HttpStatus.NOT_FOUND.value());
        //Do actual test - name not supplied
        ResponseEntity<Void> responseEntity3 = companyController.deleteCompany(null, "max.mustermann-ghgkg");
        assertTrue(responseEntity3.getStatusCodeValue() == HttpStatus.BAD_REQUEST.value());
        //Do actual test - token invalid
        ResponseEntity<Void> responseEntity4 = companyController.deleteCompany("Example Company 3", "max.mustermann-ghgkf");
        assertTrue(responseEntity4.getStatusCodeValue() == HttpStatus.FORBIDDEN.value());
    }

}
