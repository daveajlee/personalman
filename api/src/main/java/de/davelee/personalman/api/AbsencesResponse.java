package de.davelee.personalman.api;

import java.util.HashMap;
import java.util.List;

/**
 * This class is part of the PersonalMan REST API. It represents a response from the server containing details
 * of all matched absences according to specified criteria. As well as containing details about the absences in form of
 * a list of <code>AbsenceResponse</code> objects, the object also contains a simple count of the absences found as well
 * as statistics about the specified absences such as which category the absences belong to.
 * @author Dave Lee
 */
public class AbsencesResponse {

    //a count of the number of absences which were found by the server.
    private Long count;

    //a list of all absences found by the server.
    private List<AbsenceResponse> absenceResponseList;

    //a hashmap containing the absence categories as keys and the number of these categories in the response as values.
    private HashMap<String, Integer> statisticsMap = new HashMap<>();

    /**
     * Retrieve the number of absences found by the server. This is usually used when counting the absences is sufficient
     * and no further details about the absences are required.
     * @return a <code>Long</code> object containing the number of absences found by the server.
     */
    public Long getCount() {
        return count;
    }

    /**
     * Set the number of absences found. This is usually set when counting the absences is sufficient
     * and no further details about the absences are required.
     * @param count a <code>Long</code> containing the number of absences found.
     */
    public void setCount(final Long count) {
        this.count = count;
    }

    /**
     * Retrieve the list of absences found by the server. This list may be null if the server was instructed to only
     * count absences.
     * @return a <code>List</code> of <code>AbsenceResponse</code> objects containing details of the absences found.
     */
    public List<AbsenceResponse> getAbsenceResponseList() {
        return absenceResponseList;
    }

    /**
     * Set the list of absences found. This list does not have to be set if only instructed to count the absences.
     * @param absenceResponseList a <code>List</code> of <code>AbsenceResponse</code> objects containing details of the
     *                            absences found.
     */
    public void setAbsenceResponseList(final List<AbsenceResponse> absenceResponseList) {
        this.absenceResponseList = absenceResponseList;
    }

    /**
     * Retrieve a hashmap containing the absence categories as keys and the number of these categories in the response as
     * values.
     * @return a <code>HashMap</code> object containing absence categories and the number of absences found for each category.
     */
    public HashMap<String, Integer> getStatisticsMap() {
        return statisticsMap;
    }

    /**
     * Set a hashmap containing the absence categories as keys and the number of these categories in the response as values.
     * @param statisticsMap a <code>HashMap</code> object  containing absence categories and the number of absences found
     *                      for each category
     */
    public void setStatisticsMap(final HashMap<String, Integer> statisticsMap) {
        this.statisticsMap = statisticsMap;
    }

    /**
     * Increment the count for the specified category in the hash map by 1.
     * @param absenceCategory a <code>String</code> containing the category to increment.
     */
    public void addToStatisticsMap (final String absenceCategory ) {
        if ( statisticsMap.containsKey(absenceCategory) ) {
            statisticsMap.put(absenceCategory, statisticsMap.get(absenceCategory) + 1);
        } else {
            statisticsMap.put(absenceCategory, 1);
        }
    }

}
