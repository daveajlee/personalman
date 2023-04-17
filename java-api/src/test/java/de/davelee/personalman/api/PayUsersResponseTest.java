package de.davelee.personalman.api;

import org.junit.jupiter.api.Test;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * This class tests the builder, getter and setter methods of the <code>PayUsersResponse</code> class.
 */
public class PayUsersResponseTest {

    /**
     * Test the builder method and ensure variables are set together using the getter methods.
     */
    @Test
    public void testBuilder() {
        Map<String, Double> employeePayTable = Map.of("max.mustermann", 400.0, "dlee", 1000.0);
        PayUsersResponse payUsersResponse = PayUsersResponse.builder()
                .employeePayTable(employeePayTable)
                .totalSum(1400.0)
                .build();
        assertEquals(2, payUsersResponse.getEmployeePayTable().size());
        assertEquals(1400.0, payUsersResponse.getTotalSum());
    }

    /**
     * Test the setter methods and ensure variables are set together using the getter methods.
     */
    @Test
    public void testGettersAndSetters() {
        Map<String, Double> employeePayTable = Map.of("max.mustermann", 400.0, "dlee", 1000.0);
        PayUsersResponse payUsersResponse = new PayUsersResponse();
        payUsersResponse.setEmployeePayTable(employeePayTable);
        payUsersResponse.setTotalSum(1400.0);
        assertEquals(2, payUsersResponse.getEmployeePayTable().size());
        assertEquals(1400.0, payUsersResponse.getTotalSum());
    }


}
