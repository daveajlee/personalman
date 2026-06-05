import {ApiProperty} from "@nestjs/swagger";
import {AbsenceResponse} from "./absence.response";

export class AbsencesResponse {

    //a count of the number of absences which were found by the server.
    @ApiProperty()
    private count: number;

    //a list of all absences found by the server.
    @ApiProperty({ type: [AbsenceResponse] })
    private absenceResponseList: AbsenceResponse[];

    //a map containing the absence categories as keys and the number of these categories in the response as values.
    @ApiProperty()
    private statisticsMap: Map<string, number>;

}