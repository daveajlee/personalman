import { ApiProperty } from "@nestjs/swagger";

export class PayUsersResponse {

    // a table of employee by username and the amount of pay they should receive.
    @ApiProperty()
    employeePayTable: Map<string, number>;

    // total sum that should be paid out
    @ApiProperty()
    totalSum: number;

    constructor(employeePayTable: Map<string, number>, totalSum: number) {
        this.employeePayTable = employeePayTable;
        this.totalSum = totalSum;
    }

}