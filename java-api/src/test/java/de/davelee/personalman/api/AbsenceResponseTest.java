package de.davelee.personalman.api;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * This class tests the constructor, getter and setter methods of the <code>AbsenceResponse</code> class.
 * Created by davelee on 07.02.17.
 */
public class AbsenceResponseTest {

    @Test
    /**
     * Test the constructor and ensure variables are set together using the getter methods.
     */
    public void testConstructor() {
        AbsenceResponse absenceResponse = new AbsenceResponse("MyCompany", "dlee", "07-02-2017", "08-02-2017", "Holiday");
        assertEquals("MyCompany", absenceResponse.getCompany());
        assertEquals("dlee", absenceResponse.getUsername());
        assertEquals("07-02-2017", absenceResponse.getStartDate());
        assertEquals("08-02-2017", absenceResponse.getEndDate());
        assertEquals("Holiday", absenceResponse.getCategory());
    }

    @Test
    /**
     * Test the builder and ensure variables are set together using the getter methods.
     */
    public void testBuilder() {
        AbsenceResponse absenceResponse = AbsenceResponse.builder()
                .company("MyCompany")
                .username("dlee")
                .startDate("07-02-2017")
                .endDate("08-02-2017")
                .category("Holiday")
                .build();
        assertEquals("MyCompany", absenceResponse.getCompany());
        assertEquals("dlee", absenceResponse.getUsername());
        assertEquals("07-02-2017", absenceResponse.getStartDate());
        assertEquals("08-02-2017", absenceResponse.getEndDate());
        assertEquals("Holiday", absenceResponse.getCategory());
    }

    @Test
    /**
     * Test the setter methods and ensure variables are set together using the getter methods.
     */
    public void testSettersAndGetters() {
        AbsenceResponse absenceResponse = new AbsenceResponse();
        absenceResponse.setCompany("MyCompany");
        assertEquals("MyCompany", absenceResponse.getCompany());
        absenceResponse.setUsername("david.lee");
        assertEquals("david.lee", absenceResponse.getUsername());
        absenceResponse.setStartDate("06-02-2017");
        assertEquals("06-02-2017", absenceResponse.getStartDate());
        absenceResponse.setEndDate("07-02-2017");
        assertEquals("07-02-2017", absenceResponse.getEndDate());
        absenceResponse.setCategory("Illness");
        assertEquals("Illness", absenceResponse.getCategory());
    }

    @Test
    /**
     * Test the toString method by running the constructor and then calling toString.
     */
    public void testToString() {
        AbsenceResponse absenceResponse = new AbsenceResponse("MyCompany", "dlee", "07-02-2017", "08-02-2017", "Holiday");
        assertEquals("AbsenceResponse(company=MyCompany, username=dlee, startDate=07-02-2017, endDate=08-02-2017, category=Holiday)", absenceResponse.toString());
    }

}
