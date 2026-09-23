export class Absence {

    private category: string;
    private company: string;
    private username: string;
    private startDate: number;
    private endDate: number;

    constructor(category: string, company: string, username: string, startDate: number, endDate: number) {
        this.category = category;
        this.company = company;
        this.username = username;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    getCategory(): string {
        return this.category;
    }

    getStartDate(): number {
        return this.startDate;
    }

    getEndDate(): number {
        return this.endDate;
    }

    getCompany(): string {
        return this.company;
    }

    getUsername(): string {
        return this.username;
    }

}