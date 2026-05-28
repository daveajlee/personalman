package de.davelee.personalman.server.services;

import de.davelee.personalman.server.model.*;
import de.davelee.personalman.server.repository.CompanyRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test cases for the CompanyService class - the CompanyRepository is mocked.
 * @author Dave Lee
 */
@SpringBootTest
public class CompanyServiceTest {

    @InjectMocks
    private CompanyService companyService;

    @Mock
    private CompanyRepository companyRepository;

    /**
     * Test case: save a new company.
     * Expected Result: true.
     */
    @Test
    public void testSaveCompany() {
        //Test data
        Company company = Company.builder()
                .name("Example Company")
                .country("Germany")
                .defaultAnnualLeaveInDays(25).build();
        //Mock important method in repository.
        Mockito.when(companyRepository.save(company)).thenReturn(company);
        //do actual test.
        assertTrue(companyService.save(company));
    }

    /**
     * Test case: get all companies.
     * Expected Result: true.
     */
    @Test
    public void testGetAllCompanies() {
        //Test data
        Company company = Company.builder()
                .name("Example Company")
                .country("Germany")
                .defaultAnnualLeaveInDays(25).build();
        //Mock important method in repository.
        Mockito.when(companyRepository.findAll()).thenReturn(List.of(Company.builder()
                .name("Example Company")
                .country("Germany")
                .defaultAnnualLeaveInDays(25).build()));
        //do actual test.
        assertEquals(companyService.getAllCompanies().size(), 1);
    }

    /**
     * Test case: get a single company.
     * Expected Result: company is returned.
     */
    @Test
    public void testGetCompany() {
        //Test data
        Company company = Company.builder()
                .name("Example Company")
                .country("Germany")
                .defaultAnnualLeaveInDays(25).build();
        //Mock important method in repository.
        Mockito.when(companyRepository.findByName("Example Company")).thenReturn(company);
        //do actual test.
        assertNotNull(companyService.getCompany("Example Company"));
    }

    /**
     * Test case: delete a company which exists or does not exit.
     * Expected Result: true if exists or false if it does not.
     */
    @Test
    public void testDeleteCompany() {
        //Test data
        Company company = Company.builder()
                .name("Example Company")
                .country("Germany")
                .defaultAnnualLeaveInDays(25).build();
        //Mock important method in repository.
        Mockito.when(companyRepository.findByName("Example Company")).thenReturn(company);
        //do actual test.
        assertTrue(companyService.delete("Example Company"));
        assertFalse(companyService.delete("Example Company 2"));
    }

}
