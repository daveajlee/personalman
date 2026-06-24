import { Injectable } from '@nestjs/common';
import { UsersService } from '../users/users.service';
import { Absence } from './models/absence.model';
import { User } from '../users/models/user.model';
import { AbsenceCategory } from './models/absencecategory.enum';
import { AbsenceUtils } from './utils/absence.utils';
import { InjectModel } from '@nestjs/mongoose';
import { Model } from 'mongoose';

@Injectable()
export class AbsencesService {

    constructor(@InjectModel(Absence.name) private absenceModel: Model<Absence>, private readonly userService: UsersService) {}

    /**
     * Save the specified absence object in the database.
     * @param absence a <code>Absence</code> object to save in the database.
     * @return a <code>boolean</code> which is true iff the absence has been validated and saved successfully.
     */
    public async save ( absence: Absence ): Promise<boolean> {
        //Store result.
        let result: boolean = true;
        //Get the employee information.
        let user: User | null = await this.userService.findByCompanyAndUserName(absence.getCompany(), absence.getUsername());
        let absences: Absence[] = [];
        //Special processing for particular types of categories.
        if ( absence.getCategory() == AbsenceCategory.HOLIDAY && user != null ) {
            if ( Math.abs(new Date(absence.getStartDate()).getFullYear() - new Date(absence.getEndDate()).getFullYear()) < 2 ) {
                //Generate absences according to free days excluding these from the actual absences - just for the start year.
                absences.concat(AbsenceUtils.generateAbsences(user, new Date(absence.getStartDate()),
                        new Date(new Date(absence.getStartDate()).getFullYear(),12,31), AbsenceUtils.absenceCategoryFromString(absence.getCategory())));
                result = await this.controlAbsencesForYear ( absence.getCompany(), absence.getUsername(),
                        new Date(absence.getStartDate()).getFullYear(), AbsenceUtils.absenceCategoryFromString(absence.getCategory()), user, absences );
                if ( result ) {
                    var absences2: Absence[] = AbsenceUtils.generateAbsences(user,
                            new Date(new Date(absence.getEndDate()).getFullYear(),1,1), new Date(absence.getEndDate()), AbsenceUtils.absenceCategoryFromString(absence.getCategory()));
                    result = await this.controlAbsencesForYear ( absence.getCompany(), absence.getUsername(),
                            new Date(absence.getEndDate()).getFullYear(), AbsenceUtils.absenceCategoryFromString(absence.getCategory()), user, absences2 );
                    absences.concat(absences2);
                }
            } else if ( new Date(absence.getStartDate()).getFullYear() == new Date(absence.getEndDate()).getFullYear() ) {
                absences.concat(AbsenceUtils.generateAbsences(user, new Date(absence.getStartDate()), new Date(absence.getEndDate()), AbsenceUtils.absenceCategoryFromString(absence.getCategory())));
                result = await this.controlAbsencesForYear ( absence.getCompany(), absence.getUsername(),
                        new Date(absence.getStartDate()).getFullYear(), AbsenceUtils.absenceCategoryFromString(absence.getCategory()), user, absences );
            } else {
                //Holiday absences of more than one year are automatically rejected because annual leave will be exhausted.
                result = false;
            }
        } else if ( (absence.getCategory()==AbsenceCategory.CONFERENCE || absence.getCategory()==AbsenceCategory.TRIP) && user != null ) {
            //We save the absence for the whole length.
            let newAbsence = new Absence(absence.getCategory(), absence.getCompany(), absence.getUsername(), absence.getStartDate(), absence.getEndDate());
            absences.push(newAbsence);
            //We then add days in lieu if applicable.
            absences.concat(AbsenceUtils.generateDaysInLieu(user, new Date(absence.getStartDate()), new Date(absence.getEndDate())));
        } else if ( absence.getCategory()==AbsenceCategory.DAY_IN_LIEU && user != null ) {
            //Days In Lieu only bookable within same year.
            if ( new Date(absence.getStartDate()).getFullYear()!=new Date(absence.getEndDate()).getFullYear()) {
                result = false;
            } else {
                var numDayInLieuDaysRequests: number = await this.countAbsences(absence.getCompany(), absence.getUsername(),
                        new Date(new Date(absence.getStartDate()).getFullYear(),1,1), new Date(new Date(absence.getStartDate()).getFullYear(),12,31),
                        AbsenceCategory.DAY_IN_LIEU_REQUEST);
                var numDayInLieuDays: number = await this.countAbsences(absence.getCompany(), absence.getUsername(),
                        new Date(new Date(absence.getStartDate()).getFullYear(),1,1), new Date(new Date(absence.getStartDate()).getFullYear(),12,31),
                        AbsenceCategory.DAY_IN_LIEU);
                var numDayInLieuDaysAvailable: number = numDayInLieuDaysRequests - numDayInLieuDays;
                var numDaysDesired: number = Math.abs(new Date(absence.getStartDate()).getDate() - Math.abs(new Date(absence.getEndDate()).getDate())) + 1;
                result = numDaysDesired <= numDayInLieuDaysAvailable;
                absences.concat(AbsenceUtils.generateAbsences(user, new Date(absence.getStartDate()),
                        new Date(absence.getEndDate()), AbsenceUtils.absenceCategoryFromString(absence.getCategory())));
            }
        } else if ( user != null) {
            // No special processing needed so just add the absence to the list.
            absences.concat(AbsenceUtils.generateAbsences(user, new Date(absence.getStartDate()),
                    new Date(absence.getEndDate()), AbsenceUtils.absenceCategoryFromString(absence.getCategory())));
        }
        if ( result ) {
            absences.forEach(absence => {
                const createdAbsence = new this.absenceModel(absence);
                createdAbsence.save();
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
    private async controlAbsencesForYear ( company: string, employeeName: string, year: number, category: AbsenceCategory | null, user: User | null, absences: Absence[] ) : Promise<boolean> {
        var numAnnualLeave: number = await this.countAbsences(company, employeeName, new Date(year,1,1), new Date(year,12,31), category);
        numAnnualLeave += AbsenceUtils.countAbsencesInDays(absences);
        if ( user ) {
            return numAnnualLeave <= user.getLeaveEntitlementPerYear();
        }
        return false;
    }

    /**
     * Find absences taking place within the specified date range.
     * @param company a <code>String</code> with the company to retrieve absences for.
     * @param username a <code>String</code> with the username to retrieve absences for.
     * @param startDate a <code>Date</code> with the specified start date (inclusive).
     * @param endDate a <code>Date</code> with the specified start date (inclusive).
     * @return a <code>List</code> of <code>Absence</code> objects containing all absences for the specified date.
     */
    public async findAbsences ( company: string, username: string, startDate: Date,
                                        endDate: Date ): Promise<Absence[]> {
        //Call the appropriate DB method depending on whether a specified username is supplied.
        return username == null ? 
            await this.absenceModel.find({company: company, startDate: { $gt: startDate }, endDate: { $lt: endDate } }).exec() :
            await this.absenceModel.find({company: company, username: username, startDate: { $gt: startDate }, endDate: { $lt: endDate } }).exec();
    }

    /**
     * Count absences taking place within the specified date range.
     * @param company a <code>String</code> with the company to retrieve absences for.
     * @param username a <code>String</code> with the username to retrieve absences for.
     * @param startDate a <code>Date</code> with the specified start date (inclusive).
     * @param endDate a <code>Date</code> with the specified start date (inclusive).
     * @param absenceCategory a <code>AbsenceCategory</code> object representing the category of absences which should be retrieved.
     * @return a <code>Long</code> object containing the count of absences for the specified date.
     */
    public async countAbsences ( company: string, username: string, startDate: Date,
                                endDate: Date, absenceCategory: AbsenceCategory | null): Promise<number> {
        var count: number = 0;
        var matchingAbsences: Absence[] = await this.findAbsences (company, username, startDate, endDate);
        for ( var i = 0; i < matchingAbsences.length; i++ ) {
            var matchingAbsence: Absence = matchingAbsences[i];
            if ( matchingAbsence.getCategory() == absenceCategory ) {
                count = (new Date(matchingAbsence.getEndDate()).getDay() - new Date(matchingAbsence.getStartDate()).getDay()) + 1;
            }
        }
        return count;
    }

    /**
     * Delete absences taking place within the specified date range.
     * @param company a <code>String</code> with the company to retrieve absences for.
     * @param username a <code>String</code> with the username to retrieve absences for.
     * @param startDate a <code>Date</code> with the specified start date (inclusive).
     * @param endDate a <code>Date</code> with the specified start date (inclusive).
     */
    public async delete ( company: string, username: string, startDate: Date,
                         endDate: Date ): Promise<void> {
        var absencesToDelete: Absence[] = await this.findAbsences(company, username, startDate, endDate);
        absencesToDelete.forEach((absence) => {
            this.absenceModel.deleteOne(absence);
        });
    }

    /**
     * Delete all absences for a particular company.
     * @param company a <code>String</code> with the company to delete absences for.
     */
    public async deleteAllForCompany ( company: string ): Promise<void> {
        var absencesToDelete: Absence[] = await this.absenceModel.find({company: company}).exec();
        absencesToDelete.forEach((absence) => {
            this.absenceModel.deleteOne(absence);
        });
    }

}
