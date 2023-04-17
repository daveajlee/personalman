package de.davelee.personalman.api;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * This class tests the constructor, getter and setter methods of the <code>DeactivateUserRequest</code> class.
 */
public class DeactivateUserRequestTest {

    /**
     * Test the constructor and ensure variables are set together using the getter methods.
     */
    @Test
    public void testConstructor() {
        DeactivateUserRequest deactivateUserRequest = new DeactivateUserRequest("MyCompany", "dlee", "dlee-ghgkg", true, "30-06-2020", "Found new job.");
        assertEquals("MyCompany", deactivateUserRequest.getCompany());
        assertEquals("dlee", deactivateUserRequest.getUsername());
        assertEquals("dlee-ghgkg", deactivateUserRequest.getToken());
        assertTrue(deactivateUserRequest.isResigned());
        assertEquals("30-06-2020", deactivateUserRequest.getLeavingDate());
        assertEquals("Found new job.", deactivateUserRequest.getReason());
    }

    /**
     * Test the builder and ensure variables are set together using the getter methods.
     */
    @Test
    public void testBuilder() {
        DeactivateUserRequest deactivateUserRequest = DeactivateUserRequest.builder()
                .company("MyCompany")
                .username("dlee")
                .token("dlee-ghgkg")
                .resigned(false)
                .leavingDate("30-06-2020")
                .reason("Unprofessional behaviour")
                .build();
        assertEquals("MyCompany", deactivateUserRequest.getCompany());
        assertEquals("dlee", deactivateUserRequest.getUsername());
        assertEquals("dlee-ghgkg", deactivateUserRequest.getToken());
        assertFalse(deactivateUserRequest.isResigned());
        assertEquals("30-06-2020", deactivateUserRequest.getLeavingDate());
        assertEquals("Unprofessional behaviour", deactivateUserRequest.getReason());
    }

    /**
     * Test the setter methods and ensure variables are set together using the getter methods.
     */
    @Test
    public void testSettersAndGetters() {
        DeactivateUserRequest deactivateUserRequest = new DeactivateUserRequest();
        deactivateUserRequest.setCompany("MyCompany");
        assertEquals("MyCompany", deactivateUserRequest.getCompany());
        deactivateUserRequest.setUsername("dlee");
        assertEquals("dlee", deactivateUserRequest.getUsername());
        deactivateUserRequest.setToken("dlee-ghgkg");
        assertEquals("dlee-ghgkg", deactivateUserRequest.getToken());
        deactivateUserRequest.setResigned(true);
        assertTrue(deactivateUserRequest.isResigned());
        deactivateUserRequest.setLeavingDate("30-06-2020");
        assertEquals("30-06-2020", deactivateUserRequest.getLeavingDate());
        deactivateUserRequest.setReason("Found a new job");
        assertEquals("Found a new job", deactivateUserRequest.getReason());
    }

}
