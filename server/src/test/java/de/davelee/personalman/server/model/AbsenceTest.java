package de.davelee.personalman.server.model;

import org.apache.tomcat.jni.Local;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test cases for the <code>Absence</code> class which are not covered
 * by other tests.
 * @author Dave Lee
 */
public class AbsenceTest {

    @Test
    /**
     * Test case: build a <code>Absence/code> object and return a valid string of it.
     * Expected Result: valid string.
     */
    public void testToString() {
        Absence absence = Absence.builder()
                .category(AbsenceCategory.HOLIDAY)
                .company("Example Company")
                .startDate(LocalDate.of(2017,7,10))
                .endDate(LocalDate.of(2017, 7, 10))
                .username("testusr")
                .build();
        assertEquals("Absence(id=null, username=testusr, company=Example Company, startDate=2017-07-10, endDate=2017-07-10, category=Holiday)", absence.toString() );
    }

}
