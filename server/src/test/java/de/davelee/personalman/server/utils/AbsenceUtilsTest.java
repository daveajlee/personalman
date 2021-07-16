package de.davelee.personalman.server.utils;

import de.davelee.personalman.server.model.Absence;
import de.davelee.personalman.server.model.AbsenceCategory;
import de.davelee.personalman.server.model.User;
import de.davelee.personalman.server.services.AbsenceService;
import de.davelee.personalman.server.services.UserService;
import io.restassured.RestAssured;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Test Cases for the AbsenceUtils class - various utility methods for absences.
 * @author Dave Lee
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AbsenceUtilsTest {

    private static final String EMPLOYEE_USERNAME = "mmustermann";
    private static final String SECOND_EMPLOYEE_USERNAME = "csmith";
    private static final String THIRD_EMPLOYEE_USERNAME = "jmctavish";

    @Value("${local.server.port}")
    private int port;

    @Autowired
    private AbsenceService absenceService;

    @Autowired
    private UserService userService;

    /**
     * Before starting the tests, ensure that the port is configured successfully.
     */
    @Before
    public void setUp() {
        RestAssured.port = port;
    }

    /**
     * Test cases: generate and count absences including no free days inbetween, start on free day and end on working day,
     * start on a working day and end on a free day and start/end on working day with free days in between.
     * Expected Result: correct amount calculated.
     */
    @Test
    public void testGenerateAndCountAbsences() {
        User employee = User.builder()
                .company("MyCompany")
                .firstName("Max")
                .lastName("Mustermann")
                .leaveEntitlementPerYear(5)
                .position("Tester")
                .workingDays(List.of(DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY, DayOfWeek.FRIDAY))
                .startDate(LocalDate.of(2015,3,1))
                .userName(EMPLOYEE_USERNAME)
                .build();
        userService.delete(employee);
        userService.save(employee);
        //Test 1 - no free days inbetween
        List<Absence> absences1 = AbsenceUtils.generateAbsences(employee, LocalDate.of(2015,3,18), LocalDate.of(2015,3,19),
                AbsenceCategory.HOLIDAY);
        assertEquals(absences1.size(), 1);
        assertEquals(absences1.get(0).getStartDate(), LocalDate.of(2015,3,18));
        assertEquals(absences1.get(0).getEndDate(), LocalDate.of(2015,3,19));
        assertEquals(absences1.get(0).getUsername(), EMPLOYEE_USERNAME);
        assertEquals(absences1.get(0).getCategory(), AbsenceCategory.HOLIDAY);
        assertEquals(AbsenceUtils.countAbsencesInDays(absences1), 2);
        absenceService.delete("MyCompany", EMPLOYEE_USERNAME, LocalDate.of(2015,3,18), LocalDate.of(2015,3,19));
        assertTrue(absenceService.save(absences1.get(0)));
        //Test 2 - start on a free day and end on a working day.
        List<Absence> absences2 = AbsenceUtils.generateAbsences(employee, LocalDate.of(2015,3,21), LocalDate.of(2015,3,25),
                AbsenceCategory.HOLIDAY);
        assertEquals(absences2.size(), 1);
        assertEquals(absences2.get(0).getStartDate(),LocalDate.of(2015,3,23));
        assertEquals(absences2.get(0).getEndDate(), LocalDate.of(2015,3,25));
        assertEquals(absences2.get(0).getUsername(), EMPLOYEE_USERNAME);
        assertEquals(absences2.get(0).getCategory(), AbsenceCategory.HOLIDAY);
        assertEquals(AbsenceUtils.countAbsencesInDays(absences2), 3);
        absenceService.delete("MyCompany", EMPLOYEE_USERNAME, LocalDate.of(2015,3,23), LocalDate.of(2015,3,25));
        assertTrue(absenceService.save(absences2.get(0)));
        //Test 3 - start on a working day and end on a free day.
        List<Absence> absences3 = AbsenceUtils.generateAbsences(employee, LocalDate.of(2015,3,23), LocalDate.of(2015,3,29),
                AbsenceCategory.HOLIDAY);
        assertEquals(absences3.size(), 1);
        assertEquals(absences3.get(0).getStartDate(), LocalDate.of(2015,3,23));
        assertEquals(absences3.get(0).getEndDate(), LocalDate.of(2015,3,27));
        assertEquals(absences3.get(0).getUsername(), EMPLOYEE_USERNAME);
        assertEquals(absences3.get(0).getCategory(), AbsenceCategory.HOLIDAY);
        assertEquals(AbsenceUtils.countAbsencesInDays(absences3), 5);
        assertFalse(absenceService.save(absences3.get(0)));
        //Test 4 - start on a working day and end on a working day with free days inbetween.
        List<Absence> absences4 = AbsenceUtils.generateAbsences(employee, LocalDate.of(2015,3,16), LocalDate.of(2015,3,29),
                AbsenceCategory.HOLIDAY);
        assertEquals(absences4.size(), 2);
        assertEquals(absences4.get(0).getStartDate(), LocalDate.of(2015,3,16));
        assertEquals(absences4.get(0).getEndDate(), LocalDate.of(2015,3,20));
        assertEquals(absences4.get(0).getUsername(), EMPLOYEE_USERNAME);
        assertEquals(absences4.get(0).getCategory(), AbsenceCategory.HOLIDAY);
        assertEquals(absences4.get(1).getStartDate(), LocalDate.of(2015,3,23));
        assertEquals(absences4.get(1).getEndDate(), LocalDate.of(2015,3,27));
        assertEquals(absences4.get(1).getUsername(), EMPLOYEE_USERNAME);
        assertEquals(absences4.get(1).getCategory(), AbsenceCategory.HOLIDAY);
        assertEquals(AbsenceUtils.countAbsencesInDays(absences4), 10);
        assertFalse(absenceService.save(absences4.get(0)));
    }

    /**
     * Test case: generate and count absences correctly where the year changes during the absence or there is insufficient
     * leave available.
     * Expected Result: based on the test case, the absence should be either added correctly or refused.
     */
    @Test
    public void testGenerateAndCountAbsencesOverYears() {
        User employee = User.builder()
                .company("MyCompany")
                .firstName("Chris")
                .lastName("Smith")
                .leaveEntitlementPerYear(5)
                .position("Doctor")
                .workingDays(List.of(DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY, DayOfWeek.FRIDAY, DayOfWeek.SATURDAY))
                .startDate(LocalDate.of(2015,4,1))
                .userName(SECOND_EMPLOYEE_USERNAME)
                .build();
        userService.delete(employee);
        userService.save(employee);
        //Test 1 - absence over christmas and new year - two different years.
        absenceService.delete("MyCompany", SECOND_EMPLOYEE_USERNAME, LocalDate.of(2015,12,29), LocalDate.of(2016,1,4));
        assertTrue(absenceService.save(Absence.builder()
                .company("MyCompany")
                .category(AbsenceCategory.HOLIDAY)
                .startDate(LocalDate.of(2015,12,29))
                .endDate(LocalDate.of(2016,1,4))
                .username(SECOND_EMPLOYEE_USERNAME)
                .build()));
        //Test 2 - absence over christmas and new year - insufficient annual leave in 2015.
        absenceService.delete("MyCompany", SECOND_EMPLOYEE_USERNAME, LocalDate.of(2015,12,20), LocalDate.of(2016,1,4));
        assertFalse(absenceService.save(Absence.builder()
                .company("MyCompany")
                .category(AbsenceCategory.HOLIDAY)
                .startDate(LocalDate.of(2015,12,20))
                .endDate(LocalDate.of(2016,1,4))
                .username(SECOND_EMPLOYEE_USERNAME)
                .build()));
        //Test 3 - absence over christmas and new year - insufficient annual leave in 2016.
        absenceService.delete("MyCompany", SECOND_EMPLOYEE_USERNAME, LocalDate.of(2015,12,29), LocalDate.of(2016,1,15));
        assertFalse(absenceService.save(Absence.builder()
                .company("MyCompany")
                .category(AbsenceCategory.HOLIDAY)
                .startDate(LocalDate.of(2015,12,29))
                .endDate(LocalDate.of(2016,1,15))
                .username(SECOND_EMPLOYEE_USERNAME)
                .build()));
        //Test 4 - absence over christmas and new year - three different years.
        absenceService.delete("MyCompany", SECOND_EMPLOYEE_USERNAME, LocalDate.of(2015,12,29), LocalDate.of(2017,1,15));
        assertFalse(absenceService.save(Absence.builder()
                .company("MyCompany")
                .category(AbsenceCategory.HOLIDAY)
                .startDate(LocalDate.of(2015,12,29))
                .endDate(LocalDate.of(2017,1,15))
                .username(SECOND_EMPLOYEE_USERNAME)
                .build()));
    }

    /**
     * Test Case: check that days in lieu can be taken, that not too many days in lieu can be taken and that they
     * cannot be taken in different years.
     * Expected result: based on the individual situation, the absence is either approved or rejected.
     */
    @Test
    public void testDaysInLieuAbsences() {
        User employee = User.builder()
                .company("MyCompany")
                .firstName("Jock")
                .lastName("McTavish")
                .leaveEntitlementPerYear(5)
                .position("Farmer")
                .workingDays(List.of(DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY))
                .startDate(LocalDate.of(2015,4,1))
                .userName(THIRD_EMPLOYEE_USERNAME)
                .build();
        userService.delete(employee);
        userService.save(employee);
        //Test 1 - days in lieu can be taken.
        absenceService.delete("MyCompany", THIRD_EMPLOYEE_USERNAME, LocalDate.of(2015,4,6), LocalDate.of(2015,4,9));
        assertTrue(absenceService.save(Absence.builder()
                .company("MyCompany")
                .category(AbsenceCategory.TRIP)
                .startDate(LocalDate.of(2015,4,6))
                .endDate(LocalDate.of(2015,4,9))
                .username(THIRD_EMPLOYEE_USERNAME)
                .build()));
        absenceService.delete("MyCompany", THIRD_EMPLOYEE_USERNAME, LocalDate.of(2015,10,31), LocalDate.of(2015,1,11));
        assertTrue(absenceService.save(Absence.builder()
                .company("MyCompany")
                .category(AbsenceCategory.HOLIDAY)
                .startDate(LocalDate.of(2015,10,31))
                .endDate(LocalDate.of(2015,11,1))
                .username(THIRD_EMPLOYEE_USERNAME)
                .build()));
        //Test 2 - days in lieu over multiple years not possible.
        assertFalse(absenceService.save(Absence.builder()
                .company("MyCompany")
                .category(AbsenceCategory.DAY_IN_LIEU)
                .startDate(LocalDate.of(2015,12,31))
                .endDate(LocalDate.of(2016,1,1))
                .username(THIRD_EMPLOYEE_USERNAME)
                .build()));
        //Test 3 - not enough days in lieu.
        assertFalse(absenceService.save(Absence.builder()
                .company("MyCompany")
                .category(AbsenceCategory.DAY_IN_LIEU)
                .startDate(LocalDate.of(2015,11,7))
                .endDate(LocalDate.of(2016,11,12))
                .username(THIRD_EMPLOYEE_USERNAME)
                .build()));
    }

}
