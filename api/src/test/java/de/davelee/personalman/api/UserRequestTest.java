package de.davelee.personalman.api;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * This class tests the constructor, getter and setter methods of the <code>UserRequest</code> class.
 * Created by davelee on 08.02.17.
 */
public class UserRequestTest {

    @Test
    /**
     * Test the setter methods and ensure variables are set together using the getter methods.
     */
    public void testGettersAndSetters() {
        UserRequest userRequest = new UserRequest();
        userRequest.setUsername("david.lee");
        assertEquals("david.lee", userRequest.getUsername());
        userRequest.setCompany("MyCompany");
        assertEquals("MyCompany", userRequest.getCompany());
        userRequest.setStartDate("08-02-2017");
        assertEquals("08-02-2017", userRequest.getStartDate());
        userRequest.setFirstName("David");
        assertEquals("David", userRequest.getFirstName());
        userRequest.setSurname("Lee");
        assertEquals("Lee", userRequest.getSurname());
        userRequest.setLeaveEntitlementPerYear(25);
        assertEquals(25, userRequest.getLeaveEntitlementPerYear());
        userRequest.setPosition("Tester");
        assertEquals("Tester", userRequest.getPosition());
        userRequest.setWorkingDays("Monday,Tuesday");
        assertEquals("Monday,Tuesday", userRequest.getWorkingDays());
    }

}
