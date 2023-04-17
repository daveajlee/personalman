package de.davelee.personalman.api;

import lombok.extern.java.Log;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * This class tests the builder, getter and setter methods of the <code>LoginRequest</code> class.
 */
public class LoginRequestTest {

    @Test
    /**
     * Test the builder and ensure variables are set together using the getter methods.
     */
    public void testBuilder ( ) {
        LoginRequest loginRequest = LoginRequest.builder()
                .username("testuser")
                .password("testpwd")
                .build();
        assertEquals("testuser", loginRequest.getUsername());
        assertEquals("testpwd", loginRequest.getPassword());
    }

    @Test
    /**
     * Test the setter methods and ensure variables are set together using the getter methods.
     */
    public void testSettersAndGetters() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("testuser");
        loginRequest.setPassword("testpwd");
        loginRequest.setCompany("TestBuses");
        assertEquals("testuser", loginRequest.getUsername());
        assertEquals("testpwd", loginRequest.getPassword());
        assertEquals("TestBuses", loginRequest.getCompany());
    }



}
