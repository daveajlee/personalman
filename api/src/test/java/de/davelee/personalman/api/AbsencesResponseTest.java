package de.davelee.personalman.api;

import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.Assert.assertEquals;

/**
 * This class tests the constructor, getter and setter methods as well as the addToStatisticsMap method
 * of the <code>AbsencesResponse</code> class.
 * Created by davelee on 08.02.17.
 */
public class AbsencesResponseTest {


    @Test
    /**
     * Test the setter methods and ensure variables are set together using the getter methods.
     */
    public void testSettersAndGetters() {
        AbsencesResponse absencesResponse = new AbsencesResponse();
        absencesResponse.setCount(new Long(1));
        assertEquals(new Long(1), absencesResponse.getCount());
        ArrayList<AbsenceResponse> absenceResponseArrayList = new ArrayList<>();
        absenceResponseArrayList.add(new AbsenceResponse("MyCompany", "dlee", "07-02-2017", "08-02-2017", "Holiday"));
        absencesResponse.setAbsenceResponseList(absenceResponseArrayList);
        assertEquals(1, absenceResponseArrayList.size());
        assertEquals("MyCompany", absenceResponseArrayList.get(0).getCompany());
        HashMap<String, Integer> statisticsMap = new HashMap<String, Integer>();
        statisticsMap.put("Holiday", 1);
        absencesResponse.setStatisticsMap(statisticsMap);
        assertEquals(1, absencesResponse.getStatisticsMap().size());
        assertEquals(new Integer(1), absencesResponse.getStatisticsMap().get("Holiday"));
    }

    @Test
    /**
     * Test the addToStatisticsMap method by adding to an already existing category or adding to a new category.
     */
    public void testAddStatisticsMap() {
        AbsencesResponse absencesResponse = new AbsencesResponse();
        HashMap<String, Integer> statisticsMap = new HashMap<String, Integer>();
        statisticsMap.put("Holiday", 1);
        absencesResponse.setStatisticsMap(statisticsMap);
        absencesResponse.addToStatisticsMap("Illness");
        assertEquals(new Integer(1), absencesResponse.getStatisticsMap().get("Illness"));
        absencesResponse.addToStatisticsMap("Holiday");
        assertEquals(new Integer(2), absencesResponse.getStatisticsMap().get("Holiday"));
    }

}
