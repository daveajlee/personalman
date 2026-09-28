import {ApiProperty} from "@nestjs/swagger";

export class ChangePasswordRequest {

    // company associated with
    @ApiProperty()
    private company: string;

    // username who's password should be changed
    @ApiProperty()
    private username: string;

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

    getCurrentPassword(): string {
        return this.currentPassword;
    }

    getNewPassword(): string {
        return this.newPassword;
    }
}