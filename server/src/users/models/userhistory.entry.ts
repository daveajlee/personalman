export class UserHistoryEntry {

    /**
     * The date that this history entry took place.
     */
    date: Date;

    /**
     * The reason for this history entry.
     */
    reason: string;

    /**
     * A comment about this history - this could be the reason it was given.
     */
    comment: string;

    constructor(date: Date, reason: string, comment: string) {
        this.date = date;
        this.reason = reason;
        this.comment = comment;
    }

    getUserHistoryReason(): string {
        return this.reason;
    }

    getComment(): string {
        return this.comment;
    }

    getDate(): Date {
        return this.date;
    }

}