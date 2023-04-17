package de.davelee.personalman.api;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * This class tests the builder, getter and setter methods of the <code>RegisterUserRequest</code> class.
 */
public class RegisterUserRequestTest {

    @Test
    /**
     * Test the builder and ensure variables are set together using the getter methods.
     */
    public void testBuilder ( ) {
        RegisterUserRequest registerUserRequest = RegisterUserRequest.builder()
                .firstName("Max")
                .surname("Mustermann")
                .company("Mustermann GmbH")
                .username("mmustermann")
                .password("pwd")
                .role("Employee")
                .position("Geschäftsführer")
                .workingDays("Monday,Tuesday,Wednesday,Thursday,Friday")
                .dateOfBirth("17-01-1990")
                .build();
        assertEquals("Max", registerUserRequest.getFirstName());
        assertEquals("Mustermann", registerUserRequest.getSurname());
        assertEquals("Mustermann GmbH", registerUserRequest.getCompany());
        assertEquals("mmustermann", registerUserRequest.getUsername());
        assertEquals("pwd", registerUserRequest.getPassword());
        assertEquals("Employee", registerUserRequest.getRole());
        assertEquals("Geschäftsführer", registerUserRequest.getPosition());
        assertEquals("Monday,Tuesday,Wednesday,Thursday,Friday", registerUserRequest.getWorkingDays());
        assertEquals("17-01-1990", registerUserRequest.getDateOfBirth());
    }

    @Test
    /**
     * Test the setter methods and ensure variables are set together using the getter methods.
     */
    public void testSettersAndGetters() {
        RegisterUserRequest registerUserRequest = new RegisterUserRequest();
        registerUserRequest.setFirstName("Max");
        registerUserRequest.setSurname("Mustermann");
        registerUserRequest.setCompany("Mustermann GmbH");
        registerUserRequest.setUsername("mmustermann");
        registerUserRequest.setPassword("pwd");
        registerUserRequest.setRole("Employee");
        registerUserRequest.setPosition("Geschäftsführer");
        registerUserRequest.setWorkingDays("Monday,Tuesday,Wednesday,Thursday,Friday");
        registerUserRequest.setDateOfBirth("17-01-1990");
        assertEquals("Max", registerUserRequest.getFirstName());
        assertEquals("Mustermann", registerUserRequest.getSurname());
        assertEquals("Mustermann GmbH", registerUserRequest.getCompany());
        assertEquals("mmustermann", registerUserRequest.getUsername());
        assertEquals("pwd", registerUserRequest.getPassword());
        assertEquals("Employee", registerUserRequest.getRole());
        assertEquals("Geschäftsführer", registerUserRequest.getPosition());
        assertEquals("Monday,Tuesday,Wednesday,Thursday,Friday", registerUserRequest.getWorkingDays());
        assertEquals("17-01-1990", registerUserRequest.getDateOfBirth());
    }

}
