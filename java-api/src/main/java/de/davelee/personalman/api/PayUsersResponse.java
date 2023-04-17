package de.davelee.personalman.api;

import lombok.*;

import java.util.Map;

/**
 * This class is part of the PersonalMan REST API. It represents a response from the server containing details
 * of which employees (per username) should receive which pay as well as the total sum that should be paid.
 * @author Dave Lee
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PayUsersResponse {

    // a table of employee by username and the amount of pay they should receive.
    private Map<String, Double> employeePayTable;

    // total sum that should be paid out
    private Double totalSum;

}
