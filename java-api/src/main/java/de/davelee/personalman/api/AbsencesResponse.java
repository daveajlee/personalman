package de.davelee.personalman.api;

import lombok.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class is part of the PersonalMan REST API. It represents a response from the server containing details
 * of all matched absences according to specified criteria. As well as containing details about the absences in form of
 * a list of <code>AbsenceResponse</code> objects, the object also contains a simple count of the absences found as well
 * as statistics about the specified absences such as which category the absences belong to.
 * @author Dave Lee
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AbsencesResponse {

    //a count of the number of absences which were found by the server.
    private Long count;

    //a list of all absences found by the server.
    private List<AbsenceResponse> absenceResponseList;

    //a map containing the absence categories as keys and the number of these categories in the response as values.
    private Map<String, Integer> statisticsMap = new HashMap<>();

    /**
     * Increment the count for the specified category in the hash map by the amount of days that the absence took place.
     * @param absenceCategory a <code>String</code> containing the category to increment.
     * @param amountOfDays a <code>int</code> containing the number of days that the absence occurs for.
     */
    public void addToStatisticsMap (final String absenceCategory, final int amountOfDays ) {
        if ( statisticsMap.containsKey(absenceCategory) ) {
            statisticsMap.put(absenceCategory, statisticsMap.get(absenceCategory) + amountOfDays);
        } else {
            statisticsMap.put(absenceCategory, amountOfDays);
        }
    }

}
