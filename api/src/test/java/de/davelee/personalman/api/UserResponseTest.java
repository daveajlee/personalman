package de.davelee.personalman.api;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * This class tests the constructor, getter and setter methods of the <code>UserResponse</code> class.
 * Created by davelee on 08.02.17.
 */
public class UserResponseTest {

    @Test
    /**
     * Test the constructor and ensure variables are set together using the getter methods.
     */
    public void testConstructor() {
        UserResponse userResponse = new UserResponse("David", "Lee", "david.lee", "MyCompany", 25, "Monday,Tuesday", "Tester", "08-02-2017");
        assertEquals("david.lee", userResponse.getUsername());
        assertEquals("MyCompany", userResponse.getCompany());
        assertEquals("08-02-2017", userResponse.getStartDate());
        assertEquals("David", userResponse.getFirstName());
        assertEquals("Lee", userResponse.getSurname());
        assertEquals(25, userResponse.getLeaveEntitlementPerYear());
        assertEquals("Tester", userResponse.getPosition());
        assertEquals("Monday,Tuesday", userResponse.getWorkingDays());
    }

    @Test
    /**
     * Test the setter methods and ensure variables are set together using the getter methods.
     */
    public void testGettersAndSetters() {
        UserResponse userResponse = new UserResponse();
        userResponse.setUsername("david.lee");
        assertEquals("david.lee", userResponse.getUsername());
        userResponse.setCompany("MyCompany");
        assertEquals("MyCompany", userResponse.getCompany());
        userResponse.setStartDate("08-02-2017");
        assertEquals("08-02-2017", userResponse.getStartDate());
        userResponse.setFirstName("David");
        assertEquals("David", userResponse.getFirstName());
        userResponse.setSurname("Lee");
        assertEquals("Lee", userResponse.getSurname());
        userResponse.setLeaveEntitlementPerYear(25);
        assertEquals(25, userResponse.getLeaveEntitlementPerYear());
        userResponse.setPosition("Tester");
        assertEquals("Tester", userResponse.getPosition());
        userResponse.setWorkingDays("Monday,Tuesday");
        assertEquals("Monday,Tuesday", userResponse.getWorkingDays());
    }

}
