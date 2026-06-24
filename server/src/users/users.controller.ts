import { Body, Controller, Get, HttpStatus, Inject, Param, Post, Res } from '@nestjs/common';
import { ApiOperation, ApiOkResponse, ApiResponse } from '@nestjs/swagger';
import { PaidUserRequest } from './requests/paiduser.request';
import { PayUsersResponse } from './responses/payusers.response';
import { UsersResponse } from './responses/users.response';
import { UserResponse } from './responses/user.response';
import { User } from './models/user.model';
import type { Response } from 'express';
import { UsersService } from './users.service';
import { UserHistoryReason } from './models/userhistoryreason.enum';
import { UserUtils } from './utils/user.utils';

@Controller('users')
export class UsersController {

    constructor(private readonly userService: UsersService) {}

  @Post('paid')
  @ApiOperation({ summary: 'Mark users as paid for a company', description: 'Mark users as paid for a company within a specific date range.' })
  @ApiResponse({ status: 200, description: 'Successfully found user(s) and their pay'})
  @ApiResponse({ status: 204, description: 'Successful but no users found'})
  async markUsersPaid(@Body() paidUserRequest: PaidUserRequest, @Res() res: Response): Promise<void> {
    //Verify that user is logged in.
        if ( paidUserRequest.token == null || !this.userService.checkAuthToken(paidUserRequest.token) ) {
            res.status(HttpStatus.FORBIDDEN).send();
        }
        //First of all, check if the compny field is empty or null, then return bad request.
        if ( paidUserRequest.company === '' ) {
            res.status(HttpStatus.BAD_REQUEST).send();
        }
        //For each user in the supplied map.
        //var usernameSet: () => MapIterator<string> = paidUserRequest.employeePayTable.keys;
        [...paidUserRequest.employeePayTable.keys()].forEach(username => {
            //Find the relevant user.
            this.userService.findByCompanyAndUserName(paidUserRequest.company, username).then(user => {
                if ( user != null && !this.userService.addUserHistoryEntry(user, new Date(), UserHistoryReason.PAID,
                    "Paid " + paidUserRequest.employeePayTable.get(username) + " for date range " +
                    paidUserRequest.startDate + " - " + paidUserRequest.endDate)) {
                        res.status(HttpStatus.INTERNAL_SERVER_ERROR).send();
                }
            })
        });
        //Return empty ok response if no exceptions.
        res.status(HttpStatus.OK).send();
  }

  @Get('pay')
  @ApiOperation({ summary: 'Pay all users for a company', description: 'Pay all users for a company within a specific date range.' })
  @ApiOkResponse({
    description: 'Successfully found user(s) and their pay',
    type: PayUsersResponse,
  })
  @ApiResponse({ status: 204, description: 'Successful but no users found'})
  async payUsers(@Param('company') company: string, @Param('token') token: string, @Param('startDate') startDate: string, @Param('endDate') endDate: string, @Res() res: Response): Promise<PayUsersResponse> {
    //Verify that user is logged in.
        if ( token == null || !this.userService.checkAuthToken(token) ) {
            res.status(HttpStatus.FORBIDDEN).send();
        }
        //First of all, check if the compny field is empty or null, then return bad request.
        if ( company === '' ) {
            res.sendStatus(HttpStatus.BAD_REQUEST).send();
        }
        //Now retrieve the user based on the username.
        var users: User[] = await this.userService.findByCompany(company);
        //If users is empty then return 204.
        if ( users.length === 0 ) {
            res.status(HttpStatus.NO_CONTENT).send();
        }
        //Now go through each user if they have worked during the date range and then calculate pay.
        var totalSum: number = 0; 
        var employeePayTable = new Map<string, number>();
        users.forEach((user) => {
            if ( user != null ) {
                var sumToBePaid: number = 0;
                var startDateObj = new Date(startDate);
                var endDateObj = new Date(endDate);
                while ( startDateObj != null && endDateObj != null && (startDateObj <= endDateObj) ) {
                    if ( (this.userService.getHoursForDate(user, startDateObj) != null )) {
                        sumToBePaid += (this.userService.getHoursForDate(user, startDateObj) * user.getHourlyWage());
                    }
                    startDateObj.setDate(startDateObj.getDate() + 1);
                }
                employeePayTable.set(user.getUsername(), sumToBePaid);
                totalSum += sumToBePaid;
            }
            
        });
        //Return response.
        res.status(HttpStatus.OK).send();
        return new PayUsersResponse(employeePayTable, totalSum);
  }

  @Get('/')
  @ApiOperation({ summary: 'Find all users for a company', description:'Find all users for a company to the system.' })
  @ApiOkResponse({
    description: 'Successfully found user(s)',
    type: UsersResponse,
  })
  @ApiResponse({ status: 204, description: 'Successful but no users found'})
  async findAllUsers(@Param('company') company: string, @Param('token') token: string, @Res() res: Response): Promise<UsersResponse> {
    //Verify that user is logged in.
        if ( token == null || !this.userService.checkAuthToken(token) ) {
            res.status(HttpStatus.FORBIDDEN).send();
        }
        //First of all, check if the compny field is empty or null, then return bad request.
        if ( company === '') {
            res.status(HttpStatus.BAD_REQUEST).send();
        }
        //Now retrieve the user based on the username.
        var users: User[] = await this.userService.findByCompany(company);
        //If users is empty then return 204.
        if ( users.length === 0 ) {
            res.status(HttpStatus.NO_CONTENT).send();
        }
        //Convert to UserResponse object and return 200.
        var userResponses: UserResponse[] = new UserResponse[users.length];
        for ( var i = 0; i < users.length; i++ ) {
            userResponses[i] = UserUtils.convertUserToUserResponse(users[i]);
        }
        res.status(HttpStatus.OK).send();
        return new UsersResponse(userResponses.length, userResponses);
  }
}
