export class Absence {

    private category: string;
    private company: string;
    private username: string;
    private startDate: string;
    private endDate: string;

    constructor(category: string, company: string, username: string, startDate: string, endDate: string) {
        this.category = category;
        this.company = company;
        this.username = username;
        this.startDate = startDate;
        this.endDate = endDate;
    }

}