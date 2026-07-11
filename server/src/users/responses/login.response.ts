import { ApiProperty } from '@nestjs/swagger';

export class LoginResponse {

    //The error message to show the user if the login was not successful which can be null if login was successful.
    @ApiProperty()
    private errorMessage: string;

    //The authentication token which can be null if the login was not successful.
    @ApiProperty()
    private token: string;

    constructor(errorMessage: string, token: string) {
        this.errorMessage = errorMessage;
        this.token = token;
    }

}