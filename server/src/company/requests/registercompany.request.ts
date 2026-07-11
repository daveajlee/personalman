import {ApiProperty} from '@nestjs/swagger';

export class RegisterCompanyRequest {
    
    //name of the company
    @ApiProperty()
    private name: string;

    //default annual leave in days
    @ApiProperty()
    private defaultAnnualLeaveInDays: number;

    //base country
    @ApiProperty()
    private country: string;

    getCountry(): string {
        return this.country;
    }

    getDefaultAnnualLeaveInDays(): number {
        return this.defaultAnnualLeaveInDays;
    }

    getName(): string {
        return this.name;
    }

}