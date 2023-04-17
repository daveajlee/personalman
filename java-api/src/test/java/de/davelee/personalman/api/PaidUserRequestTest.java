package de.davelee.personalman.api;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * This class tests the constructor, getter and setter methods of the <code>PaidUserRequest</code> class.
 */
public class PaidUserRequestTest {

    /**
     * Test the constructor and ensure variables are set together using the getter methods.
     */
    @Test
    public void testConstructor() {
        Map<String, Double> employeePayTable = Map.of("max.mustermann", 400.0, "dlee", 1000.0);
        PaidUserRequest paidUserRequest = new PaidUserRequest("MyCompany", employeePayTable, "01-01-2020", "04-01-2020", "dlee-ghgkg" );
        assertEquals("MyCompany", paidUserRequest.getCompany());
        assertEquals(2,paidUserRequest.getEmployeePayTable().size());
        assertEquals("dlee-ghgkg", paidUserRequest.getToken());
        assertEquals("01-01-2020", paidUserRequest.getStartDate());
        assertEquals("04-01-2020", paidUserRequest.getEndDate());
    }

    /**
     * Test the builder and ensure variables are set together using the getter methods.
     */
    @Test
    public void testBuilder() {
        Map<String, Double> employeePayTable = Map.of("max.mustermann", 400.0, "dlee", 1000.0);
        PaidUserRequest paidUserRequest = PaidUserRequest.builder()
                .company("MyCompany")
                .employeePayTable(employeePayTable)
                .token("dlee-ghgkg")
                .startDate("01-01-2020")
                .endDate("04-01-2020")
                .build();
        assertEquals("MyCompany", paidUserRequest.getCompany());
        assertEquals(2,paidUserRequest.getEmployeePayTable().size());
        assertEquals("dlee-ghgkg", paidUserRequest.getToken());
        assertEquals("01-01-2020", paidUserRequest.getStartDate());
        assertEquals("04-01-2020", paidUserRequest.getEndDate());
    }

    /**
     * Test the setter methods and ensure variables are set together using the getter methods.
     */
    @Test
    public void testSettersAndGetters() {
        Map<String, Double> employeePayTable = Map.of("max.mustermann", 400.0, "dlee", 1000.0);
        PaidUserRequest paidUserRequest = new PaidUserRequest();
        paidUserRequest.setCompany("MyCompany");
        assertEquals("MyCompany", paidUserRequest.getCompany());
        paidUserRequest.setEmployeePayTable(employeePayTable);
        assertEquals(2, paidUserRequest.getEmployeePayTable().size());
        paidUserRequest.setToken("dlee-ghgkg");
        assertEquals("dlee-ghgkg", paidUserRequest.getToken());
        paidUserRequest.setStartDate("01-01-2020");
        assertEquals("01-01-2020", paidUserRequest.getStartDate());
        paidUserRequest.setEndDate("04-01-2020");
        assertEquals("04-01-2020", paidUserRequest.getEndDate());
    }

}
