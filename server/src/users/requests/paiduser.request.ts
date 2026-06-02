import { ApiProperty } from '@nestjs/swagger';

export class PaidUserRequest {

    // company associated with
    @ApiProperty()
    company: string;

    // a table of employee by username and the amount of pay they have received.
    @ApiProperty()
    employeePayTable: Map<string, number>;

    // start date of date range that they were paid for in format dd-MM-yyyy
    @ApiProperty()
    startDate: string;

    // end date of date range that they were paid for in format dd-MM-yyyy
    @ApiProperty()
    endDate: string;

    // The token of the user to verify that they are logged in
    @ApiProperty()
    token: string;

    constructor(company: string, employeePayTable: Map<string, number>, startDate: string, endDate: string, token: string) {
        this.company = company;
        this.employeePayTable = employeePayTable;
        this.startDate = startDate;
        this.endDate = endDate;
        this.token = token;
    }

}