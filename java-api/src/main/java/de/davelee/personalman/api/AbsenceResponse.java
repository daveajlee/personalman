package de.davelee.personalman.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * This class is part of the PersonalMan REST API. It represents a response from the server containing details
 * of a planned absence. The absence details include company and username of the person requesting the absence as well
 * as the start date, end date and category of the absence.
 * @author Dave Lee
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class AbsenceResponse {

    // Which company the person who is absent works for
    private String company;

    // The username of the person who is absent
    private String username;

    // The start date of the absence in the format dd-mm-yyyy
    private String startDate;

    // The end date of the absence in the format dd-mm-yyyy
    private String endDate;

    // The category of the absence
    private String category;

}
