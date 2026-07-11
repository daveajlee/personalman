import { ApiProperty } from "@nestjs/swagger";

export class UserHistoryResponse {

    /**
     * The date that this history entry took place in format dd-MM-yyyy.
     */
    @ApiProperty()
    date: string;

    /**
     * The reason for this history entry.
     */
    @ApiProperty()
    userHistoryReason: string;

    /**
     * A comment about this history - this could be the reason it was given.
     */
    @ApiProperty()
    comment: string;

    constructor(date: string, userHistoryReason: string, comment: string) {
        this.date = date;
        this.userHistoryReason = userHistoryReason;
        this.comment = comment;
    }

}