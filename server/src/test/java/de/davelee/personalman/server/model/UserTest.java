package de.davelee.personalman.server.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * Test cases for the <class>User</class> class which are not covered
 * by other tests.
 * @author Dave Lee
 */
public class UserTest {

    @Test
    /**
     * Test case: build a <code>User</code> object and return string of it.
     * Expected Result: valid values and string.
     */
    public void testBuilderToString() {
        User user = User.builder()
                .firstName("Max")
                .lastName("Mustermann")
                .company("Example Company")
                .userName("mmustermann")
                .position("Tester")
                .dateOfBirth(LocalDate.of(1990,12,31))
                .leaveEntitlementPerYear(25)
                .startDate(LocalDate.of(2020,3,1))
                .workingDays(List.of(DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY, DayOfWeek.FRIDAY))
                .password("test123")
                .role("Admin")
                .contractedHoursPerWeek(40)
                .hourlyWage(new BigDecimal(12))
                .trainingsList(List.of("Certified Tester"))
                .timesheet(Map.of(LocalDate.of(2020,3,1), 8))
                .build();
        assertEquals("Max", user.getFirstName());
        assertEquals("Mustermann", user.getLastName());
        assertEquals("Example Company", user.getCompany());
        assertEquals("mmustermann", user.getUserName());
        assertEquals("Tester", user.getPosition());
        assertEquals(LocalDate.of(1990, 12, 31), user.getDateOfBirth());
        assertEquals(25, user.getLeaveEntitlementPerYear());
        assertEquals(LocalDate.of(2020,3,1), user.getStartDate());
        assertEquals(List.of(DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY, DayOfWeek.FRIDAY), user.getWorkingDays());
        assertEquals("test123", user.getPassword());
        assertEquals("Admin", user.getRole());
        assertEquals(40, user.getContractedHoursPerWeek());
        assertEquals(new BigDecimal(12), user.getHourlyWage());
        assertEquals("Certified Tester", user.getTrainingsList().get(0));
        assertEquals(8, user.getTimesheet().get(LocalDate.of(2020,3,1)));
    }

    @Test
    /**
     * Test case: construct an empty <code>User</code> object
     * fill it with values through setters and return string of it.
     * Expected Result: valid values and string.
     */
    public void testSettersToString() {
        User user = new User();
        user.setFirstName("Max");
        user.setLastName("Mustermann");
        user.setCompany("Example Company");
        user.setUserName("mmustermann");
        user.setPosition("Tester");
        user.setDateOfBirth(LocalDate.of(1990,12,31));
        user.setLeaveEntitlementPerYear(25);
        user.setStartDate(LocalDate.of(2020,3,1));
        user.setWorkingDays(List.of(DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY, DayOfWeek.FRIDAY));
        user.setPassword("test123");
        user.setRole("Admin");
        user.setContractedHoursPerWeek(40);
        user.setHourlyWage(new BigDecimal(12));
        user.setTrainingsList(List.of("Certified Tester"));
        user.setTimesheet(Map.of(LocalDate.of(2020,3,1), 8));
        assertNull(user.getId());
        assertEquals("Max", user.getFirstName());
        assertEquals("Mustermann", user.getLastName());
        assertEquals("Example Company", user.getCompany());
        assertEquals("mmustermann", user.getUserName());
        assertEquals("Tester", user.getPosition());
        assertEquals(LocalDate.of(1990, 12, 31), user.getDateOfBirth());
        assertEquals(25, user.getLeaveEntitlementPerYear());
        assertEquals(LocalDate.of(2020,3,1), user.getStartDate());
        assertEquals(List.of(DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY, DayOfWeek.FRIDAY), user.getWorkingDays());
        assertEquals("test123", user.getPassword());
        assertEquals("Admin", user.getRole());
        assertEquals(40, user.getContractedHoursPerWeek());
        assertEquals(new BigDecimal(12), user.getHourlyWage());
        assertEquals("Certified Tester", user.getTrainingsList().get(0));
        assertEquals(8, user.getTimesheet().get(LocalDate.of(2020,3,1)));

        //Test adding training course - reset to avoid immutable problems.
        user.setTrainingsList(new ArrayList<>());
        user.addTrainingCourse("Fire Safety");
        assertEquals("Fire Safety", user.getTrainingsList().get(0));

        //Test the hours for date method.
        assertEquals(8, user.getHoursForDate(LocalDate.of(2020, 3, 1)));
        assertEquals(0, user.getHoursForDate(LocalDate.of(2020,3,2)));

        //Test adding hours - reset map to avoid immutable problems.
        user.setTimesheet(new HashMap<>());
        user.addHoursForDate(8, LocalDate.of(2020,3,1));
        user.addHoursForDate(1, LocalDate.of(2020,3,1));
        assertEquals(9, user.getHoursForDate(LocalDate.of(2020, 3, 1)));
        user.addHoursForDate(2, LocalDate.of(2020,3,2));
        assertEquals(2, user.getHoursForDate(LocalDate.of(2020, 3, 2)));

    }

}
