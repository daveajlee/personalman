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

    getCompany(): string {
        return this.company;
    }

    getUsername(): string {
        return this.username;
    }

    getPassword(): string {
        return this.password;
    }
}