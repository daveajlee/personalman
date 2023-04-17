package de.davelee.personalman.api;

import lombok.*;
import java.util.Map;

/**
 * This class is part of the PersonalMan REST API. It represents a request to the server to mark the employees
 * as paid with the amount supplied.
 * @author Dave Lee
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PaidUserRequest {

    // company associated with
    private String company;

    // a table of employee by username and the amount of pay they have received.
    private Map<String, Double> employeePayTable;

    // start date of date range that they were paid for in format dd-MM-yyyy
    private String startDate;

    // end date of date range that they were paid for in format dd-MM-yyyy
    private String endDate;

    // The token of the user to verify that they are logged in
    private String token;

}
