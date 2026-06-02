import { ApiProperty } from '@nestjs/swagger';
import { UserHistoryResponse } from './userhistory.response';

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

}