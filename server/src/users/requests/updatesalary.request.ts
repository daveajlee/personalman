import {ApiProperty} from "@nestjs/swagger";

export class UpdateSalaryRequest {

    //company that the person works for
    @ApiProperty()
    private company: string;

    //username of the person
    @ApiProperty()
    private username: string;

    //token of the user making the change
    @ApiProperty()
    private token: string;

    //hourly wage that the person should get
    @ApiProperty()
    private hourlyWage: number;

    //number of hour person works per week
    @ApiProperty()
    private contractedHoursPerWeek: number;

}