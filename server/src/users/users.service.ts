import { Injectable } from '@nestjs/common';
import { ConfigService } from '@nestjs/config';
import { User } from './models/user.model';
import { InjectModel } from '@nestjs/mongoose';
import { Model } from 'mongoose';
import { UserHistoryReason } from './models/userhistoryreason.enum';
import { UserAccountStatus } from './models/useraccountstatus.enum';

@Injectable()
export class UsersService {

    tokenLength: number | undefined;
    timeoutInMinutes: number | undefined;

    constructor(private configService: ConfigService, @InjectModel(User.name) private userModel: Model<User>) {
        this.tokenLength = this.configService.get<number>('TOKEN_LENGTH');
        this.timeoutInMinutes = this.configService.get<number>('LOGOUT_MINUTES');
    }

    private loggedInTokens: Map<string, Date> = new Map<string, Date>();

    /**
     * Save the specified user object in the database.
     * @param user a <code>User</code> object to save in the database.
     * @return a <code>boolean</code> which is true iff the user has been saved successfully.
     */
    public save ( user: User ): boolean {
        const createdUser = new this.userModel(user);
        return createdUser.save() != null;
    }

    /**
     * Find a user according to their user name and company.
     * @param company a <code>String</code> with the company to retrieve user for.
     * @param userName a <code>String</code> with the user name.
     * @return a <code>User</code> representing the user which has this user name. Returns null if no matching user. User name is a unique field so no chance of more than one result!
     */
    public async findByCompanyAndUserName ( company: string, userName: string ): Promise<User | null> {
        return await this.userModel.findOne({company, userName});
    }

    /**
     * Find a user based on their date of birth, name and company.
     * @param dateOfBirth a <code>LocalDate</code> object containing the date of birth of the user to retrieve.
     * @param firstName a <code>String</code> containing the first name of the user to retrieve.
     * @param lastName a <code>String</code> containing the surname of the user to retrieve.
     * @param company a <code>String</code> containing the company of the user to retrieve.
     * @return a <code>User</code> object representing the user matching the criteria or null if none can be found.
     */
    public async findUserByDateOfBirthAndNameAndCompany ( dateOfBirth: Date, firstName: string, lastName: string, company: string ): Promise<User | null> {
        return await this.userModel.findOne({dateOfBirth, firstName, lastName, company});
    }


    /**
     * Find all users belonging to a company.
     * @param company a <code>String</code> with the company to retrieve users for.
     * @return a <code>List</code> of <code>User</code> objects representing the users belonging to this company. Returns null if no matching users.
     */
    public async findByCompany ( company: string ): Promise<User[]> {
        return await this.userModel.find({company});
    }

    /**
     * Delete the specified user object from the database.
     * @param user a <code>User</code> object to delete from the database.
     */
    public delete ( user: User ): void {
        this.userModel.deleteOne(user);
    }

    /**
     * Delete all users for a particular company.
     * @param company a <code>String</code> with the company to delete users for.
     */
    public async deleteAllUsers ( company: string ): Promise<void> {
        var usersToDelete: User[] | null = await this.userModel.find({company}).exec();
        if ( usersToDelete != null ) {
            usersToDelete.forEach(user => this.userModel.deleteOne(user));
        }
    }

    /**
     * Deactivate the specified user object from the database.
     * Also calculate annual leave entitlement for this year and return it.
     * @param user a <code>User</code> object to delete from the database.
     * @param leavingDate a <code>LocalDate</code> object containing the date that the user will leave.
     * @param resigned a <code>boolean</code> which is true iff the user resigned and was not sacked.
     * @param reason a <code>String</code> with a comment why the user is leaving
     * @return a <code>int</code> containing the number of days of annual leave the user may use this year.
     */
    public deactivate (user: User, leavingDate: Date, resigned: boolean, reason: string) : number {
        //Set end date for this user as their leaving date.
        user.setEndDate(leavingDate);
        //Add a note to their profile with reason for leaving or being sacked.
        if ( resigned ) {
            user.addUserHistoryEntry(leavingDate, UserHistoryReason.RESIGNED, reason);
        } else {
            user.addUserHistoryEntry(leavingDate, UserHistoryReason.SACKED, reason);
        }
        //Deactivate user account.
        user.setAccountStatus(UserAccountStatus.DEACTIVATED);
        //Calculate remaining annual leave.
        var remainingAnnualLeave: number = 0;
        //Calculate number of days that user worked this year.
        var numWorkingDays: number = (new Date(leavingDate.getFullYear(), 0, 0).getTime() - leavingDate.getTime()) / 86400000; 
        //1. Divide the user's annual leave by 365 days and then multiply by the number of working days - rounding up as necessary.
        remainingAnnualLeave = Math.ceil(( user.getLeaveEntitlementPerYear() / 365) * numWorkingDays);
        //Return the annual leave entitlement in this year.
        return remainingAnnualLeave;
    }

    /**
     * Update the salary information for the specified user object,
     * @param user a <code>User</code> object to set the salary information for.
     * @param hourlyWage a <code>BigDecimal</code> object containing the hourly wage to set for this user.
     * @param contractedHoursPerWeek a <code>int</code> containing the number of contracted hours per week to add.
     * @return a <code>boolean</code> which is true iff the user has been updated successfully.
     */
    public updateSalaryInformation (user: User, hourlyWage: number, contractedHoursPerWeek: number ): boolean {
        user.setHourlyWage(hourlyWage);
        user.setContractedHoursPerWeek(contractedHoursPerWeek);
        const createdUser = new this.userModel(user);
        return createdUser.save() != null;
    }

    /**
     * Add a training course or qualification for the specified user object.
     * @param user a <code>User</code> object to set the salary information for.
     * @param trainingCourse a <code>String</code> containing the name of the training course or qualification.
     * @return a <code>boolean</code> which is true iff the user has been updated successfully.
     */
    public addTrainingCourse ( user: User, trainingCourse: string ): boolean {
        if ( user.getTrainingsList() == null ) {
            user.setTrainingsList([]);
        }
        user.addTrainingCourse(trainingCourse);
        const createdUser = new this.userModel(user);
        return createdUser.save() != null;
    }

    /**
     * Add the number of hours for a particular date to the specified user object.
     * @param user a <code>User</code> object to set the hours for.
     * @param hours a <code>int</code> with the number of hours to add.
     * @param date a <code>LocalDate</code> object containing the day to add the hours to.
     * @return a <code>boolean</code> which is true iff the user has been updated successfully.
     */
    public addHoursForDate ( user: User, hours: number, date: Date ): boolean {
        if ( user.getTimesheet() == null ) {
            user.setTimesheet(new Map<Date, number>());
        }
        user.addHoursForDate(hours, date);
        const createdUser = new this.userModel(user);
        return createdUser.save() != null;
    }

    /**
     * Add a new history entry to the list.
     * @param user a <code>User</code> object to set the hours for.
     * @param date a <code>LocalDate</code> containing the date that the entry/event took place.
     * @param userHistoryReason a <code>UserHistoryReason</code> containing the reason that the entry/event took place.
     * @param comment a <code>String</code> containing the comment about the entry/event.
     * @return a <code>boolean</code> which is true iff the user has been updated successfully.
     */
    public addUserHistoryEntry (user: User, date: Date, userHistoryReason: UserHistoryReason | null, comment: string): boolean {
        if ( user.getUserHistoryEntryList() == null ) {
            user.setUserHistoryEntryList([]);
        }
        user.addUserHistoryEntry(date, userHistoryReason, comment);
        const createdUser = new this.userModel(user);
        return createdUser.save() != null;
    }

    /**
     * Retrieve the number of hours for a particular date with the specified user object,
     * @param user a <code>User</code> object to get the hours for.
     * @param date a <code>LocalDate</code> object containing the day to get the hours for.
     * @return a <code>int</code> with the number of hours.
     */
    public getHoursForDate ( user: User, date: Date ): number | undefined {
        return user.getHoursForDate(date);
    }

    /**
     * Retrieve the number of hours for a date range with the specified user object,
     * @param user a <code>User</code> object to get the hours for.
     * @param startDate a <code>LocalDate</code> object containing the first day to get the hours for.
     * @param endDate a <code>LocalDate</code> object containing the last day to get the hours for.
     * @return a <code>int</code> with the number of hours.
     */
    public getHoursForDateRange ( user: User, startDate: Date, endDate: Date ): number {
        var hours: number = 0;
        var date: Date = startDate;
        while ( date.getTime() <= endDate.getTime() ) {
            hours += this.getHoursForDate(user, date);
            date = new Date(date.getTime() + 86400000);
        }
        return hours;
    }

    /**
     * Generate a token for this user and add it to the list of logged in tokens. The token should expire after the specified amount of minutes in the config.
     * Return the token so that the client can also have access to it.
     * @param userName a <code>String</code> with the user name of the logged in user.
     * @return a <code>String</code> containing the token which is valid for a limited amount of time.
     */
    public generateAuthToken ( userName: string ): string {
        var token: string = userName + "-" + this.createRandomString(this.tokenLength!);
        this.loggedInTokens.set(token, new Date(Date.now() + this.timeoutInMinutes! * 60000));
        return token;
    }

    private createRandomString(length: number): string {
        const chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        let result = "";
        for (let i = 0; i < length; i++) {
            result += chars.charAt(Math.floor(Math.random() * chars.length));
        }     
        return result;
    }

    /**
     * Check if this is a valid token which is defined as a token that exists in the token storage and has not yet expired.
     * @param token a <code>String</code> containing the token to check.
     * @return a <code>boolean</code> which is true iff the token is valid.
     */
    public checkAuthToken ( token: string ): boolean {
        return this.loggedInTokens.has(token) && this.loggedInTokens.get(token)!.getTime() >= new Date().getTime();
    }

    /**
     * Remove the supplied token from the list of logged in tokens.
     * @param token a <code>String</code> containing the token to remove from the list of logged in tokens.
     */
    public removeAuthToken ( token: string ): void {
        this.loggedInTokens.delete(token);
    }

    /**
     * Reset the password for a particular user.
     * @param company a <code>String</code> containing the company that the user is associated with.
     * @param username a <code>String</code> containing the username of the user who's password should be reset.
     * @param newPassword a <code>String</code> containing the new password that the user should receive.
     * @return a <code>boolean</code> which is true iff the password could be reset successfully.
     */
    public async resetUserPassword ( company: string, username: string, newPassword: string ): Promise<boolean> {
        var user: User | null = await this.userModel.findOne({ company, username }).exec();
        if ( user != null ) {
            user.setPassword(newPassword);
            const createdUser = new this.userModel(user);
            return createdUser.save() != null;
        }
        return false;
    }

    /**
     * Change the password for a particular user if the old password matches the current password in database.
     * @param company a <code>String</code> containing the company that the user is associated with.
     * @param username a <code>String</code> containing the username of the user who's password should be reset.
     * @param oldPassword a <code>String</code> containing the old password that the user had.
     * @param newPassword a <code>String</code> containing the new password that the user should receive.
     * @return a <code>boolean</code> which is true iff the password could be changed successfully.
     */
    public async changePassword ( company: string, username: string, oldPassword: string, newPassword: string ): Promise<boolean> {
        var user: User | null = await this.userModel.findOne({ company, username }).exec();
        if ( user != null ) {
            if ( user.getPassword() === oldPassword ) {
                user.setPassword(newPassword);
                const createdUser = new this.userModel(user);
                return createdUser.save() != null;
            }
        }
        return false;
    }

}
