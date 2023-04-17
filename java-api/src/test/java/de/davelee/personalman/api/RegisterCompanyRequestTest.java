package de.davelee.personalman.api;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * This class tests the builder, getter and setter methods of the <code>RegisterCompanyRequest</code> class.
 */
public class RegisterCompanyRequestTest {

    @Test
    /**
     * Test the builder and ensure variables are set together using the getter methods.
     */
    public void testBuilder ( ) {
        RegisterCompanyRequest registerCompanyRequest = RegisterCompanyRequest.builder()
                .name("Mustermann GmbH")
                .defaultAnnualLeaveInDays(25)
                .country("Germany")
                .build();
        assertEquals("Mustermann GmbH", registerCompanyRequest.getName());
        assertEquals(25, registerCompanyRequest.getDefaultAnnualLeaveInDays());
        assertEquals("Germany", registerCompanyRequest.getCountry());
    }

    @Test
    /**
     * Test the setter methods and ensure variables are set together using the getter methods.
     */
    public void testSettersAndGetters() {
        RegisterCompanyRequest registerCompanyRequest = new RegisterCompanyRequest();
        registerCompanyRequest.setName("Mustermann GmbH");
        registerCompanyRequest.setDefaultAnnualLeaveInDays(25);
        registerCompanyRequest.setCountry("Germany");
        assertEquals("Mustermann GmbH", registerCompanyRequest.getName());
        assertEquals(25, registerCompanyRequest.getDefaultAnnualLeaveInDays());
        assertEquals("Germany", registerCompanyRequest.getCountry());
    }

}
