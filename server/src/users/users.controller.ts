import { Body, Controller, Get, HttpStatus, Inject, Param, Post, Res } from '@nestjs/common';
import { ApiOperation, ApiOkResponse, ApiResponse } from '@nestjs/swagger';
import { PaidUserRequest } from './requests/paiduser.request';
import { PayUsersResponse } from './responses/payusers.response';
import { UsersResponse } from './responses/users.response';
import { UserResponse } from './responses/user.response';
import { User } from './models/user.model';
import {Response} from 'express';
import { Inject } from '@nestjs/common';
import { UsersService } from './users.service';

@Controller('users')
export class UsersController {

    constructor(private readonly userService: UsersService) {}

  @Post('paid')
  @ApiOperation({ summary: 'Mark users as paid for a company', description: 'Mark users as paid for a company within a specific date range.' })
  @ApiResponse({ status: 200, description: 'Successfully found user(s) and their pay'})
  @ApiResponse({ status: 204, description: 'Successful but no users found'})
  markUsersPaid(@Body() paidUserRequest: PaidUserRequest): void {
    //Verify that user is logged in.
        if ( paidUserRequest.getToken() == null || !this.userService.checkAuthToken(paidUserRequest.getToken()) ) {
            return ResponseEntity.status(403).build();
        }
        //First of all, check if the compny field is empty or null, then return bad request.
        if ( StringUtils.isBlank(paidUserRequest.getCompany())) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        //For each user in the supplied map.
        var usernameSet: String[] = paidUserRequest.getEmployeePayTable().keySet();
        usernameSet.forEach((username) => {
            //Find the relevant user.
            var user: User = userService.findByCompanyAndUserName(paidUserRequest.getCompany(), username);
            if ( !userService.addUserHistoryEntry(user, LocalDate.now(), UserHistoryReason.PAID,
                    "Paid " + paidUserRequest.getEmployeePayTable().get(username) + " for date range " +
                    paidUserRequest.getStartDate() + " - " + paidUserRequest.getEndDate())) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        });
        //Return empty ok response if no exceptions.
        return ResponseEntity.ok().build();
  }

  @Get('pay')
  @ApiOperation({ summary: 'Pay all users for a company', description: 'Pay all users for a company within a specific date range.' })
  @ApiOkResponse({
    description: 'Successfully found user(s) and their pay',
    type: PayUsersResponse,
  })
  @ApiResponse({ status: 204, description: 'Successful but no users found'})
  payUsers(@Param('company') company: string, @Param('token') token: string, @Param('startDate') startDate: string, @Param('endDate') endDate: string): void {
    //Verify that user is logged in.
        if ( token == null || !userService.checkAuthToken(token) ) {
            return ResponseEntity.status(403).build();
        }
        //First of all, check if the compny field is empty or null, then return bad request.
        if ( StringUtils.isBlank(company)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        //Now retrieve the user based on the username.
        var users: User[] = userService.findByCompany(company);
        //If users is empty then return 204.
        if ( users.isEmpty() ) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        //Now go through each user if they have worked during the date range and then calculate pay.
        var totalSum: number = 0; 
        var employeePayTable = new Map<String, number>();
        users.forEach((user) => {
            var sumToBePaid: number = 0;
            var startLocalDate: Date = DateUtils.convertDateToLocalDate(startDate);
            var endLocalDate: Date = DateUtils.convertDateToLocalDate(endDate);
            while ( startLocalDate != null && endLocalDate != null && (startLocalDate.isBefore(endLocalDate) || startLocalDate.isEqual(endLocalDate)) ) {
                sumToBePaid = sumToBePaid.add(new BigDecimal(userService.getHoursForDate(user, startLocalDate)).multiply(user.getHourlyWage()));
                startLocalDate = startLocalDate.plusDays(1);
            }
            employeePayTable.put(user.getUserName(), sumToBePaid.doubleValue());
            totalSum = totalSum.add(sumToBePaid);
        });
        //Return response.
        return ResponseEntity.ok(PayUsersResponse.builder()
                .employeePayTable(employeePayTable)
                .totalSum(totalSum)
                .build());
  }

  @Get('/')
  @ApiOperation({ summary: 'Find all users for a company', description:'Find all users for a company to the system.' })
  @ApiOkResponse({
    description: 'Successfully found user(s)',
    type: UsersResponse,
  })
  @ApiResponse({ status: 204, description: 'Successful but no users found'})
  async findAllUsers(@Res() res: Response, @Param('company') company: string, @Param('token') token: string): Promise<UsersResponse> {
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
            userResponses[i] = new UserResponse(users[i].getFirstName(), users[i].getLastName(), users[i].getUsername(), 
                users[i].getCompany(), users[i].getLeaveEntitlementPerYear(), users[i].workingDays, users[i].getPosition(),
                users[i].getStartDate(), users[i].getEndDate(), users[i].getRole(), users[i].getDateOfBirth(), users[i].getHourlyWage(),
                users[i].getContractedHoursPerWeek(), users[i].getTrainingsList(), users[i].getUserHistoryEntryList());
        }
        res.status(HttpStatus.OK).send();
        return new UsersResponse(userResponses.length, userResponses);
  }
}
