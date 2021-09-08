package de.davelee.personalman.server.services;

import de.davelee.personalman.server.model.Absence;
import de.davelee.personalman.server.model.AbsenceCategory;
import de.davelee.personalman.server.model.User;
import de.davelee.personalman.server.repository.AbsenceRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

/**
 * Test cases for the AbsenceService class which are not covered by other tests - the AbsenceRepository is mocked.
 * @author Dave Lee
 */
@SpringBootTest
public class AbsenceServiceTest {

    @InjectMocks
    private AbsenceService absenceService;

    @Mock
    private AbsenceRepository absenceRepository;

    @Mock
    private UserService userService;

    @Mock
    private MongoTemplate mongoTemplate;

    /**
     * Test case: save a holiday absence which is more than one year
     * Expected result: false
     */
    @Test
    public void testSaveHolidayOverOneYear ( ) {
        //Test data
        User user = generateValidUser();
        Absence absence = Absence.builder()
                .category(AbsenceCategory.HOLIDAY)
                .startDate(LocalDate.of(2016,1,1))
                .endDate(LocalDate.of(2019,6,30))
                .company("Example Company")
                .username("max.mustermann")
                .build();
        //Mock important method in repository.
        Mockito.when(userService.findByCompanyAndUserName("Example Company", "max.mustermann")).thenReturn(user);
        assertFalse(absenceService.save(absence));
    }

    /**
     * Test case: save a day in lieu if no days are available to take.
     * Expected result: false
     */
    @Test
    public void testSaveDayInLieu ( ) {
        //Test data
        User user = generateValidUser();
        Absence absence = Absence.builder()
                .category(AbsenceCategory.DAY_IN_LIEU)
                .startDate(LocalDate.of(2015, 12,30))
                .endDate(LocalDate.of(2015,12,31))
                .company("Example Company")
                .username("max.mustermann")
                .build();
        //Mock important method in repository.
        Mockito.when(userService.findByCompanyAndUserName("Example Company", "max.mustermann")).thenReturn(user);
        assertFalse(absenceService.save(absence));
    }

    /**
     * Test case: save a day of illness.
     * Expected result: true
     */
    @Test
    public void testSaveIllness ( ) {
        //Test data
        User user = generateValidUser();
        Absence absence = Absence.builder()
                .category(AbsenceCategory.ILLNESS)
                .startDate(LocalDate.of(2015, 12,30))
                .endDate(LocalDate.of(2015,12,31))
                .company("Example Company")
                .username("max.mustermann")
                .build();
        //Mock important method in repository.
        Mockito.when(userService.findByCompanyAndUserName("Example Company", "max.mustermann")).thenReturn(user);
        assertTrue(absenceService.save(absence));
    }

    /**
     * Test case: find absences with and without username.
     * Expected result: list of 1 absence
     */
    @Test
    public void testFindAbsences ( ) {
        //Test data
        List<Absence> absences = List.of(Absence.builder()
                .category(AbsenceCategory.ILLNESS)
                .startDate(LocalDate.of(2015, 12,30))
                .endDate(LocalDate.of(2015,12,31))
                .company("Example Company")
                .username("max.mustermann")
                .build());
        //Mock important method in repository.
        Mockito.when(mongoTemplate.find(any(), eq(Absence.class))).thenReturn(absences);
        assertEquals(1, absenceService.findAbsences("Example Company", "max.mustermann", LocalDate.of(2015,12,30), LocalDate.of(2015,12,31)).size());
        assertEquals(1, absenceService.findAbsences("Example Company", null, LocalDate.of(2015,12,30), LocalDate.of(2015,12,31)).size());
    }

    /**
     * Test case: count absences of same category.
     * Expected result: 2 as two days worth of absences.
     */
    @Test
    public void testCountAbsences ( ) {
        //Test data
        List<Absence> absences = List.of(Absence.builder()
                .category(AbsenceCategory.ILLNESS)
                .startDate(LocalDate.of(2015, 12,30))
                .endDate(LocalDate.of(2015,12,31))
                .company("Example Company")
                .username("max.mustermann")
                .build());
        //Mock important method in repository.
        Mockito.when(mongoTemplate.find(any(), eq(Absence.class))).thenReturn(absences);
        assertEquals(2, absenceService.countAbsences("Example Company", "max.mustermann", LocalDate.of(2015,12,30), LocalDate.of(2015,12,31), AbsenceCategory.ILLNESS));
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
                .startDate(LocalDate.of(2020,3,1))
                .workingDays(List.of(DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY, DayOfWeek.FRIDAY, DayOfWeek.SATURDAY, DayOfWeek.SUNDAY))
                .dateOfBirth(LocalDate.of(1992,12,31))
                .build();
    }
}
