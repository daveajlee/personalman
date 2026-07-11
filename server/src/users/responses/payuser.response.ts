import { ApiProperty } from "@nestjs/swagger";

export class PayUserResponse {

    @ApiProperty()
    userName: string;

    @ApiProperty()
    amount: number;

    constructor(userName: string, amount: number) {
        this.userName = userName;
        this.amount = amount;
    }

}