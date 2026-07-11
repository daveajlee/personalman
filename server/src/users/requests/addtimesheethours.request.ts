import { ApiProperty } from '@nestjs/swagger';

export class AddTimesheetHoursRequest {

    //company that the person works for
    @ApiProperty()
    private company: string;

    //username of the person
    @ApiProperty()
    private username: string;

    //token of the user making the change
    @ApiProperty()
    private token: string;

    //the date to add the hours to in format dd-MM-yyyy.
    @ApiProperty()
    private date: string;

    //the number of hours to add
    @ApiProperty()
    private hours: number;

    getCompany(): string {
        return this.company;
    }

    getUsername(): string {
        return this.username;
    }

    getToken(): string {
        return this.token;
    }

    getHours(): number {
        return this.hours;
    }

    getDate(): string {
        return this.date;
    }

}