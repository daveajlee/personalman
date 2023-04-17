package de.davelee.personalman.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * This class is part of the PersonalMan REST API. It represents a request for an absence containing company and
 * username of the person requesting the absence as well as the start date, end date and category of the requested
 * absence.
 * @author Dave Lee
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AbsenceRequest {

    // Which company the person making the absence request works for
    private String company;

    // The username of the person making the absence request
    private String username;

    // The start date of the absence in the format dd-mm-yyyy
    private String startDate;

    // The end date of the absence in the format dd-mm-yyyy
    private String endDate;

    // The category of the absence
    private String category;

    // The token of the user to verify that they are logged in
    private String token;

}
