import { Body, Controller, Get, Param, Post } from '@nestjs/common';
import { ApiOperation, ApiOkResponse, ApiResponse } from '@nestjs/swagger';
import { PaidUserRequest } from './requests/paiduser.request';
import { PayUsersResponse } from './responses/payusers.response';
import { UsersResponse } from './responses/users.response';

@Controller('users')
export class UsersController {
  @Post('paid')
  @ApiOperation({ summary: 'Mark users as paid for a company', description: 'Mark users as paid for a company within a specific date range.' })
  @ApiResponse({ status: 200, description: 'Successfully found user(s) and their pay'})
  @ApiResponse({ status: 204, description: 'Successful but no users found'})
  markUsersPaid(@Body() paidUserRequest: PaidUserRequest): void {
    //TODO: implement mark users paid.
  }

  @Get('pay')
  @ApiOperation({ summary: 'Pay all users for a company', description: 'Pay all users for a company within a specific date range.' })
  @ApiOkResponse({
    description: 'Successfully found user(s) and their pay',
    type: PayUsersResponse,
  })
  @ApiResponse({ status: 204, description: 'Successful but no users found'})
  payUsers(@Param('company') company: string, @Param('token') token: string, @Param('startDate') startDate: string, @Param('endDate') endDate: string): void {
    //TODO: initiaise process of paying users.
  }

  @Get('/')
  @ApiOperation({ summary: 'Find all users for a company', description:'Find all users for a company to the system.' })
  @ApiOkResponse({
    description: 'Successfully found user(s)',
    type: UsersResponse,
  })
  @ApiResponse({ status: 204, description: 'Successful but no users found'})
  findAllUsers(@Param('company') company: string, @Param('token') token: string): void {
    //TODO: find all users.
  }
}
