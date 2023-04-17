package de.davelee.personalman.api;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * This class tests the constructor, getter and setter methods of the <code>DeactivateUserResponse</code> class.
 */
public class DeactivateUserResponseTest {

    @Test
    /**
     * Test the constructor and ensure variables are set together using the getter methods.
     */
    public void testConstructor() {
        DeactivateUserResponse deactivateUserResponse = new DeactivateUserResponse(13);
        assertEquals(13, deactivateUserResponse.getLeaveEntitlementForThisYear());
    }

    @Test
    /**
     * Test the builder and ensure variables are set together using the getter methods.
     */
    public void testBuilder() {
        DeactivateUserResponse deactivateUserResponse = DeactivateUserResponse.builder()
                .leaveEntitlementForThisYear(12)
                .build();
        assertEquals(12, deactivateUserResponse.getLeaveEntitlementForThisYear());
    }

    @Test
    /**
     * Test the setter methods and ensure variables are set together using the getter methods.
     */
    public void testSettersAndGetters() {
        DeactivateUserResponse deactivateUserResponse = new DeactivateUserResponse();
        deactivateUserResponse.setLeaveEntitlementForThisYear(11);
        assertEquals(11, deactivateUserResponse.getLeaveEntitlementForThisYear());
    }

}
