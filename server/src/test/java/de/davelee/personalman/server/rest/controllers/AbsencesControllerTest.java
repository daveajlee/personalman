package de.davelee.personalman.server.rest.controllers;

import de.davelee.personalman.api.AbsenceRequest;
import de.davelee.personalman.api.AbsencesResponse;
import de.davelee.personalman.server.model.Absence;
import de.davelee.personalman.server.model.AbsenceCategory;
import de.davelee.personalman.server.services.AbsenceService;
import de.davelee.personalman.server.services.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;

/**
 * Test cases for the absences endpoints in the Personalman REST API.
 * @author Dave Lee
 */
@SpringBootTest
public class AbsencesControllerTest {

    @InjectMocks
    private AbsencesController absencesController;

    @Mock
    private AbsenceService absenceService;

    @Mock
    private UserService userService;

    /**
     * Test case: add a valid absence.
     * Expected Result: created.
     */
    @Test
    public void testValidAbsence() {
        //Mock the important methods in user service.
        Mockito.when(userService.checkAuthToken(anyString())).thenReturn(true);
        //Do actual test.
        AbsenceRequest validAbsenceRequest = generateValidAbsenceRequest();
        ResponseEntity<Void> responseEntity = absencesController.addAbsence(validAbsenceRequest);
        assertTrue(responseEntity.getStatusCodeValue() == HttpStatus.CREATED.value());
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
        ResponseEntity<Void> responseEntity = absencesController.addAbsence(validAbsenceRequest);
        assertTrue(responseEntity.getStatusCodeValue() == HttpStatus.BAD_REQUEST.value());
    }

    /**
     * Test case: add an absence with an end date before the start date.
     * Expected Result: bad request.
     */
    @Test
    public void testEndDateBeforeStartDate() {
        //Mock the important methods in user service.
        Mockito.when(userService.checkAuthToken(anyString())).thenReturn(true);
        //Do actual test.
        AbsenceRequest validAbsenceRequest = AbsenceRequest.builder()
                .company("MyCompany")
                .username("dlee")
                .startDate("30-11-2017")
                .endDate("30-11-2016")
                .category("Holiday")
                .token("dlee-ghgkg")
                .build();
        assertEquals("30-11-2017", validAbsenceRequest.getStartDate());
        ResponseEntity<Void> responseEntity = absencesController.addAbsence(validAbsenceRequest);
        assertTrue(responseEntity.getStatusCodeValue() == HttpStatus.BAD_REQUEST.value());
    }

    /**
     * Test case: add a valid absence with bad token.
     * Expected Result: forbidden.
     */
    @Test
    public void testWithInvalidToken() {
        //Mock the important methods in user service.
        Mockito.when(userService.checkAuthToken(anyString())).thenReturn(false);
        //Do actual test.
        AbsenceRequest validAbsenceRequest = AbsenceRequest.builder()
                .company("MyCompany")
                .username("dlee")
                .startDate("30-11-2016")
                .endDate("30-11-2017")
                .category("Holiday")
                .token("dlee-ghgkg")
                .build();
        ResponseEntity<Void> responseEntity = absencesController.addAbsence(validAbsenceRequest);
        assertTrue(responseEntity.getStatusCodeValue() == HttpStatus.FORBIDDEN.value());
    }

    /**
     * Test case: add an absence without specifying the category.
     * Expected Result: bad request.
     */
    @Test
    public void testMissingCategory() {
        //Mock the important methods in user service.
        Mockito.when(userService.checkAuthToken(anyString())).thenReturn(true);
        //Perform tests
        AbsenceRequest validAbsenceRequest = AbsenceRequest.builder()
                .company("MyCompany")
                .username("dlee")
                .startDate("30-11-2016")
                .endDate("30-11-2016")
                .token("dlee-ghgkg")
                .build();
        ResponseEntity<Void> responseEntity = absencesController.addAbsence(validAbsenceRequest);
        assertTrue(responseEntity.getStatusCodeValue() == HttpStatus.BAD_REQUEST.value());
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
        ResponseEntity<Void> responseEntity = absencesController.addAbsence(validAbsenceRequest);
        assertTrue(responseEntity.getStatusCodeValue() == HttpStatus.BAD_REQUEST.value());
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
        ResponseEntity<Void> responseEntity = absencesController.addAbsence(validAbsenceRequest);
        assertTrue(responseEntity.getStatusCodeValue() == HttpStatus.BAD_REQUEST.value());
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
        ResponseEntity<Void> responseEntity = absencesController.addAbsence(validAbsenceRequest);
        assertTrue(responseEntity.getStatusCodeValue() == HttpStatus.BAD_REQUEST.value());
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
        ResponseEntity<Void> responseEntity = absencesController.addAbsence(validAbsenceRequest);
        assertTrue(responseEntity.getStatusCodeValue() == HttpStatus.BAD_REQUEST.value());
    }

    /**
     * Test case: find absences for a particular company.
     * Expected Result: status ok.
     */
    @Test
    public void testValidFindAbsencesWithoutUsername() {
        //Mock the important methods in user service.
        Mockito.when(userService.checkAuthToken(anyString())).thenReturn(true);
        Mockito.when(absenceService.findAbsences("MyCompany", null, LocalDate.of(2016,11,30), LocalDate.of(2016,11,30))).thenReturn(
                List.of(Absence.builder()
                        .username("max.mustermann")
                        .company("MyCompany")
                        .startDate(LocalDate.of(2016,11,30))
                        .endDate(LocalDate.of(2016,11,30))
                        .category(AbsenceCategory.HOLIDAY)
                        .build())
        );
        //Perform test
        ResponseEntity<AbsencesResponse> responseEntity = absencesController.findAbsence("MyCompany", null, "30-11-2016", "30-11-2016", null, false, "dlee-fkgfgg");
        assertTrue(responseEntity.getStatusCodeValue() == HttpStatus.OK.value());
    }

    /**
     * Test case: find absences for a particular company and username.
     * Expected Result: status ok.
     */
    @Test
    public void testValidFindAbsencesWithUsername() {
        //Mock the important methods in user service.
        Mockito.when(userService.checkAuthToken(anyString())).thenReturn(true);
        Mockito.when(absenceService.findAbsences("MyCompany", "max.mustermann", LocalDate.of(2016,11,30), LocalDate.of(2016,11,30))).thenReturn(
                List.of(Absence.builder()
                        .username("max.mustermann")
                        .company("MyCompany")
                        .startDate(LocalDate.of(2016,11,30))
                        .endDate(LocalDate.of(2016,11,30))
                        .category(AbsenceCategory.HOLIDAY)
                        .build())
        );
        //Perform test
        ResponseEntity<AbsencesResponse> responseEntity = absencesController.findAbsence("MyCompany", "max.mustermann", "30-11-2016", "30-11-2016", null, false, "dlee-fkmggkgk");
        assertTrue(responseEntity.getStatusCodeValue() == HttpStatus.OK.value());
    }

    /**
     * Test case: find absences for a particular company without specifying a date range.
     * Expected Result: bad request.
     */
    @Test
    public void testMissingDatesFindAbsences() {
        ResponseEntity<AbsencesResponse> responseEntity = absencesController.findAbsence("MyCompany", null, null, null, null, false, null);
        assertTrue(responseEntity.getStatusCodeValue() == HttpStatus.BAD_REQUEST.value());
    }

    /**
     * Test case: find absences for a particular company with an end date before the start date.
     * Expected Result: bad request.
     */
    @Test
    public void testEndDateBeforeStartDateFindAbsences() {
        ResponseEntity<AbsencesResponse> responseEntity = absencesController.findAbsence("MyCompany", null, "30-11-2017", "30-11-2016", null, false, null);
        assertTrue(responseEntity.getStatusCodeValue() == HttpStatus.BAD_REQUEST.value());
    }

    /**
     * Test case: count absences for a particular company.
     * Expected Result: status ok and count 0.
     */
    @Test
    public void testCountAbsencesWithoutUsername() {
        //Mock the important methods in user service.
        Mockito.when(userService.checkAuthToken(anyString())).thenReturn(true);
        Mockito.when(absenceService.findAbsences("MyCompany", "max.mustermann", LocalDate.of(2016,11,30), LocalDate.of(2016,11,30))).thenReturn(
                List.of()
        );
        //Perform tests
        ResponseEntity<AbsencesResponse> responseEntity = absencesController.findAbsence("MyCompany", null, "30-11-2016", "30-11-2016", "Holiday", true, "dlee-fkgkgkgk");
        assertTrue(responseEntity.getStatusCodeValue() == HttpStatus.OK.value());
        assertEquals(responseEntity.getBody().getCount(), 0);
    }

    /**
     * Test case: find absences for a particular company and username.
     * Expected Result: status ok.
     */
    @Test
    public void testValidCountAbsencesWithUsername() {
        //Mock the important methods in user service.
        Mockito.when(userService.checkAuthToken(anyString())).thenReturn(true);
        Mockito.when(absenceService.findAbsences("MyCompany", "max.mustermann", LocalDate.of(2016,11,30), LocalDate.of(2016,11,30))).thenReturn(
                List.of(Absence.builder()
                        .username("max.mustermann")
                        .company("MyCompany")
                        .startDate(LocalDate.of(2016,11,30))
                        .endDate(LocalDate.of(2016,11,30))
                        .category(AbsenceCategory.HOLIDAY)
                        .build())
        );
        ResponseEntity<AbsencesResponse> responseEntity = absencesController.findAbsence("MyCompany", "max.mustermann", "30-11-2016", "30-11-2016", "Holiday", true, "dlee-fgltglgtl");
        assertTrue(responseEntity.getStatusCodeValue() == HttpStatus.OK.value());
        ResponseEntity<AbsencesResponse> responseEntity2 = absencesController.findAbsence("MyCompany", "max.mustermann", "30-11-2016", "30-11-2016", "Hol2day", true, "dlee-fgltglgtl");
        assertTrue(responseEntity2.getStatusCodeValue() == HttpStatus.BAD_REQUEST.value());
    }

    /**
     * Test case: count absences for a particular company and username where there are no known absences.
     * Expected Result: status ok and 0.
     */
    @Test
    public void testValidCountAbsencesWithUsernameWhenNoAbsences() {
        //Mock the important methods in user service.
        Mockito.when(userService.checkAuthToken(anyString())).thenReturn(true);
        Mockito.when(absenceService.findAbsences("MyCompany", "max.mustermann", LocalDate.of(2016,11,30), LocalDate.of(2016,11,30))).thenReturn(
                List.of()
        );
        //Perform tests
        ResponseEntity<AbsencesResponse> responseEntity = absencesController.findAbsence("MyCompany", "dlee", "30-11-2016", "30-11-2016", "Illness", true, "dlee-gkgtgtl");
        assertTrue(responseEntity.getStatusCodeValue() == HttpStatus.OK.value());
        assertEquals(responseEntity.getBody().getCount(), 0);
    }

    /**
     * Test case: count absences for a particular company without specifying a date range.
     * Expected Result: bad request.
     */
    @Test
    public void testMissingDateCountAbsences() {
        ResponseEntity<AbsencesResponse> responseEntity = absencesController.findAbsence("MyCompany", null, null, null, null, true, null);
        assertTrue(responseEntity.getStatusCodeValue() == HttpStatus.BAD_REQUEST.value());
    }

    /**
     * Test case: count absences for a particular company with an end date before the start date.
     * Expected Result: bad request.
     */
    @Test
    public void testEndDateBeforeStartDateCountAbsences() {
        ResponseEntity<AbsencesResponse> responseEntity = absencesController.findAbsence("MyCompany", null, "30-11-2017", "30-11-2016", null, true, null);
        assertTrue(responseEntity.getStatusCodeValue() == HttpStatus.BAD_REQUEST.value());
    }

    /**
     * Test case: count absences for a particular company without specifying a category.
     * Expected Result: bad request.
     */
    @Test
    public void testMissingCategoryCountAbsence() {
        ResponseEntity<AbsencesResponse> responseEntity = absencesController.findAbsence("MyCompany", "dlee", "30-11-2016", "30-11-2016", null, true, null);
        assertTrue(responseEntity.getStatusCodeValue() == HttpStatus.BAD_REQUEST.value());
    }

    /**
     * Test case: find absences for a particular company with an invalid category.
     * Expected Result: bad request.
     */
    @Test
    public void testInvalidCategoryCountAbsence() {
        ResponseEntity<AbsencesResponse> responseEntity = absencesController.findAbsence("MyCompany", "dlee", "30-11-2016", "30-11-2016", "MyHoliday", true, null);
        assertTrue(responseEntity.getStatusCodeValue() == HttpStatus.BAD_REQUEST.value());
    }

    /**
     * Test case: delete absences for a particular company.
     * Expected Result: status ok.
     */
    @Test
    public void testValidDeleteAbsencesWithoutUsername() {
        //Mock the important methods in user service.
        Mockito.when(userService.checkAuthToken(anyString())).thenReturn(true);
        Mockito.when(absenceService.findAbsences("MyCompany", "max.mustermann", LocalDate.of(2016,11,30), LocalDate.of(2016,11,30))).thenReturn(
                List.of()
        );
        //Perform tests
        ResponseEntity<Void> responseEntity = absencesController.deleteAbsences("MyCompany", null, "30-11-2016", "30-11-2016", "dlee-ffpggoog");
        assertTrue(responseEntity.getStatusCodeValue() == HttpStatus.OK.value());
    }

    /**
     * Test case: delete absences for a particular company and username.
     * Expected Result: status ok.
     */
    @Test
    public void testValidDeleteAbsencesWithUsername() {
        //Mock the important methods in user service.
        Mockito.when(userService.checkAuthToken(anyString())).thenReturn(true);
        Mockito.when(absenceService.findAbsences("MyCompany", "max.mustermann", LocalDate.of(2016,11,30), LocalDate.of(2016,11,30))).thenReturn(
                List.of()
        );
        //Perform tests
        ResponseEntity<Void> responseEntity = absencesController.deleteAbsences("MyCompany", "dlee", "30-11-2016", "30-11-2016", "dlee-fgglggtlg");
        assertTrue(responseEntity.getStatusCodeValue() == HttpStatus.OK.value());
    }

    /**
     * Test case: delete absences for a particular company without specifying a date range.
     * Expected Result: bad request.
     */
    @Test
    public void testMissingDatesDeleteAbsences() {
        ResponseEntity<Void> responseEntity = absencesController.deleteAbsences("MyCompany", null, null, null, null);
        assertTrue(responseEntity.getStatusCodeValue() == HttpStatus.BAD_REQUEST.value());
    }

    /**
     * Test case: delete absences for a particular company with an end date before the start date.
     * Expected Result: bad request.
     */
    @Test
    public void testEndDateBeforeStartDateDeleteAbsences() {
        ResponseEntity<Void> responseEntity = absencesController.deleteAbsences("MyCompany", null, "30-11-2017", "30-11-2016", null);
        assertTrue(responseEntity.getStatusCodeValue() == HttpStatus.BAD_REQUEST.value());
    }

    /**
     * Private helper method to generate a valid absence request.
     * @return a <code>AbsenceRequest</code> object containing valid test data.
     */
    private AbsenceRequest generateValidAbsenceRequest() {
        return AbsenceRequest.builder()
                .company("MyCompany")
                .username("dlee")
                .startDate("30-11-2016")
                .endDate("30-11-2016")
                .category("Holiday")
                .token("dlee-ghgkg")
                .build();
    }

}
