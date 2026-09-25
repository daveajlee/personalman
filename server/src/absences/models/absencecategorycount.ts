import { AbsenceCategory } from "./absencecategory.enum";

export class AbsenceCategoryCount {

    public absenceCategory: AbsenceCategory;
    public count: number;

    constructor(absenceCategory: AbsenceCategory, count: number) {
        this.absenceCategory = absenceCategory;
        this.count = count;
    }

    

}