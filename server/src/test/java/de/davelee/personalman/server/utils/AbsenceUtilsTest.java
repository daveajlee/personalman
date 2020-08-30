package de.davelee.personalman.server.utils;

import com.jayway.restassured.RestAssured;
import de.davelee.personalman.server.model.Absence;
import de.davelee.personalman.server.model.AbsenceCategory;
import de.davelee.personalman.server.model.User;
import de.davelee.personalman.server.services.AbsenceService;
import de.davelee.personalman.server.services.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
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

    @Before
    /**
     * Before starting the tests, ensure that the port is configured successfully.
     */
    public void setUp() {
        RestAssured.port = port;
    }

    @Test
    /**
     * Test cases: generate and count absences including no free days inbetween, start on free day and end on working day,
     * start on a working day and end on a free day and start/end on working day with free days in between.
     * Expected Result: correct amount calculated.
     */
    public void testGenerateAndCountAbsences() {
        User employee = new User();
        employee.setCompany("MyCompany");
        employee.setFirstName("Max");
        employee.setLastName("Mustermann");
        employee.setLeaveEntitlementPerYear(5);
        employee.setPosition("Tester");
        List<DayOfWeek> dayOfWeekList = new ArrayList<>();
        dayOfWeekList.add(DayOfWeek.MONDAY); dayOfWeekList.add(DayOfWeek.TUESDAY); dayOfWeekList.add(DayOfWeek.WEDNESDAY);
        dayOfWeekList.add(DayOfWeek.THURSDAY); dayOfWeekList.add(DayOfWeek.FRIDAY);
        employee.setWorkingDays(dayOfWeekList);
        employee.setStartDate(LocalDate.of(2015,3,1));
        employee.setUserName(EMPLOYEE_USERNAME);
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

    @Test
    /**
     * Test case: generate and count absences correctly where the year changes during the absence or there is insufficient
     * leave available.
     * Expected Result: based on the test case, the absence should be either added correctly or refused.
     */
    public void testGenerateAndCountAbsencesOverYears() {
        User employee = new User();
        employee.setCompany("MyCompany");
        employee.setFirstName("Chris");
        employee.setLastName("Smith");
        employee.setLeaveEntitlementPerYear(5);
        employee.setPosition("Doctor");
        List<DayOfWeek> dayOfWeekList = new ArrayList<>();
        dayOfWeekList.add(DayOfWeek.TUESDAY); dayOfWeekList.add(DayOfWeek.WEDNESDAY);
        dayOfWeekList.add(DayOfWeek.THURSDAY); dayOfWeekList.add(DayOfWeek.FRIDAY); dayOfWeekList.add(DayOfWeek.SATURDAY);
        employee.setWorkingDays(dayOfWeekList);
        employee.setStartDate(LocalDate.of(2015,4,1));
        employee.setUserName(SECOND_EMPLOYEE_USERNAME);
        userService.delete(employee);
        userService.save(employee);
        //Test 1 - absence over christmas and new year - two different years.
        absenceService.delete("MyCompany", SECOND_EMPLOYEE_USERNAME, LocalDate.of(2015,12,29), LocalDate.of(2016,1,4));
        Absence absence = new Absence();
        absence.setCompany("MyCompany");
        absence.setCategory(AbsenceCategory.HOLIDAY);
        absence.setStartDate(LocalDate.of(2015,12,29));
        absence.setEndDate(LocalDate.of(2016,1,4));
        absence.setUsername(SECOND_EMPLOYEE_USERNAME);
        assertTrue(absenceService.save(absence));
        //Test 2 - absence over christmas and new year - insufficient annual leave in 2015.
        absenceService.delete("MyCompany", SECOND_EMPLOYEE_USERNAME, LocalDate.of(2015,12,20), LocalDate.of(2016,1,4));
        Absence absence2 = new Absence();
        absence2.setCompany("MyCompany");
        absence2.setCategory(AbsenceCategory.HOLIDAY);
        absence2.setStartDate(LocalDate.of(2015,12,20));
        absence2.setEndDate(LocalDate.of(2016,1,4));
        absence2.setUsername(SECOND_EMPLOYEE_USERNAME);
        assertFalse(absenceService.save(absence2));
        //Test 3 - absence over christmas and new year - insufficient annual leave in 2016.
        absenceService.delete("MyCompany", SECOND_EMPLOYEE_USERNAME, LocalDate.of(2015,12,29), LocalDate.of(2016,1,15));
        Absence absence3 = new Absence();
        absence3.setCompany("MyCompany");
        absence3.setCategory(AbsenceCategory.HOLIDAY);
        absence3.setStartDate(LocalDate.of(2015,12,29));
        absence3.setEndDate(LocalDate.of(2016,1,15));
        absence3.setUsername(SECOND_EMPLOYEE_USERNAME);
        assertFalse(absenceService.save(absence3));
        //Test 4 - absence over christmas and new year - three different years.
        absenceService.delete("MyCompany", SECOND_EMPLOYEE_USERNAME, LocalDate.of(2015,12,29), LocalDate.of(2017,1,15));
        Absence absence4 = new Absence();
        absence4.setCompany("MyCompany");
        absence4.setCategory(AbsenceCategory.HOLIDAY);
        absence4.setStartDate(LocalDate.of(2015,12,29));
        absence4.setEndDate(LocalDate.of(2017,1,15));
        absence4.setUsername(SECOND_EMPLOYEE_USERNAME);
        assertFalse(absenceService.save(absence4));
    }

    @Test
    /**
     * Test Case: check that days in lieu can be taken, that not too many days in lieu can be taken and that they
     * cannot be taken in different years.
     * Expected result: based on the individual situation, the absence is either approved or rejected.
     */
    public void testDaysInLieuAbsences() {
        User employee = new User();
        employee.setCompany("MyCompany");
        employee.setFirstName("Jock");
        employee.setLastName("McTavish");
        employee.setLeaveEntitlementPerYear(5);
        employee.setPosition("Farmer");
        List<DayOfWeek> dayOfWeekList = new ArrayList<>();
        dayOfWeekList.add(DayOfWeek.TUESDAY); dayOfWeekList.add(DayOfWeek.WEDNESDAY);
        employee.setWorkingDays(dayOfWeekList);
        employee.setStartDate(LocalDate.of(2015,4,1));
        employee.setUserName(THIRD_EMPLOYEE_USERNAME);
        userService.delete(employee);
        userService.save(employee);
        //Test 1 - days in lieu can be taken.
        absenceService.delete("MyCompany", THIRD_EMPLOYEE_USERNAME, LocalDate.of(2015,4,6), LocalDate.of(2015,4,9));
        Absence absence = new Absence();
        absence.setCompany("MyCompany");
        absence.setCategory(AbsenceCategory.TRIP);
        absence.setStartDate(LocalDate.of(2015,4,6));
        absence.setEndDate(LocalDate.of(2015,4,9));
        absence.setUsername(THIRD_EMPLOYEE_USERNAME);
        assertTrue(absenceService.save(absence));
        absenceService.delete("MyCompany", THIRD_EMPLOYEE_USERNAME, LocalDate.of(2015,10,31), LocalDate.of(2015,1,11));
        Absence absence2 = new Absence();
        absence2.setCompany("MyCompany");
        absence2.setCategory(AbsenceCategory.DAY_IN_LIEU);
        absence2.setStartDate(LocalDate.of(2015,10,31));
        absence2.setEndDate(LocalDate.of(2015,11,1));
        absence2.setUsername(THIRD_EMPLOYEE_USERNAME);
        assertTrue(absenceService.save(absence2));
        //Test 2 - days in lieu over multiple years not possible.
        Absence absence3 = new Absence();
        absence3.setCompany("MyCompany");
        absence3.setCategory(AbsenceCategory.DAY_IN_LIEU);
        absence3.setStartDate(LocalDate.of(2015,12,31));
        absence3.setEndDate(LocalDate.of(2016,1,1));
        absence3.setUsername(THIRD_EMPLOYEE_USERNAME);
        assertFalse(absenceService.save(absence3));
        //Test 3 - not enough days in lieu.
        Absence absence4 = new Absence();
        absence4.setCompany("MyCompany");
        absence4.setCategory(AbsenceCategory.DAY_IN_LIEU);
        absence4.setStartDate(LocalDate.of(2015,11,7));
        absence4.setEndDate(LocalDate.of(2016,11,12));
        absence4.setUsername(THIRD_EMPLOYEE_USERNAME);
        assertFalse(absenceService.save(absence4));
    }

}
