import {ApiProperty} from "@nestjs/swagger";

export class ChangePasswordRequest {

    // company associated with
    @ApiProperty()
    private company: string;

    // username who's password should be changed
    @ApiProperty()
    private username: string;

    // The token of the user to verify that they are logged in
    @ApiProperty()
    private token: string;

    // current password for this user
    @ApiProperty()
    private currentPassword: string;

    // new password to set for this user
    @ApiProperty()
    private newPassword: string;

    getCompany(): string {
        return this.company;
    }

    getUsername(): string {
        return this.username;
    }

    getToken(): string {
        return this.token;
    }

    getCurrentPassword(): string {
        return this.currentPassword;
    }

    getNewPassword(): string {
        return this.newPassword;
    }
}