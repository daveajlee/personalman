import { ApiProperty } from '@nestjs/swagger';
import { UserHistoryResponse } from './userhistory.response';
import { UsersController } from '../users.controller';

export class UserResponse {

    // first name of the user
    @ApiProperty()
    firstName: string;

    // surname of the user
    @ApiProperty()
    surname: string;

    // username
    @ApiProperty()
    username: string;

    // company associated with
    @ApiProperty()
    company: string;

    // leave entitlement for this user (in days per year)
    @ApiProperty()
    leaveEntitlementPerYear: number;

    // which days of the week that the users works comma-separated (e.g. Monday,Tuesday,Wednesday,Thursday)
    @ApiProperty()
    workingDays: string;

    // the position of this user
    @ApiProperty()
    position: string;

    // start date for the user in format dd-MM-yyyy
    @ApiProperty()
    startDate: string;

    // end date for the user in format dd-MM-yyyy
    @ApiProperty()
    endDate: string;

    //The role that this user has
    @ApiProperty()
    role: string;

    //The date of birth for this user
    @ApiProperty()
    dateOfBirth: string;

    //The salary of this user
    @ApiProperty()
    hourlyWage: number;

    //The number of hours that this user works
    @ApiProperty()
    contractedHoursPerWeek: number;

    //list of trainings and qualifications that user has
    @ApiProperty()
    trainings: string[];

    //list of entries in the log history of this user
    @ApiProperty({ type: [UserHistoryResponse] })
    userHistory: UserHistoryResponse[];

    constructor(firstName:string, surname:string, username:string, company:string, leaveEntitlementPerYear: number,
        workingDays:string, position:string, startDate:string, endDate:string, role:string, dateOfBirth:string, hourlyWage:number,
        contractedHoursPerWeek:number, trainings: string[], userHistory: UserHistoryResponse[]) {
            this.firstName = firstName;
            this.surname = surname;
            this.username = username;
            this.company = company;
            this.leaveEntitlementPerYear = leaveEntitlementPerYear;
            this.workingDays = workingDays;
            this.position = position;
            this.startDate = startDate;
            this.endDate = endDate;
            this.role = role;
            this.dateOfBirth = dateOfBirth;
            this.hourlyWage = hourlyWage;
            this.contractedHoursPerWeek = contractedHoursPerWeek;
            this.trainings = trainings;
            this.userHistory = userHistory;

    }

}