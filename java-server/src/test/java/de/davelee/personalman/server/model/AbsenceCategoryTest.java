package de.davelee.personalman.server.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * Test cases for the <code>AbsenceCategory</code> enum which are not covered
 * by other tests.
 * @author Dave Lee
 */
public class AbsenceCategoryTest {

    @Test
    /**
     * Test case: attempt to convert a string into a <code>AbsenceCategory</code> object.
     * Expected Result: one valid and one null object.
     */
    public void testFromString() {
        assertEquals(AbsenceCategory.TRIP, AbsenceCategory.fromString("Trip"));
        assertNull(AbsenceCategory.fromString("Trip2"));
    }

}
