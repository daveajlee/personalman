package de.davelee.personalman.api;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * This class tests the constructor, getter and setter methods of the <code>UserRequest</code> class.
 * Created by davelee on 08.02.17.
 */
public class UserRequestTest {

    /**
     * Test the builder method and ensure variables are set together using the getter methods.
     */
    @Test
    public void testBuilder() {
        UserRequest userRequest = UserRequest.builder()
                .username("david.lee")
                .password("test123")
                .company("MyCompany")
                .startDate("08-02-2017")
                .firstName("David")
                .surname("Lee")
                .leaveEntitlementPerYear(25)
                .position("Tester")
                .role("Admin")
                .workingDays("Monday,Tuesday")
                .dateOfBirth("01-01-1993")
                .build();
        assertEquals("david.lee", userRequest.getUsername());
        assertEquals("test123", userRequest.getPassword());
        assertEquals("MyCompany", userRequest.getCompany());
        assertEquals("08-02-2017", userRequest.getStartDate());
        assertEquals("David", userRequest.getFirstName());
        assertEquals("Lee", userRequest.getSurname());
        assertEquals(25, userRequest.getLeaveEntitlementPerYear());
        assertEquals("Tester", userRequest.getPosition());
        assertEquals("Admin", userRequest.getRole());
        assertEquals("Monday,Tuesday", userRequest.getWorkingDays());
        assertEquals("01-01-1993", userRequest.getDateOfBirth());
        assertEquals("UserRequest(firstName=David, surname=Lee, username=david.lee, password=test123, company=MyCompany, leaveEntitlementPerYear=25, workingDays=Monday,Tuesday, position=Tester, startDate=08-02-2017, role=Admin, dateOfBirth=01-01-1993)", userRequest.toString());
    }

    /**
     * Test the constructor method and ensure variables are set together using the getter methods.
     */
    @Test
    public void testGettersAndSetters() {
        UserRequest userRequest = new UserRequest();
        userRequest.setUsername("david.lee");
        userRequest.setPassword("test123");
        userRequest.setCompany("MyCompany");
        userRequest.setStartDate("08-02-2017");
        userRequest.setFirstName("David");
        userRequest.setSurname("Lee");
        userRequest.setLeaveEntitlementPerYear(25);
        userRequest.setPosition("Tester");
        userRequest.setRole("Admin");
        userRequest.setWorkingDays("Monday,Tuesday");
        userRequest.setDateOfBirth("01-01-1993");
        assertEquals("david.lee", userRequest.getUsername());
        assertEquals("test123", userRequest.getPassword());
        assertEquals("MyCompany", userRequest.getCompany());
        assertEquals("08-02-2017", userRequest.getStartDate());
        assertEquals("David", userRequest.getFirstName());
        assertEquals("Lee", userRequest.getSurname());
        assertEquals(25, userRequest.getLeaveEntitlementPerYear());
        assertEquals("Tester", userRequest.getPosition());
        assertEquals("Admin", userRequest.getRole());
        assertEquals("Monday,Tuesday", userRequest.getWorkingDays());
        assertEquals("01-01-1993", userRequest.getDateOfBirth());
        assertEquals("UserRequest(firstName=David, surname=Lee, username=david.lee, password=test123, company=MyCompany, leaveEntitlementPerYear=25, workingDays=Monday,Tuesday, position=Tester, startDate=08-02-2017, role=Admin, dateOfBirth=01-01-1993)", userRequest.toString());
    }

}
