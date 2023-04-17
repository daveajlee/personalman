package de.davelee.personalman.api;

import lombok.*;

/**
 * This class is part of the PersonalMan REST API. It represents a request for a registration of a new user.
 * @author Dave Lee
 */
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class UpdateSalaryRequest {

    //company that the person works for
    private String company;

    //username of the person
    private String username;

    //token of the user making the change
    private String token;

    //hourly wage that the person should get
    private Double hourlyWage;

    //number of hour person works per week
    private int contractedHoursPerWeek;

}
