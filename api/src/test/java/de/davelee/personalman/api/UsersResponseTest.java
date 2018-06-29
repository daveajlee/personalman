package de.davelee.personalman.api;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * This class tests the constructor, getter and setter methods of the <code>UsersResponse</code> class.
 * Created by davelee on 08.02.17.
 */
public class UsersResponseTest {

    @Test
    /**
     * Test the setter methods and ensure variables are set together using the getter methods.
     */
    public void testGettersAndSetters() {
        UsersResponse usersResponse = new UsersResponse();
        usersResponse.setCount(new Long(1));
        assertEquals(new Long(1),usersResponse.getCount());
        UserResponse[] userResponses = new UserResponse[1];
        userResponses[0] =  new UserResponse("David", "Lee", "david.lee", "MyCompany", 25, "Monday,Tuesday", "Tester", "08-02-2017");
        assertEquals(1, userResponses.length);
        assertEquals("MyCompany", userResponses[0].getCompany());
    }

}
