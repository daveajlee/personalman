import { ApiProperty } from '@nestjs/swagger';

export class LoginRequest {

    //The company that the user is using to login.
    @ApiProperty()
    private company: string;

    //The username who wants to login
    @ApiProperty()
    private username: string;

    //The password used for login
    @ApiProperty()
    private password: string;

    constructor(company: string, username: string, password: string) {
        this.company = company;
        this.username = username;
        this.password = password;
    }

    getCompany(): string {
        return this.company;
    }

    getUsername(): string {
        return this.username;
    }

}