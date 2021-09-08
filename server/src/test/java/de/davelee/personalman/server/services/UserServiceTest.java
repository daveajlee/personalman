package de.davelee.personalman.server.services;

import de.davelee.personalman.server.model.User;
import de.davelee.personalman.server.repository.UserRepository;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test cases for the UserService class - the UserRepository is mocked.
 * @author Dave Lee
 */
@SpringBootTest(properties = { "logout.minutes=30","token.length=10"})
public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    /**
     * Initialise the spring properties which otherwise with Mockito would not be set
     * @throws Exception if the fields cannot be set
     */
    @BeforeEach
    public void setSpringProperties() throws Exception {
        FieldUtils.writeField(userService, "timeoutInMinutes", 30, true);
        FieldUtils.writeField(userService, "tokenLength", 10, true);
    }

    /**
     * Test case: save a new user.
     * Expected Result: true.
     */
    @Test
    public void testSaveUser() {
        //Test data
        User user = generateValidUser();
        //Mock important method in repository.
        Mockito.when(userRepository.save(user)).thenReturn(user);
        //do actual test.
        assertTrue(userService.save(user));
    }

    /**
     * Test case: find a user by company and username.
     * Expected Result: user is not null.
     */
    @Test
    public void testFindUserByCompanyAndUserName() {
        //Test data
        User user = generateValidUser();
        //Mock important method in repository.
        Mockito.when(userRepository.findByCompanyAndUserName("Example Company", "max.mustermann")).thenReturn(user);
        //do actual test.
        assertNotNull(userService.findByCompanyAndUserName("Example Company", "max.mustermann"));
    }

    /**
     * Test case: find all users of a company.
     * Expected Result: list has size of 1.
     */
    @Test
    public void testFindUserByCompany() {
        //Test data
        User user = generateValidUser();
        //Mock important method in repository.
        Mockito.when(userRepository.findByCompany("Example Company")).thenReturn(List.of(user));
        //do actual test.
        assertEquals(userService.findByCompany("Example Company").size(), 1);
    }

    /**
     * Test case: delete a user.
     * Currently void so no validation possible.
     */
    @Test
    public void testDeleteUser() {
        //Test data
        User user = generateValidUser();
        //Mock important method in repository.
        Mockito.doNothing().when(userRepository).delete(user);
        //do actual test.
        userService.delete(user);
    }

    /**
     * Test case: update salary information.
     * Expected result: true.
     */
    @Test
    public void testUpdateSalaryInformation() {
        //Test data
        User user = generateValidUser();
        //Mock important method in repository.
        Mockito.when(userRepository.save(user)).thenReturn(user);
        //do actual test.
        assertTrue(userService.updateSalaryInformation(user, new BigDecimal(12), 40));
    }

    /**
     * Test case: add training course / qualification information.
     * Expected result: true.
     */
    @Test
    public void testAddTrainingCourse() {
        //Test data
        User user = generateValidUser();
        //Mock important method in repository.
        Mockito.when(userRepository.save(user)).thenReturn(user);
        //do actual test.
        assertTrue(userService.addTrainingCourse(user, "Certified Tester"));
        assertTrue(userService.addTrainingCourse(user, "Certified Developer"));
    }

    /**
     * Test case: add timesheet hours.
     * Expected result: true.
     */
    @Test
    public void testAddHoursForDate() {
        //Test data
        User user = generateValidUser();
        //Mock important method in repository.
        Mockito.when(userRepository.save(user)).thenReturn(user);
        //do actual test.
        assertTrue(userService.addHoursForDate(user, 8, LocalDate.of(2020,3,1) ));
        assertTrue(userService.addHoursForDate(user, 1, LocalDate.of(2020,3,1) ));
    }

    /**
     * Test case: get timesheet hours for date ranges.
     * Expected result: true.
     */
    @Test
    public void testGetHoursForDateRanges() {
        //Test data
        User user = generateValidUser();
        userService.addHoursForDate(user, 4, LocalDate.of(2020,3,1));
        userService.addHoursForDate(user, 8, LocalDate.of(2020,3,3));
        //do actual test.
        assertEquals(12, userService.getHoursForDateRange(user, LocalDate.of(2020,3,1), LocalDate.of(2020,3,4)));
    }

    /**
     * Test case: generate a token.
     * Expected result: string is not null.
     */
    @Test
    public void testAuthTokens() {
        //do actual test.
        String token = userService.generateAuthToken("max.mustermann");
        assertNotNull(token);
        assertTrue(userService.checkAuthToken(token));
        userService.removeAuthToken(token);
    }

    /**
     * Test case: reset user psssword
     * Expected result: true if user could be found or false if not.
     */
    @Test
    public void testResetUserPassword() {
        //Test data
        User user = generateValidUser();
        //Mock important method in repository.
        Mockito.when(userRepository.findByCompanyAndUserName("Example Company", "max.mustermann")).thenReturn(user);
        Mockito.when(userRepository.findByCompanyAndUserName("Example Company 2", "max.mustermann")).thenReturn(null);
        Mockito.when(userRepository.save(user)).thenReturn(user);
        //do actual test.
        assertTrue(userService.resetUserPassword("Example Company", "max.mustermann", "123test"));
        assertFalse(userService.resetUserPassword("Example Company 2", "max.mustermann", "123test"));
    }

    /**
     * Test case: change user psssword
     * Expected result: true if user could be found or false if not.
     */
    @Test
    public void testChangeUserPassword() {
        //Test data
        User user = generateValidUser();
        //Mock important method in repository.
        Mockito.when(userRepository.findByCompanyAndUserName("Example Company", "max.mustermann")).thenReturn(user);
        Mockito.when(userRepository.findByCompanyAndUserName("Example Company 2", "max.mustermann")).thenReturn(null);
        Mockito.when(userRepository.save(user)).thenReturn(user);
        //do actual test.
        assertTrue(userService.changePassword("Example Company", "max.mustermann", user.getPassword(), "123test"));
        assertFalse(userService.changePassword("Example Company 2", "max.mustermann", user.getPassword(), "123test"));
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
