package de.davelee.personalman.api;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * This class tests the constructor, getter and setter methods of the <code>AddHistoryEntryRequest</code> class.
 */
public class AddHistoryEntryRequestTest {

    @Test
    /**
     * Test the constructor and ensure variables are set together using the getter methods.
     */
    public void testConstructor() {
        AddHistoryEntryRequest addHistoryEntryRequest = new AddHistoryEntryRequest("MyCompany", "dlee", "dlee-ghgkg", "01-03-2020", "Joined", "Welcome to the company!" );
        assertEquals("MyCompany", addHistoryEntryRequest.getCompany());
        assertEquals("dlee", addHistoryEntryRequest.getUsername());
        assertEquals("dlee-ghgkg", addHistoryEntryRequest.getToken());
        assertEquals("01-03-2020", addHistoryEntryRequest.getDate());
        assertEquals("Joined", addHistoryEntryRequest.getReason());
        assertEquals("Welcome to the company!", addHistoryEntryRequest.getComment());
    }

    @Test
    /**
     * Test the builder and ensure variables are set together using the getter methods.
     */
    public void testBuilder() {
        AddHistoryEntryRequest addHistoryEntryRequest = AddHistoryEntryRequest.builder()
                .company("MyCompany")
                .username("dlee")
                .token("dlee-ghgkg")
                .date("07-02-2017")
                .reason("Joined")
                .comment("Welcome to the company!")
                .build();
        assertEquals("MyCompany", addHistoryEntryRequest.getCompany());
        assertEquals("dlee", addHistoryEntryRequest.getUsername());
        assertEquals("dlee-ghgkg", addHistoryEntryRequest.getToken());
        assertEquals("07-02-2017", addHistoryEntryRequest.getDate());
        assertEquals("Joined", addHistoryEntryRequest.getReason());
        assertEquals("Welcome to the company!", addHistoryEntryRequest.getComment());
    }

    @Test
    /**
     * Test the setter methods and ensure variables are set together using the getter methods.
     */
    public void testSettersAndGetters() {
        AddHistoryEntryRequest addHistoryEntryRequest = new AddHistoryEntryRequest();
        addHistoryEntryRequest.setCompany("MyCompany");
        assertEquals("MyCompany", addHistoryEntryRequest.getCompany());
        addHistoryEntryRequest.setUsername("dlee");
        assertEquals("dlee",addHistoryEntryRequest.getUsername());
        addHistoryEntryRequest.setToken("dlee-ghgkg");
        assertEquals("dlee-ghgkg", addHistoryEntryRequest.getToken());
        addHistoryEntryRequest.setDate("17-02-2017");
        assertEquals("17-02-2017", addHistoryEntryRequest.getDate());
        addHistoryEntryRequest.setReason("Sacked");
        assertEquals("Sacked", addHistoryEntryRequest.getReason());
        addHistoryEntryRequest.setComment("Because of bad behaviour");
        assertEquals("Because of bad behaviour", addHistoryEntryRequest.getComment());
    }

}
