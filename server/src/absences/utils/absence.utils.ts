import { Absence } from "../models/absence.model";
import { AbsenceRequest } from "../requests/absence.request";
import { AbsenceResponse } from "../responses/absence.response";

export class AbsenceUtils {

    /**
     * This method converts an AbsenceRequest object into an Absence object which can be saved in the database.
     * @param absenceRequest a <code>AbsenceRequest</code> object to convert
     * @param startDate a <code>LocalDate</code> with the already converted start date.
     * @param endDate a <code>LocalDate</code> with the already converted end date.
     * @return a <code>Absence</code> object.
     */
    static convertAbsenceRequestToAbsence(absenceRequest: AbsenceRequest, startDate: Date, endDate: Date ): Absence{
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
    static convertAbsencesToAbsenceResponses ( absences: Absence[] ): AbsenceResponse[] {
        //Make a list to return.
        let absenceResponses: AbsenceResponse[] = [];
        //Now convert each absence to an absence response.
        absences.forEach((absence) => {
            let absenceResponse = new AbsenceResponse(absence.getCompany(), absence.getUsername(), 
                    new Date(absence.getStartDate()), new Date(absence.getEndDate()));
            if ( absence.getCategory() != null ) {
                absenceResponse.setCategory(absence.getCategory().toString());
            }
            absenceResponses.push(absenceResponse);
        });
        //Return absence response.
        return absenceResponses;
    }

    /**
     * This method prepares the statistics map for AbsencesResponse which can be returned through the REST API.
     * @param absencesResponse a <code>AbsencesResponse</code> object as data basis for statistics.
     * @return a <code>AbsencesResponse</code> object containing additionally the completed statistics.
     */
    static calculateAbsencesResponseStatistics (absencesResponse: AbsencesResponse): AbsencesResponse {
        //Get list of absences.
        let absenceResponseList: AbsenceResponse[] = absencesResponse.getAbsenceResponseList();
        //Go through and add it to the correct category.
        absenceResponseList.forEach((absenceResponse) => {
            if ( absenceResponse.getStartDate() != null && absenceResponse.getEndDate() != null
                && absenceResponse.getCategory() != null ) {
                absencesResponse.addToStatisticsMap(absenceResponse.getCategory(),
                        Period.between(new Date(absenceResponse.getStartDate()),
                                new Date(absenceResponse.getEndDate())).getDays() + 1);
            }
        })
        //Return absences response.
        return absencesResponse;
    }

    /**
     * Count the total number of days where the employee is absent given the list of supplied absences.
     * @param absences a <code>List</code> of <code>Absence</code> objects with absence details.
     * @return a <code>int</code> with the total number of days.
     */
    static countAbsencesInDays ( absences: Absence[] ): number {
        let numAbsentDays: number = 0;
        absences.forEach((absence) => {
            numAbsentDays += Period.between(absence.getStartDate(), absence.getEndDate()).getDays() + 1;
        })
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
    static generateAbsences (user: User, startDate: Date, endDate: Date, category: AbsenceCategory ): Absence[] {
        let workingDays: string[] = user.getWorkingDays();
        let absences: Absence[] = [];
        let currentDate: Date = startDate;
        //Start with start date and run until end date.
        while ( !currentDate > endDate ) {
            //Check if it is a free day.
            let isFreeDay = true;
            workingDays.forEach((workingDay) => {
                if (workingDay == currentDate.getDayOfWeek()) {
                    isFreeDay = false;
                }
            })
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
    static generateDaysInLieu(user: User, startDate: Date, endDate: Date): Absence[] {
        let workingDays = user.getWorkingDays();
        let absences = [];
        let actualDate = startDate;
        while ( !actualDate.isAfter(endDate)) {
            //Check if it is a free day.
            let isFreeDay = true;
            workingDays.forEach((workingDay) => {
                if ( workingDay==actualDate.getDayOfWeek()) {
                    isFreeDay = false;
                }
            })
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