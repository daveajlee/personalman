import { ApiProperty } from '@nestjs/swagger';

export class LoginRequest {

    //The username who wants to login
    @ApiProperty()
    private username: string;

    //The password used for login
    @ApiProperty()
    private password: string;

    constructor(username: string, password: string) {
        this.username = username;
        this.password = password;
    }

    getUsername(): string {
        return this.username;
    }

    getPassword(): string {
        return this.password;
    }

}