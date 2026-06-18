import { UserHistoryReason } from "./userhistoryreason.enum";

export class UserHistoryEntry {

    /**
     * The date that this history entry took place.
     */
    date: Date;

    /**
     * The reason for this history entry.
     */
    userHistoryReason: typeof UserHistoryReason;

    /**
     * A comment about this history - this could be the reason it was given.
     */
    comment: string;

    constructor(date: Date, userHistoryReason: typeof UserHistoryReason, comment: string) {
        this.date = date;
        this.userHistoryReason = userHistoryReason;
        this.comment = comment;
    }

    getUserHistoryReason(): typeof UserHistoryReason {
        return this.userHistoryReason;
    }

    getComment(): string {
        return this.comment;
    }

    getDate(): Date {
        return this.date;
    }

}