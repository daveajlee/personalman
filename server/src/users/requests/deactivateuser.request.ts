import { ApiProperty } from '@nestjs/swagger';

export class DeactivateUserRequest {

    // company associated with
    @ApiProperty()
    private company: string;

    // username who's password should be changed
    @ApiProperty()
    private username: string;

    // The token of the user to verify that they are logged in
    @ApiProperty()
    private token: string;

    // did the user resign or were they sacked
    @ApiProperty()
    private resigned: boolean;

    // leaving date in the format dd-MM-yyyy
    @ApiProperty()
    private leavingDate: string;

    // reason for leaving
    @ApiProperty()
    private reason: string;

    getReason(): string {
        return this.reason;
    }

    isResigned(): boolean {
        return this.resigned;
    }

    getLeavingDate(): string {
        return this.leavingDate;
    }

    getCompany(): string {
        return this.company;
    }

    getUsername(): string {
        return this.username;
    }

    getToken(): string {
        return this.token;
    }

}