
import { ApiProperty } from '@nestjs/swagger';
import { UserResponse } from './user.response';

export class UsersResponse {

    //a count of the number of users which were found by the server.
    @ApiProperty()
    count: number;

    //an array of all users found by the server.
    @ApiProperty({ type: [UserResponse] })
    userResponses: UserResponse[];

    constructor(count: number, userResponses: UserResponse[]) {
        this.count = count;
        this.userResponses = userResponses;
    }
}