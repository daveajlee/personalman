import {ApiProperty} from "@nestjs/swagger";

export class CompanyResponse {

    //name
    @ApiProperty()
    private name: string;

    //default leave entitlement per year
    @ApiProperty()
    private defaultAnnualLeaveInDays: number;

    //base country
    @ApiProperty()
    private country: string;

}