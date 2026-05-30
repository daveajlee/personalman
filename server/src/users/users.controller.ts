import { Controller, Get, Post } from '@nestjs/common';
import { ApiOperation } from '@nestjs/swagger';

@Controller('users')
export class UsersController {
  @Post('paid')
  @ApiOperation({ summary: 'Mark users as paid for a company' })
  markUsersPaid(): void {
    //TODO: implement mark users paid.
  }

  @Get('pay')
  @ApiOperation({ summary: 'Pay all users for a company' })
  payUsers(): void {
    //TODO: initiaise process of paying users.
  }

  @Get('/')
  @ApiOperation({ summary: 'Find all users for a company' })
  findAllUsers(): void {
    //TODO: find all users.
  }
}
