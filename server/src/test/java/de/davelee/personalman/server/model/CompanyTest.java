package de.davelee.personalman.server.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test cases for the <class>Company</class> class which are not covered
 * by other tests.
 * @author Dave Lee
 */
public class CompanyTest {

    @Test
    /**
     * Test case: build a <code>Company</code> object and return string of it.
     * Expected Result: valid values and string.
     */
    public void testBuilderToString() {
        Company company = Company.builder()
                .country("Germany")
                .defaultAnnualLeaveInDays(25)
                .name("Test Company")
                .build();
        assertEquals("Test Company", company.getName());
        assertEquals(25, company.getDefaultAnnualLeaveInDays());
        assertEquals("Germany", company.getCountry());
        assertEquals("Company(id=null, name=Test Company, defaultAnnualLeaveInDays=25, country=Germany)", company.toString());
    }

    @Test
    /**
     * Test case: construct an empty <code>Company</code> object
     * fill it with values through setters and return string of it.
     * Expected Result: valid values and string.
     */
    public void testSettersToString() {
        Company company = new Company();
        company.setCountry("Germany");
        company.setDefaultAnnualLeaveInDays(25);
        company.setName("Test Company");
        assertEquals("Test Company", company.getName());
        assertEquals(25, company.getDefaultAnnualLeaveInDays());
        assertEquals("Germany", company.getCountry());
        assertEquals("Company(id=null, name=Test Company, defaultAnnualLeaveInDays=25, country=Germany)", company.toString());
    }

}
