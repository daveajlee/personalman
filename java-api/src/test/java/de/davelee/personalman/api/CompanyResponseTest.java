package de.davelee.personalman.api;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * This class tests the constructor, getter and setter methods of the <code>CompanyResponse</code> class.
 */
public class CompanyResponseTest {

    @Test
    /**
     * Test the constructor and ensure variables are set together using the getter methods.
     */
    public void testConstructor() {
        CompanyResponse companyResponse = new CompanyResponse("TestBuses", 25, "Germany");
        assertEquals("TestBuses", companyResponse.getName());
        assertEquals(25, companyResponse.getDefaultAnnualLeaveInDays());
        assertEquals("Germany", companyResponse.getCountry());
    }

    @Test
    /**
     * Test the builder and ensure variables are set together using the getter methods.
     */
    public void testBuilder() {
        CompanyResponse companyResponse = CompanyResponse.builder()
                .name("TestBuses")
                .defaultAnnualLeaveInDays(25)
                .country("Germany")
                .build();
        assertEquals("TestBuses", companyResponse.getName());
        assertEquals(25, companyResponse.getDefaultAnnualLeaveInDays());
        assertEquals("Germany", companyResponse.getCountry());
    }

    @Test
    /**
     * Test the setter methods and ensure variables are set together using the getter methods.
     */
    public void testSettersAndGetters() {
       CompanyResponse companyResponse = new CompanyResponse();
       companyResponse.setName("TestBuses");
       assertEquals("TestBuses", companyResponse.getName());
       companyResponse.setDefaultAnnualLeaveInDays(25);
       assertEquals(25, companyResponse.getDefaultAnnualLeaveInDays());
       companyResponse.setCountry("Germany");
       assertEquals("Germany", companyResponse.getCountry());
    }

}
