package de.davelee.personalman.api;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * This class tests the builder, getter and setter methods of the <code>LoginResponse</code> class.
 */
public class LoginResponseTest {

    @Test
    public void testGoodLogin( ) {
        LoginResponse loginResponse = LoginResponse.builder()
                .token("dlee-ghgkg")
                .build();
        assertEquals("dlee-ghgkg", loginResponse.getToken());
        assertNull(loginResponse.getErrorMessage());
    }

    @Test
    public void testBadLogin( ) {
        LoginResponse loginResponse = LoginResponse.builder()
                .errorMessage("Password was incorrect!")
                .build();
        assertEquals("Password was incorrect!", loginResponse.getErrorMessage());
        assertNull(loginResponse.getToken());
    }

    @Test
    public void testGoodLoginWithSetters( ) {
        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setToken("dlee-ghgkg");
        assertEquals("dlee-ghgkg", loginResponse.getToken());
        assertNull(loginResponse.getErrorMessage());
    }

    @Test
    public void testBadLogiWithSetters( ) {
        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setErrorMessage("Password was incorrect!");
        assertEquals("Password was incorrect!", loginResponse.getErrorMessage());
        assertNull(loginResponse.getToken());
    }

}
