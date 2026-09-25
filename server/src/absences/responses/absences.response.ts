import {ApiProperty} from "@nestjs/swagger";
import {AbsenceResponse} from "./absence.response";
import { AbsenceCategoryCount } from "../models/absencecategorycount";
import { AbsenceUtils } from "../utils/absence.utils";

export class AbsencesResponse {

    //a count of the number of absences which were found by the server.
    @ApiProperty()
    private count: number;

    //a list of all absences found by the server.
    @ApiProperty({ type: [AbsenceResponse] })
    private absenceResponseList: AbsenceResponse[];

    //a map containing the absence categories as keys and the number of these categories in the response as values.
    @ApiProperty()
    private statisticsMap: AbsenceCategoryCount[];

    constructor() {
        this.statisticsMap = [];
    }

    setCount(count: number): void {
        this.count = count;
    }

    setAbsenceResponseList(absenceResponseList: AbsenceResponse[]) {
        this.absenceResponseList = absenceResponseList;
    }

    getAbsenceResponseList(): AbsenceResponse[] {
        return this.absenceResponseList;
    }

    addToStatisticsMap(category: string, days: number) {
        if ( AbsenceUtils.absenceCategoryFromString(category) ) {
            this.statisticsMap.push(new AbsenceCategoryCount(AbsenceUtils.absenceCategoryFromString(category)!, days));
        }
    }

}