package de.davelee.personalman.api;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * This class tests the builder, getter and setter methods of the <code>LogoutRequest</code> class.
 */
public class LogoutRequestTest {

    @Test
    /**
     * Test the builder and ensure variables are set together using the getter methods.
     */
    public void testBuilder ( ) {
        LogoutRequest logoutRequest = LogoutRequest.builder()
                .token("max.mustermann-ghgkg")
                .build();
    }

    @Test
    /**
     * Test the setter methods and ensure variables are set together using the getter methods.
     */
    public void testSettersAndGetters() {
        LogoutRequest logoutRequest = new LogoutRequest();
        logoutRequest.setToken("max.mustermann-ghgkg");
        assertEquals("max.mustermann-ghgkg", logoutRequest.getToken());
    }



}
