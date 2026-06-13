import { Injectable } from '@nestjs/common';
import { UsersService } from '../users/users.service';
import { Absence } from './models/absence.model';
import { User } from '../users/models/user.model';

@Injectable()
export class AbsencesService {

    private userService: UsersService;

    /**
     * Save the specified absence object in the database.
     * @param absence a <code>Absence</code> object to save in the database.
     * @return a <code>boolean</code> which is true iff the absence has been validated and saved successfully.
     */
    public save ( absence: Absence ): boolean {
        //Store result.
        let result: boolean = true;
        //Get the employee information.
        var user: User = userService.findByCompanyAndUserName(absence.getCompany(), absence.getUsername());
        let absences: Absence[] = [];
        //Special processing for particular types of categories.
        if ( absence.getCategory() == AbsenceCategory.HOLIDAY ) {
            if ( absence.getStartDate().getYear() != absence.getEndDate().getYear() &&
                    ChronoUnit.YEARS.between(absence.getStartDate(), absence.getEndDate()) < 2 ) {
                //Generate absences according to free days excluding these from the actual absences - just for the start year.
                absences.addAll(AbsenceUtils.generateAbsences(user, absence.getStartDate(),
                        LocalDate.of(absence.getStartDate().getYear(),12,31), absence.getCategory()));
                result = controlAbsencesForYear ( absence.getCompany(), absence.getUsername(),
                        absence.getStartDate().getYear(), absence.getCategory(), user, absences );
                if ( result ) {
                    var absences2: Absence[] = AbsenceUtils.generateAbsences(user,
                            LocalDate.of(absence.getEndDate().getYear(),1,1), absence.getEndDate(), absence.getCategory());
                    result = controlAbsencesForYear ( absence.getCompany(), absence.getUsername(),
                            absence.getEndDate().getYear(), absence.getCategory(), user, absences2 );
                    absences.addAll(absences2);
                }
            } else if ( absence.getStartDate().getYear() == absence.getEndDate().getYear() ) {
                absences.addAll(AbsenceUtils.generateAbsences(user, absence.getStartDate(), absence.getEndDate(), absence.getCategory()));
                result = controlAbsencesForYear ( absence.getCompany(), absence.getUsername(),
                        absence.getStartDate().getYear(), absence.getCategory(), user, absences );
            } else {
                //Holiday absences of more than one year are automatically rejected because annual leave will be exhausted.
                result = false;
            }
        } else if ( absence.getCategory()==AbsenceCategory.CONFERENCE || absence.getCategory()==AbsenceCategory.TRIP ) {
            //We save the absence for the whole length.
            let newAbsence = new Absence(absence.getCategory(), absence.getCompany(), absence.getUsername(), absence.getStartDate(), absence.getEndDate());
            absences.add(newAbsence);
            //We then add days in lieu if applicable.
            absences.addAll(AbsenceUtils.generateDaysInLieu(user, absence.getStartDate(), absence.getEndDate()));
        } else if ( absence.getCategory()==AbsenceCategory.DAY_IN_LIEU ) {
            //Days In Lieu only bookable within same year.
            if ( absence.getStartDate().getYear()!=absence.getEndDate().getYear()) {
                result = false;
            } else {
                var numDayInLieuDaysRequests: number = countAbsences(absence.getCompany(), absence.getUsername(),
                        LocalDate.of(absence.getStartDate().getYear(),1,1), LocalDate.of(absence.getStartDate().getYear(),12,31),
                        AbsenceCategory.DAY_IN_LIEU_REQUEST);
                var numDayInLieuDays: number = countAbsences(absence.getCompany(), absence.getUsername(),
                        LocalDate.of(absence.getStartDate().getYear(),1,1), LocalDate.of(absence.getStartDate().getYear(),12,31),
                        AbsenceCategory.DAY_IN_LIEU);
                var numDayInLieuDaysAvailable: number = numDayInLieuDaysRequests - numDayInLieuDays;
                var numDaysDesired: number = Period.between(absence.getStartDate(), absence.getEndDate()).getDays() + 1;
                result = numDaysDesired <= numDayInLieuDaysAvailable;
                absences.addAll(AbsenceUtils.generateAbsences(user, absence.getStartDate(),
                        absence.getEndDate(), absence.getCategory()));
            }
        } else {
            // No special processing needed so just add the absence to the list.
            absences.addAll(AbsenceUtils.generateAbsences(user, absence.getStartDate(),
                    absence.getEndDate(), absence.getCategory()));
        }
        if ( result ) {
            absences.forEach(absence => {
                absenceRepository.save(absence);
            });
        }
        return result;
    }

    /**
     * Calculate the name of absences per year for a particular reason based on current absences and the planned absence list.
     * @param company a <code>String</code> with the company that the user is associated with.
     * @param employeeName a <code>String</code> containing the name of the employee.
     * @param year a <code>int</code> with the year to perform the calculation for.
     * @param category a <code>AbsenceCategory</code> enum with the category for absence.
     * @param user a <code>User</code> object representing the user taking absence.
     * @param absences a <code>List</code> of <code>Absence/code> objects representing planned absences.
     * @return a <code>boolean</code> which is true iff the planned absences can be taken without exhausting all annual leave for the supplied year.
     */
    private controlAbsencesForYear ( company: string, employeeName: string, year: number, category: AbsenceCategory, user: User, absences: Absence[] ) : boolean {
        var numAnnualLeave: number = countAbsences(company, employeeName, LocalDate.of(year,1,1), LocalDate.of(year,12,31), category);
        numAnnualLeave += AbsenceUtils.countAbsencesInDays(absences);
        return numAnnualLeave <= user.getLeaveEntitlementPerYear();
    }

    /**
     * Find absences taking place within the specified date range.
     * @param company a <code>String</code> with the company to retrieve absences for.
     * @param username a <code>String</code> with the username to retrieve absences for.
     * @param startDate a <code>LocalDate</code> with the specified start date (inclusive).
     * @param endDate a <code>LocalDate</code> with the specified start date (inclusive).
     * @return a <code>List</code> of <code>Absence</code> objects containing all absences for the specified date.
     */
    public findAbsences ( company: string, username: string, startDate: LocalDate,
                                        endDate: LocalDate ): Absence[] {
        //If the mongo template is empty return an empty list. (This only happens in JUnit tests).
        if ( mongoTemplate == null ) {
            return List.of();
        }
        //Call the appropriate DB method depending on whether a specified username is supplied.
        var query: Query = new Query();
        if ( username == null ) {
            query.addCriteria(Criteria.where("company").is(company).and("startDate").gte(startDate).and("endDate").lte(endDate));
            return mongoTemplate.find(query, Absence.class);
        }
        query.addCriteria(Criteria.where("company").is(company).and("username").is(username).and("startDate").gte(startDate).and("endDate").lte(endDate));
        return mongoTemplate.find(query, Absence.class);
    }

    /**
     * Count absences taking place within the specified date range.
     * @param company a <code>String</code> with the company to retrieve absences for.
     * @param username a <code>String</code> with the username to retrieve absences for.
     * @param startDate a <code>LocalDate</code> with the specified start date (inclusive).
     * @param endDate a <code>LocalDate</code> with the specified start date (inclusive).
     * @param absenceCategory a <code>AbsenceCategory</code> object representing the category of absences which should be retrieved.
     * @return a <code>Long</code> object containing the count of absences for the specified date.
     */
    public countAbsences ( company: string, username: string, startDate: LocalDate,
                                endDate: LocalDate, absenceCategory: AbsenceCategory): number {
        var count: number = 0;
        var matchingAbsences: Absence[] = findAbsences (company, username, startDate, endDate);
        for ( var i = 0; i < matchingAbsences.length; i++ ) {
            var matchingAbsence: Absence = matchingAbsences[i];
            if ( matchingAbsence.getCategory() == absenceCategory ) {
                count = Period.between(matchingAbsence.getStartDate(), matchingAbsence.getEndDate()).getDays() + 1;
            }
        }
        return count;
    }

    /**
     * Delete absences taking place within the specified date range.
     * @param company a <code>String</code> with the company to retrieve absences for.
     * @param username a <code>String</code> with the username to retrieve absences for.
     * @param startDate a <code>LocalDate</code> with the specified start date (inclusive).
     * @param endDate a <code>LocalDate</code> with the specified start date (inclusive).
     */
    public delete ( company: string, username: string, startDate: LocalDate,
                         endDate: LocalDate ): void {
        var absencesToDelete: Absence[] = findAbsences(company, username, startDate, endDate);
        absencesToDelete.forEach((absence) => {
            absenceModel.deleteOne(absence);
        });
    }

    /**
     * Delete all absences for a particular company.
     * @param company a <code>String</code> with the company to delete absences for.
     */
    public deleteAllForCompany ( company: string ): void {
        var absencesToDelete: Absence[] = absenceRepository.findByCompany(company);
        absencesToDelete.forEach((absence) => {
            absenceModel.deleteOne(absence);
        });
    }

}
