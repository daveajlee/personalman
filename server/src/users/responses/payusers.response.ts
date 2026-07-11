import { ApiProperty } from "@nestjs/swagger";
import { PayUserResponse } from "./payuser.response";

export class PayUsersResponse {

    // a table of employee by username and the amount of pay they should receive.
    @ApiProperty()
    employeePayTable: PayUserResponse[];

    // total sum that should be paid out
    @ApiProperty()
    totalSum: number;

    constructor(employeePayTable: PayUserResponse[], totalSum: number) {
        this.employeePayTable = employeePayTable;
        this.totalSum = totalSum;
    }

}