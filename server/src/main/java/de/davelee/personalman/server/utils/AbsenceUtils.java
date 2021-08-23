package de.davelee.personalman.server.utils;

import de.davelee.personalman.api.AbsenceRequest;
import de.davelee.personalman.api.AbsenceResponse;
import de.davelee.personalman.api.AbsencesResponse;
import de.davelee.personalman.server.model.Absence;
import de.davelee.personalman.server.model.AbsenceCategory;
import de.davelee.personalman.server.model.User;
import org.bson.types.ObjectId;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

/**
 * This class provides utility methods for processing related to /absences endpoints in the PersonalManRestController.
 * @author Dave Lee
 */
public class AbsenceUtils {

    /**
     * This method converts an AbsenceRequest object into an Absence object which can be saved in the database.
     * @param absenceRequest a <code>AbsenceRequest</code> object to convert
     * @param startDate a <code>LocalDate</code> with the already converted start date.
     * @param endDate a <code>LocalDate</code> with the already converted end date.
     * @return a <code>Absence</code> object.
     */
    public static Absence convertAbsenceRequestToAbsence (final AbsenceRequest absenceRequest, final LocalDate startDate, final LocalDate endDate ) {
        return Absence.builder()
                .id(new ObjectId())
                .category(AbsenceCategory.fromString(absenceRequest.getCategory()))
                .company(absenceRequest.getCompany())
                .username(absenceRequest.getUsername())
                .startDate(startDate)
                .endDate(endDate)
                .build();
    }

    /**
     * This method converts a List of Absences to a list of AbsenceResponses which can be returned through the REST API.
     * @param absences a <code>List</code> of <code>Absence</code> objects to convert.
     * @return a <code>List</code> of converted <code>AbsenceResponse</code> objects.
     */
    public static List<AbsenceResponse> convertAbsencesToAbsenceResponses ( final List<Absence> absences ) {
        //Make a list to return.
        List<AbsenceResponse> absenceResponses = new ArrayList<>(absences.size());
        //Now convert each absence to an absence response.
        for ( Absence absence : absences ) {
            AbsenceResponse absenceResponse = AbsenceResponse.builder()
                    .company(absence.getCompany())
                    .username(absence.getUsername())
                    .startDate(DateUtils.convertLocalDateToDate(absence.getStartDate()))
                    .endDate(DateUtils.convertLocalDateToDate(absence.getEndDate()))
                    .build();
            if ( absence.getCategory() != null ) {
                absenceResponse.setCategory(absence.getCategory().toString());
            }
            absenceResponses.add(absenceResponse);
        }
        //Return absence response.
        return absenceResponses;
    }

    /**
     * This method prepares the statistics map for AbsencesResponse which can be returned through the REST API.
     * @param absencesResponse a <code>AbsencesResponse</code> object as data basis for statistics.
     * @return a <code>AbsencesResponse</code> object containing additionally the completed statistics.
     */
    public static AbsencesResponse calculateAbsencesResponseStatistics (final AbsencesResponse absencesResponse) {
        //Get list of absences.
        List<AbsenceResponse> absenceResponseList = absencesResponse.getAbsenceResponseList();
        //Go through and add it to the correct category.
        for ( AbsenceResponse absenceResponse : absenceResponseList ) {
            if ( absenceResponse.getStartDate() != null && absenceResponse.getEndDate() != null ) {
                absencesResponse.addToStatisticsMap(absenceResponse.getCategory(),
                        Period.between(DateUtils.convertDateToLocalDate(absenceResponse.getStartDate()),
                                DateUtils.convertDateToLocalDate(absenceResponse.getEndDate())).getDays() + 1);
            }
        }
        //Return absences response.
        return absencesResponse;
    }

    /**
     * Count the total number of days where the employee is absent given the list of supplied absences.
     * @param absences a <code>List</code> of <code>Absence</code> objects with absence details.
     * @return a <code>int</code> with the total number of days.
     */
    public static int countAbsencesInDays ( final List<Absence> absences ) {
        int numAbsentDays = 0;
        for ( Absence absence : absences ) {
            numAbsentDays += Period.between(absence.getStartDate(), absence.getEndDate()).getDays() + 1;
        }
        return numAbsentDays;
    }

    /**
     * Generate those absences objects according to the specified dates excluding the free days of this employee.
     * @param user a <code>User</code> object with the details for this user.
     * @param startDate a <code>LocalDate</code> object with the start date of the desired absence in format dd-MM-yyyy.
     * @param endDate a <code>LocalDate</code> object with the end date of the desired absence in format dd-MM-yyyy.
     * @param category a <code>AbsenceCategory</code> enum with the category for the desired absence.
     * @return a <code>List</code> of <code>Absence</code> objects.
     */
    public static List<Absence> generateAbsences (final User user, final LocalDate startDate, final LocalDate endDate, final AbsenceCategory category ) {
        final List<DayOfWeek> workingDays = user.getWorkingDays();
        List<Absence> absences = new ArrayList<>();
        LocalDate currentDate = LocalDate.of(startDate.getYear(), startDate.getMonth(), startDate.getDayOfMonth());
        //Start with start date and run until end date.
        while ( !currentDate.isAfter(endDate) ) {
            //Check if it is a free day.
            boolean isFreeDay = true;
            for (DayOfWeek workingDay : workingDays) {
                if (workingDay == currentDate.getDayOfWeek()) {
                    isFreeDay = false;
                }
            }

            //If it is not a free day then add the absence.
            if (!isFreeDay) {
                absences.add(Absence.builder()
                        .id(new ObjectId())
                        .category(category)
                        .startDate(currentDate)
                        .endDate(currentDate)
                        .username(user.getUserName())
                        .company(user.getCompany())
                        .build());
            }

            //Regardless we need to increase currentDate.
            currentDate = currentDate.plusDays(1);

        }

        return absences;
    }

    /**
     * Generate those absences objects for days in lieu according to the specified dates which fall on free days of this employee.
     * @param user a <code>User</code> object with the details for this user.
     * @param startDate a <code>LocalDate</code> object with the start date of the desired absence.
     * @param endDate a <code>LocalDate</code> object with the end date of the desired absence.
     * @return a <code>List</code> of <code>Absence</code> objects.
     */
    public static List<Absence> generateDaysInLieu(final User user, final LocalDate startDate, final LocalDate endDate) {
        final List<DayOfWeek> workingDays = user.getWorkingDays();
        List<Absence> absences = new ArrayList<>();
        LocalDate actualDate = LocalDate.of(startDate.getYear(), startDate.getMonth(), startDate.getDayOfMonth());
        while ( !actualDate.isAfter(endDate)) {
            //Check if it is a free day.
            boolean isFreeDay = true;
            for ( DayOfWeek workingDay : workingDays ) {
                if ( workingDay==actualDate.getDayOfWeek()) {
                    isFreeDay = false;
                }
            }
            if ( isFreeDay ) {
                absences.add(Absence.builder()
                        .id(new ObjectId())
                        .company(user.getCompany())
                        .category(AbsenceCategory.DAY_IN_LIEU_REQUEST)
                        .startDate(actualDate)
                        .endDate(actualDate)
                        .username(user.getUserName())
                        .build());
            }
            actualDate = actualDate.plusDays(1);
        }
        return absences;
    }

}
