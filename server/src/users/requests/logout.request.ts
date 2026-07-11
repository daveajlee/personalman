import { ApiProperty } from '@nestjs/swagger';

export class LogoutRequest {

    //The token to invalidate
    @ApiProperty()
    private token: string;

    constructor(token: string) {
        this.token = token;
    }

    getToken(): string {
        return this.token;
    }

}