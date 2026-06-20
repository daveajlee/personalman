import {ApiProperty} from "@nestjs/swagger";

export class AddHistoryRequest {
    
    //company that the person works for
    @ApiProperty()
    private company: string;

    //username of the person
    @ApiProperty()
    private username: string;

    //token of the user making the change
    @ApiProperty()
    private token: string;

    //date that the history entry took place in format (dd-mm-yyyy)
    @ApiProperty()
    private date: string;

    //reason for the history entry
    @ApiProperty()
    private reason: string;

    //comment for the history entry
    @ApiProperty()
    private comment: string;

    getCompany(): string {
        return this.company;
    }

    getUsername(): string {
        return this.username;
    }

    getDate(): string {
        return this.date;
    }

    getReason(): string {
        return this.reason;
    }

    getComment(): string {
        return this.comment;
    }
}