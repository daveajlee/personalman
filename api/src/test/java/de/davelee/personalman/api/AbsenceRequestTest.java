package de.davelee.personalman.api;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * This class tests the constructor, getter and setter methods of the <code>AbsenceRequest</code> class.
 * Created by davelee on 07.02.17.
 */
public class AbsenceRequestTest {

    @Test
    /**
     * Test the constructor and ensure variables are set together using the getter methods.
     */
    public void testConstructor() {
        AbsenceRequest absenceRequest = new AbsenceRequest("MyCompany", "dlee", "07-02-2017", "08-02-2017", "Holiday");
        assertEquals("MyCompany", absenceRequest.getCompany());
        assertEquals("dlee", absenceRequest.getUsername());
        assertEquals("07-02-2017", absenceRequest.getStartDate());
        assertEquals("08-02-2017", absenceRequest.getEndDate());
        assertEquals("Holiday", absenceRequest.getCategory());
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
    }

}
