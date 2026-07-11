export class Company {

    /**
     * The name of the company.
     */
    private name: string;

    /**
     * The default number of days of annual leave for this company.
     */
    private defaultAnnualLeaveInDays: number;

    /**
     * The country in which this company is based.
     */
    private country: string;

    constructor(name: string, defaultAnnualLeaveInDays: number, country: string) {
        this.name = name;
        this.defaultAnnualLeaveInDays = defaultAnnualLeaveInDays;
        this.country = country;
    }

    getDefaultAnnualLeaveInDays(): number {
        return this.defaultAnnualLeaveInDays;
    }

    getName(): string {
        return this.name;
    }

    getCountry(): string {
        return this.country;
    }

}