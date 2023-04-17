package de.davelee.personalman.api;

import lombok.*;

/**
 * This class is part of the PersonalMan REST API. It represents a request to add a number of hours to a particular date
 * to the user's timesheet.
 * @author Dave Lee
 */
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class AddTimeSheetHoursRequest {

    //company that the person works for
    private String company;

    //username of the person
    private String username;

    //token of the user making the change
    private String token;

    //the date to add the hours to in format dd-MM-yyyy.
    private String date;

    //the number of hours to add
    private int hours;

}
