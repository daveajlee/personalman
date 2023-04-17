package de.davelee.personalman.api;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * This class tests the constructor, getter and setter methods of the <code>AbsenceRequest</code> class.
 */
public class AbsenceRequestTest {

    @Test
    /**
     * Test the constructor and ensure variables are set together using the getter methods.
     */
    public void testConstructor() {
        AbsenceRequest absenceRequest = new AbsenceRequest("MyCompany", "dlee", "07-02-2017", "08-02-2017", "Holiday", "dlee-ejekrj");
        assertEquals("MyCompany", absenceRequest.getCompany());
        assertEquals("dlee", absenceRequest.getUsername());
        assertEquals("07-02-2017", absenceRequest.getStartDate());
        assertEquals("08-02-2017", absenceRequest.getEndDate());
        assertEquals("Holiday", absenceRequest.getCategory());
        assertEquals("dlee-ejekrj", absenceRequest.getToken());
    }

    @Test
    /**
     * Test the builder and ensure variables are set together using the getter methods.
     */
    public void testBuilder() {
        AbsenceRequest absenceRequest = AbsenceRequest.builder()
                .company("MyCompany")
                .username("dlee")
                .startDate("07-02-2017")
                .endDate("08-02-2017")
                .category("Holiday")
                .token("dlee-ejekrj").build();
        assertEquals("MyCompany", absenceRequest.getCompany());
        assertEquals("dlee", absenceRequest.getUsername());
        assertEquals("07-02-2017", absenceRequest.getStartDate());
        assertEquals("08-02-2017", absenceRequest.getEndDate());
        assertEquals("Holiday", absenceRequest.getCategory());
        assertEquals("dlee-ejekrj", absenceRequest.getToken());
    }

    @Test
    /**
     * Test the setter methods and ensure variables are set together using the getter methods.
     */
    public void testSettersAndGetters() {
        AbsenceRequest absenceRequest = new AbsenceRequest();
        absenceRequest.setCompany("MyCompany");
        assertEquals("MyCompany", absenceRequest.getCompany());
        absenceRequest.setUsername("david.lee");
        assertEquals("david.lee", absenceRequest.getUsername());
        absenceRequest.setStartDate("06-02-2017");
        assertEquals("06-02-2017", absenceRequest.getStartDate());
        absenceRequest.setEndDate("07-02-2017");
        assertEquals("07-02-2017", absenceRequest.getEndDate());
        absenceRequest.setCategory("Illness");
        assertEquals("Illness", absenceRequest.getCategory());
        absenceRequest.setToken("dlee-ejekrk");
        assertEquals("dlee-ejekrk", absenceRequest.getToken());
    }

}
