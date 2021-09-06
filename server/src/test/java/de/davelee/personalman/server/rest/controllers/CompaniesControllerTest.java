package de.davelee.personalman.server.rest.controllers;

import de.davelee.personalman.server.services.CompanyService;
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
 * Test cases for the companies endpoints in the Personalman REST API.
 * @author Dave Lee
 */
@SpringBootTest
public class CompaniesControllerTest {

    @InjectMocks
    private CompaniesController companiesController;

    @Mock
    private CompanyService companyService;

    /**
     * Test case: companies exist
     * Expected Result: ok with size 1.
     */
    @Test
    public void testGetCompaniesNotEmpty() {
        //Mock the important methods in user service.
        Mockito.when(companyService.getAllCompanies()).thenReturn(List.of("Example Company"));
        //Do actual test.
        ResponseEntity<List<String>> responseEntity = companiesController.getCompanies();
        assertTrue(responseEntity.getStatusCodeValue() == HttpStatus.OK.value());
        assertEquals(responseEntity.getBody().size(), 1);
    }

    /**
     * Test case: companies exist
     * Expected Result: no content with size 0.
     */
    @Test
    public void testGetCompanies() {
        //Mock the important methods in user service.
        Mockito.when(companyService.getAllCompanies()).thenReturn(List.of());
        //Do actual test.
        ResponseEntity<List<String>> responseEntity = companiesController.getCompanies();
        assertTrue(responseEntity.getStatusCodeValue() == HttpStatus.NO_CONTENT.value());
    }

}
