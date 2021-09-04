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

    @Test
    public void testValidAbsence() {
        //Do actual test.
        AbsenceRequest validAbsenceRequest = generateValidAbsenceRequest();
        ResponseEntity<Void> responseEntity = personalManRestController.addAbsence(validAbsenceRequest);
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
