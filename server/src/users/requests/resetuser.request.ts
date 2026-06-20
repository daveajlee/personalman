import { ApiProperty } from "@nestjs/swagger";

export class ResetUserRequest {
    // company associated with
    @ApiProperty()
    private company: string;

    // username who's password should be reset
    @ApiProperty()
    private username: string;

    // new password to set for this user
    @ApiProperty()
    private password: string;

    // The token of the user to verify that they are logged in
    @ApiProperty()
    private token: string;

    getCompany(): string {
        return this.company;
    }

    getToken(): string {
        return this.token;
    }

    getUsername(): string {
        return this.username;
    }

    getPassword(): string {
        return this.password;
    }
}