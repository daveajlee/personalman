import { ApiProperty } from "@nestjs/swagger";

export class AddTrainingRequest {

    //company that the person works for
    @ApiProperty()
    private company: string;

    //username of the person
    @ApiProperty()
    private username: string;

    //token of the user making the change
    @ApiProperty()
    private token: string;

    //name of the training course or qualification to be added to the user's profile
    @ApiProperty()
    private trainingCourse: string;

    getCompany(): string {
        return this.company;
    }

    getUsername(): string {
        return this.username;
    }

    getToken(): string {
        return this.token;
    }

    getTrainingCourse(): string {
        return this.trainingCourse;
    }

}