package de.davelee.personalman.api;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * This class tests the constructor, getter and setter methods of the <code>AddTimeSheetHoursRequest</code> class.
 */
public class AddTimeSheetHoursRequestTest {

    @Test
    /**
     * Test the constructor and ensure variables are set together using the getter methods.
     */
    public void testConstructor() {
        AddTimeSheetHoursRequest addTimeSheetHoursRequest = new AddTimeSheetHoursRequest("MyCompany", "dlee", "dlee-ghgkg", "07-02-2017", 20);
        assertEquals("MyCompany", addTimeSheetHoursRequest.getCompany());
        assertEquals("dlee", addTimeSheetHoursRequest.getUsername());
        assertEquals("dlee-ghgkg", addTimeSheetHoursRequest.getToken());
        assertEquals("07-02-2017", addTimeSheetHoursRequest.getDate());
        assertEquals(20, addTimeSheetHoursRequest.getHours());
    }

    @Test
    /**
     * Test the builder and ensure variables are set together using the getter methods.
     */
    public void testBuilder() {
        AddTimeSheetHoursRequest addTimeSheetHoursRequest = AddTimeSheetHoursRequest.builder()
                .company("MyCompany")
                .username("dlee")
                .token("dlee-ghgkg")
                .date("07-02-2017")
                .hours(20)
                .build();
        assertEquals("MyCompany", addTimeSheetHoursRequest.getCompany());
        assertEquals("dlee", addTimeSheetHoursRequest.getUsername());
        assertEquals("dlee-ghgkg", addTimeSheetHoursRequest.getToken());
        assertEquals("07-02-2017", addTimeSheetHoursRequest.getDate());
        assertEquals(20, addTimeSheetHoursRequest.getHours());
    }

    @Test
    /**
     * Test the setter methods and ensure variables are set together using the getter methods.
     */
    public void testSettersAndGetters() {
        AddTimeSheetHoursRequest addTimeSheetHoursRequest = new AddTimeSheetHoursRequest();
        addTimeSheetHoursRequest.setCompany("MyCompany");
        assertEquals("MyCompany", addTimeSheetHoursRequest.getCompany());
        addTimeSheetHoursRequest.setUsername("dlee");
        assertEquals("dlee", addTimeSheetHoursRequest.getUsername());
        addTimeSheetHoursRequest.setToken("dlee-ghgkg");
        assertEquals("dlee-ghgkg", addTimeSheetHoursRequest.getToken());
        addTimeSheetHoursRequest.setDate("17-02-2017");
        assertEquals("17-02-2017", addTimeSheetHoursRequest.getDate());
        addTimeSheetHoursRequest.setHours(20);
        assertEquals(20, addTimeSheetHoursRequest.getHours());
    }

}
