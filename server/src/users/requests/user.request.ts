import { ApiProperty } from '@nestjs/swagger';

export class UserRequest {

    // first name of the user
    @ApiProperty()
    firstName: string;

    // surname of the user
    @ApiProperty()
    surname: string;

    // username
    @ApiProperty()
    username: string;

    //password
    @ApiProperty()
    password: string;

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

    //The role which the user would like
    @ApiProperty()
    role: string;

    // date of birth for the user in format dd-MM-yyyy
    @ApiProperty()
    dateOfBirth: string;

    getDateOfBirth(): string {
        return this.dateOfBirth;
    }

    getRole(): string {
        return this.role;
    }

    getWorkingDays(): string {
        return this.workingDays;
    }

    getCompany(): string {
        return this.company;
    }

    getPassword(): string {
        return this.password;
    }

    getUsername(): string {
        return this.username;
    }

    getPosition(): string {
        return this.position;
    }

    getLeaveEntitlementPerYear(): number {
        return this.leaveEntitlementPerYear;
    }

    getSurname(): string {
        return this.surname;
    }

    getFirstName(): string {
        return this.firstName;
    }

    getStartDate(): string {
        return this.startDate;
    }

}