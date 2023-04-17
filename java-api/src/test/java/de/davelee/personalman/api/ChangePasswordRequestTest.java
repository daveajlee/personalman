package de.davelee.personalman.api;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * This class tests the constructor, getter and setter methods of the <code>ChangePasswordRequest</code> class.
 */
public class ChangePasswordRequestTest {

    @Test
    /**
     * Test the constructor and ensure variables are set together using the getter methods.
     */
    public void testConstructor() {
        ChangePasswordRequest changePasswordRequest = new ChangePasswordRequest("MyCompany", "dlee", "dlee-ghgkg", "test123", "super1strong");
        assertEquals("MyCompany", changePasswordRequest.getCompany());
        assertEquals("dlee", changePasswordRequest.getUsername());
        assertEquals("dlee-ghgkg", changePasswordRequest.getToken());
        assertEquals("test123", changePasswordRequest.getCurrentPassword());
        assertEquals("super1strong", changePasswordRequest.getNewPassword());
    }

    @Test
    /**
     * Test the builder and ensure variables are set together using the getter methods.
     */
    public void testBuilder() {
        ChangePasswordRequest changePasswordRequest = ChangePasswordRequest.builder()
                .company("MyCompany")
                .username("dlee")
                .token("dlee-ghgkg")
                .currentPassword("test123")
                .newPassword("super1strong")
                .build();
        assertEquals("MyCompany", changePasswordRequest.getCompany());
        assertEquals("dlee", changePasswordRequest.getUsername());
        assertEquals("dlee-ghgkg", changePasswordRequest.getToken());
        assertEquals("test123", changePasswordRequest.getCurrentPassword());
        assertEquals("super1strong", changePasswordRequest.getNewPassword());
    }

    @Test
    /**
     * Test the setter methods and ensure variables are set together using the getter methods.
     */
    public void testSettersAndGetters() {
        ChangePasswordRequest changePasswordRequest = new ChangePasswordRequest();
        changePasswordRequest.setCompany("MyCompany");
        assertEquals("MyCompany", changePasswordRequest.getCompany());
        changePasswordRequest.setUsername("dlee");
        assertEquals("dlee", changePasswordRequest.getUsername());
        changePasswordRequest.setToken("dlee-ghgkg");
        assertEquals("dlee-ghgkg", changePasswordRequest.getToken());
        changePasswordRequest.setCurrentPassword("test123");
        assertEquals("test123", changePasswordRequest.getCurrentPassword());
        changePasswordRequest.setNewPassword("strong1secure");
        assertEquals("strong1secure", changePasswordRequest.getNewPassword());
    }

}
