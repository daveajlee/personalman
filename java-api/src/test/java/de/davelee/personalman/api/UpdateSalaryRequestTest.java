package de.davelee.personalman.api;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * This class tests the constructor, getter and setter methods of the <code>UpdateSalaryRequest</code> class.
 */
public class UpdateSalaryRequestTest {

    @Test
    /**
     * Test the constructor and ensure variables are set together using the getter methods.
     */
    public void testConstructor() {
        UpdateSalaryRequest updateSalaryRequest = new UpdateSalaryRequest("MyCompany", "dlee", "dlee-ghgkg", 12.0, 40);
        assertEquals("MyCompany", updateSalaryRequest.getCompany());
        assertEquals("dlee", updateSalaryRequest.getUsername());
        assertEquals("dlee-ghgkg", updateSalaryRequest.getToken());
        assertEquals(12.0, updateSalaryRequest.getHourlyWage());
        assertEquals(40, updateSalaryRequest.getContractedHoursPerWeek());
    }

    @Test
    /**
     * Test the builder and ensure variables are set together using the getter methods.
     */
    public void testBuilder() {
        UpdateSalaryRequest updateSalaryRequest = UpdateSalaryRequest.builder()
                .company("MyCompany")
                .username("dlee")
                .token("dlee-ghgkg")
                .hourlyWage(12.0)
                .contractedHoursPerWeek(40)
                .build();
        assertEquals("MyCompany", updateSalaryRequest.getCompany());
        assertEquals("dlee", updateSalaryRequest.getUsername());
        assertEquals("dlee-ghgkg", updateSalaryRequest.getToken());
        assertEquals(12.0, updateSalaryRequest.getHourlyWage());
        assertEquals(40, updateSalaryRequest.getContractedHoursPerWeek());
    }

    @Test
    /**
     * Test the setter methods and ensure variables are set together using the getter methods.
     */
    public void testSettersAndGetters() {
        UpdateSalaryRequest updateSalaryRequest = new UpdateSalaryRequest();
        updateSalaryRequest.setCompany("MyCompany");
        assertEquals("MyCompany", updateSalaryRequest.getCompany());
        updateSalaryRequest.setUsername("dlee");
        assertEquals("dlee", updateSalaryRequest.getUsername());
        updateSalaryRequest.setToken("dlee-ghgkg");
        assertEquals("dlee-ghgkg", updateSalaryRequest.getToken());
        updateSalaryRequest.setHourlyWage(12.0);
        assertEquals(12.0, updateSalaryRequest.getHourlyWage());
        updateSalaryRequest.setContractedHoursPerWeek(40);
        assertEquals(40, updateSalaryRequest.getContractedHoursPerWeek());
    }

}
