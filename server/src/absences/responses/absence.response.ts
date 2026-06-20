import {ApiProperty} from "@nestjs/swagger";

export class AbsenceResponse {

    // Which company the person who is absent works for
    @ApiProperty()
    private company: string;

    // The username of the person who is absent
    @ApiProperty()
    private username: string;

    // The start date of the absence in the format dd-mm-yyyy
    @ApiProperty()
    private startDate: string;

    // The end date of the absence in the format dd-mm-yyyy
    @ApiProperty()
    private endDate: string;

    // The category of the absence
    @ApiProperty()
    private category: string;

    constructor(company: string, username: string, startDate: string, endDate: string, category: string) {
        this.company = company;
        this.username = username;
        this.startDate = startDate;
        this.endDate = endDate;
        this.category = category;
    }

    getCompany(): string {
        return this.company;
    }

    getUsername(): string {
        return this.username;
    }

    getStartDate(): string {
        return this.startDate;
    }

    getEndDate(): string {
        return this.endDate;
    }

    getCategory(): string {
        return this.category;
    }

}