import { Body, Controller, Delete, Get, HttpStatus, Param, Patch, Post, Res } from '@nestjs/common';
import { ApiOperation, ApiResponse, ApiOkResponse } from '@nestjs/swagger';
import { AddHistoryRequest } from './requests/addhistory.request';
import { AddTimesheetHoursRequest } from './requests/addtimesheethours.request';
import { AddTrainingRequest } from './requests/addtraining.request';
import { ChangePasswordRequest } from './requests/changepassword.request';
import { DeactivateUserRequest } from './requests/deactivateuser.request';
import { LogoutRequest } from './requests/logout.request';
import { LoginResponse } from './responses/login.response';
import { LoginRequest } from './requests/login.request';
import { ResetUserRequest } from './requests/resetuser.request';
import { UpdateSalaryRequest } from './requests/updatesalary.request';
import { UserResponse } from './responses/user.response';
import { UserRequest } from './requests/user.request';
import { UsersService } from './users.service';
import type { Response } from 'express';
import { User } from './models/user.model';
import { UserUtils } from './utils/user.utils';

@Controller('user')
export class UserController {

    constructor(private readonly userService: UsersService) {}

  @Post('logout')
  @ApiOperation({ summary: 'Logout', description: 'Logout from the system' })
  @ApiResponse({ status: 200, description: 'Successfully processed logout request'})
  logout(@Body() logoutRequest: LogoutRequest, @Res() res: Response): void {
    //Remove the token from the authenticated tokens.
        this.userService.removeAuthToken(logoutRequest.getToken());
        //Return 200.
        res.status(HttpStatus.OK).send();
  }

  @Post('login')
  @ApiOperation({ summary: 'Login', description: 'Login to the system' })
  @ApiOkResponse({
      description: 'Successfully processed login request',
      type: LoginResponse,
  })
  login(@Body() loginRequest: LoginRequest): void {
    var user: User = this.userService.findByCompanyAndUserName(loginRequest.getCompany(), loginRequest.getUsername());
        if ( user != null && user.getAccountStatus()== UserAccountStatus.ACTIVE && user.getPassword().contentEquals(loginRequest.getPassword()) ) {
            return ResponseEntity.ok().body(LoginResponse.builder().token(userService.generateAuthToken(loginRequest.getUsername())).build());
        } else if ( user != null ) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(LoginResponse.builder().errorMessage("Password was incorrect!").build());
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(LoginResponse.builder().errorMessage("User was not found").build());
  }

  @Get('/')
  @ApiOperation({ summary: 'Find a user', description: 'Find a user in the system.' })
  @ApiOkResponse({
      description: 'Successfully found user',
      type: UserResponse,
  })
  @ApiResponse({ status: 204, description: 'Successful but no user found'})
  findUser(@Param('company') company: string, @Param('username') username: string, @Param('token') token: string): void {
    //Check valid request including authentication
        var status: HttpStatus = this.validateAndAuthenticateRequest(company, username, token);
        //If the status is not null then produce response and return.
        if ( status != null ) {
            
            return new ResponseEntity<>(status);
        }
        //Now retrieve the user based on the username.
        var user: User = userService.findByCompanyAndUserName(company, username);
        //If user is null then return 204.
        if ( user == null ) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        //Convert to UserResponse object and return 200.
        return ResponseEntity.ok(UserUtils.convertUserToUserResponse(user));
  }

  @Post('/')
  @ApiOperation({ summary: 'Add a user', description: 'Add a user to the system.' })
  @ApiResponse({ status: 201, description: 'Successfully created user'})
  addUser(@Body() userRequest: UserRequest): void {
    //First of all, check if any of the fields are empty or null, then return bad request.
        if (StringUtils.isBlank(userRequest.getFirstName()) || StringUtils.isBlank(userRequest.getSurname())
                || StringUtils.isBlank(userRequest.getPosition()) || StringUtils.isBlank(userRequest.getStartDate())
                || StringUtils.isBlank(userRequest.getUsername()) || StringUtils.isBlank(userRequest.getWorkingDays())
                || StringUtils.isBlank(userRequest.getCompany()) ) {
            return ResponseEntity.badRequest().build();
        }
        // If the leave entitlement is 0 then set it to company default.
        if ( userRequest.getLeaveEntitlementPerYear() <= 0 ) {
            userRequest.setLeaveEntitlementPerYear(companyService.getCompany(userRequest.getCompany()).getDefaultAnnualLeaveInDays());
        }
        //Now convert the dates to LocalDate. If end date is before start date then return bad request.
        var startLocalDate: Date = DateUtils.convertDateToLocalDate(userRequest.getStartDate());
        if ( startLocalDate == null ) {
            return ResponseEntity.badRequest().build();
        }
        //Now convert to user object.
        var user: User = UserUtils.convertUserRequestToUser(userRequest, startLocalDate);
        //Return 201 if saved successfully.
        return userService.save(user) ? ResponseEntity.status(201).build() : ResponseEntity.status(500).build();
  }

  @Delete('/')
  @ApiOperation({ summary: 'Delete a user', description: 'Delete a user from the system.' })
  @ApiResponse({ status: 200, description: 'Successfully delete user'})
  @ApiResponse({ status: 204, description: 'Successful but no user found'})
  deleteUser(@Param('company') company: string, @Param('username') username: string, @Param('token') token: string): void {
    //Check valid request including authentication
        var status: HttpStatus = validateAndAuthenticateRequest(company, username, token);
        //If the status is not null then produce response and return.
        if ( status != null ) {
            return new ResponseEntity<>(status);
        }
        //Now retrieve the user based on the username.
        var user: User = userService.findByCompanyAndUserName(company, username);
        //If user is null then return 204.
        if ( user == null ) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        //Now delete the user based on the username.
        userService.delete(user);
        //Return 200.
        return ResponseEntity.status(200).build();
  }

  @Patch('/training')
  @ApiOperation({ summary: 'Add a training course', description: 'Add a training course for a particular user.' })
  @ApiResponse({ status: 200, description: 'Successfully added training course'})
  @ApiResponse({ status: 204, description: 'No user found'})
  addTraining(@Body() addTrainingRequest: AddTrainingRequest): void {
    //Check valid request including authentication
        var status: HttpStatus = validateAndAuthenticateRequest(addTrainingRequest.getCompany(), addTrainingRequest.getUsername(), addTrainingRequest.getToken());
        //If the status is not null then produce response and return.
        if ( status != null ) {
            return new ResponseEntity<>(status);
        }
        //Now retrieve the user based on the username.
        var user: User = userService.findByCompanyAndUserName(addTrainingRequest.getCompany(), addTrainingRequest.getUsername());
        //If user is null then return 204.
        if ( user == null ) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        //Now add training course and return 200 or 500 depending on DB success.
        return userService.addTrainingCourse(user, addTrainingRequest.getTrainingCourse()) ?
                ResponseEntity.status(200).build() : ResponseEntity.status(500).build();
  }

  @Get('/timesheet')
  @ApiOperation({ summary: "Retrieve the user's timesheet", description: "Retrieve number of hours for a specified date (range) for a specified user." })
  @ApiOkResponse({
      description: 'Successfully retrieved hours',
      type: Number,
  })
  @ApiResponse({ status: 204, description: 'No user found'})
  retrieveTimesheet(@Param('company') company: string, @Param('username') username: string, @Param('token') token: string, @Param('startDate') startDate: string, @Param('endDate') endDate: string): void {
    //Check valid request including authentication
        var status: HttpStatus = validateAndAuthenticateRequest(company, username, token);
        //If the status is not null then produce response and return.
        if ( status != null ) {
            return new ResponseEntity<>(status);
        }
        //Now retrieve the user based on the username.
        var user: User = userService.findByCompanyAndUserName(company, username);
        //If user is null then return 204.
        if ( user == null ) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        //Convert dates to LocalDates.
        var localStartDate: Date = DateUtils.convertDateToLocalDate(startDate);
        var localEndDate: Date = DateUtils.convertDateToLocalDate(endDate);
        //Perform either date range or single date.
        if ( localStartDate != null && localEndDate != null && localStartDate.isEqual(localEndDate) ) {
            return ResponseEntity.ok(userService.getHoursForDate(user, localStartDate));
        } else {
            return ResponseEntity.ok(userService.getHoursForDateRange(user, localStartDate, localEndDate));
        }
  }

  @Patch('/timesheet')
  @ApiOperation({ summary: "Add a number of hours to the user's timesheet", description: "Add a number of hours to a specified date for a specified user." })
  @ApiResponse({ status: 200, description: 'Successfully added hours'})
  @ApiResponse({ status: 204, description: 'No user found'})
  addHours(@Body() addHoursRequest: AddTimesheetHoursRequest): void {
    //Check valid request including authentication
        var status: HttpStatus = validateAndAuthenticateRequest(addTimeSheetHoursRequest.getCompany(), addTimeSheetHoursRequest.getUsername(), addTimeSheetHoursRequest.getToken());
        //If the status is not null then produce response and return.
        if ( status != null ) {
            return new ResponseEntity<>(status);
        }
        //Now retrieve the user based on the username.
        var user: User = userService.findByCompanyAndUserName(addTimeSheetHoursRequest.getCompany(), addTimeSheetHoursRequest.getUsername());
        //If user is null then return 204.
        if ( user == null ) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        //Now add the hours and return 200 or 500 depending on DB success.
        return userService.addHoursForDate(user, addTimeSheetHoursRequest.getHours(), DateUtils.convertDateToLocalDate(addTimeSheetHoursRequest.getDate())) ?
                ResponseEntity.status(200).build() : ResponseEntity.status(500).build();
  }

  @Patch('/salary')
  @ApiOperation({ summary: 'Update salary information', description: "Update salary information for a particular user." })
  @ApiResponse({ status: 200, description: 'Successfully updated salary information'})
  @ApiResponse({ status: 204, description: 'No user found'})
  updateSalary(@Body() updateSalaryRequest: UpdateSalaryRequest): void {
    //Check valid request including authentication
        var status: HttpStatus = validateAndAuthenticateRequest(updateSalaryRequest.getCompany(), updateSalaryRequest.getUsername(), updateSalaryRequest.getToken());
        //If the status is not null then produce response and return.
        if ( status != null ) {
            return new ResponseEntity<>(status);
        }
        //Now retrieve the user based on the username.
        var user: User = userService.findByCompanyAndUserName(updateSalaryRequest.getCompany(), updateSalaryRequest.getUsername());
        //If user is null then return 204.
        if ( user == null ) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        //Now update salary information and return 200 or 500 depending on DB success.
        return userService.updateSalaryInformation(user, BigDecimal.valueOf(updateSalaryRequest.getHourlyWage()), updateSalaryRequest.getContractedHoursPerWeek() ) ?
            ResponseEntity.status(200).build() : ResponseEntity.status(500).build();
  }

  @Patch('/reset')
  @ApiOperation({ summary: 'Reset user', description: 'Reset password for a user' })
  @ApiResponse({ status: 200, description: 'Successfully processed reset user request'})
  resetUser(@Body() resetUserRequest: ResetUserRequest): void {
    //Verify that user is logged in.
        if ( resetUserRequest.getToken() == null || !userService.checkAuthToken(resetUserRequest.getToken()) ) {
            return ResponseEntity.status(403).build();
        }
        var result: boolean= userService.resetUserPassword(resetUserRequest.getCompany(), resetUserRequest.getUsername(), resetUserRequest.getPassword());
        //If result is true, then return 200 otherwise return 404 to indicate user not found.
        return result ? ResponseEntity.status(200).build() : ResponseEntity.status(404).build();
  }

  @Patch('/password')
  @ApiOperation({ summary: 'Change Password', description: 'Change password for a user' })
  @ApiResponse({ status: 200, description: 'Successfully processed change password request'})
  changePassword(@Body() changePasswordRequest: ChangePasswordRequest): void {
    //Check valid request including authentication
        var status: HttpStatus = validateAndAuthenticateRequest(changePasswordRequest.getCompany(), changePasswordRequest.getUsername(), changePasswordRequest.getToken());
        //If the status is not null then produce response and return.
        if ( status != null ) {
            return new ResponseEntity<>(status);
        }
        var result: boolean = userService.changePassword(changePasswordRequest.getCompany(), changePasswordRequest.getUsername(),
                changePasswordRequest.getCurrentPassword(), changePasswordRequest.getNewPassword());
        //If result is true, then return 200 otherwise return 404 to indicate user not found.
        return result ? ResponseEntity.status(200).build() : ResponseEntity.status(404).build();
  }

  @Patch('/history')
  @ApiOperation({ summary: 'Add a new history entry', description: 'Add a new history entry for a particular user.' })
  @ApiResponse({ status: 200, description: 'Successfully added history entry'})
  @ApiResponse({ status: 204, description: 'No user found'})
  addHistoryEntry(@Body() addHistoryRequest: AddHistoryRequest): void {
    //Check valid request including authentication
        var status: HttpStatus = validateAndAuthenticateRequest(addHistoryEntryRequest.getCompany(), addHistoryEntryRequest.getUsername(), addHistoryEntryRequest.getToken());
        //If the status is not null then produce response and return.
        if ( status != null ) {
            return new ResponseEntity<>(status);
        }
        //Now retrieve the user based on the username.
        var user: User = userService.findByCompanyAndUserName(addHistoryEntryRequest.getCompany(), addHistoryEntryRequest.getUsername());
        //If user is null then return 204.
        if ( user == null ) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        //Now add training course and return 200 or 500 depending on DB success.
        return userService.addUserHistoryEntry(user, DateUtils.convertDateToLocalDate(addHistoryEntryRequest.getDate()),
                UserHistoryReason.valueOf(addHistoryEntryRequest.getReason()), addHistoryEntryRequest.getComment()) ?
                ResponseEntity.status(200).build() : ResponseEntity.status(500).build();
  }

  @Patch('/deactivate')
  @ApiOperation({ summary: 'Deactivate user', description: 'Deactivate a user from the system' })
  @ApiResponse({ status: 200, description: 'Successfully deactivated user' })
  @ApiResponse({ status: 204, description: 'Successful but no user found' })
  deactivate(@Body() deactivateUserRequest: DeactivateUserRequest): void {
    //Check valid request including authentication
        var status: HttpStatus = validateAndAuthenticateRequest(deactivateUserRequest.getCompany(), deactivateUserRequest.getUsername(), deactivateUserRequest.getToken());
        //If the status is not null then produce response and return.
        if ( status != null ) {
            return new ResponseEntity<>(status);
        }
        //Now retrieve the user based on the username.
        var user: User = userService.findByCompanyAndUserName(deactivateUserRequest.getCompany(), deactivateUserRequest.getUsername());
        //If user is null then return 204.
        if ( user == null ) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        //Now deactivate the user based on the username and return the result.
        return ResponseEntity.ok(DeactivateUserResponse.builder()
                .leaveEntitlementForThisYear(this.userService.deactivate(user, new Date(deactivateUserRequest.getLeavingDate()),
                        deactivateUserRequest.isResigned(), deactivateUserRequest.getReason()))
                .build());
  }

  @Patch('/getUser')
  @ApiOperation({ summary: 'Get user', description: 'Method to get a users details by name and date of birth.' })
  @ApiOkResponse({
    description: 'Successfully retrieved user details',
    type: UserResponse
  })
  @ApiResponse({ status: 500, description: 'Database not available' })
  getUser(@Param('name') name: string, @Param('dateOfBirth') dateOfBirth: string, @Param('company') company: string, @Param('token') token: string, @Res() res: Response): UserResponse | null {
    //Check valid request including authentication
        if ( token == null || !this.userService.checkAuthToken(token) ) {
            res.status(HttpStatus.FORBIDDEN).send();
            return null;
        }
        //If name or date of birth is null then bad request.
        if ( name == null || dateOfBirth == null || company === '' ) {
            res.status(HttpStatus.BAD_REQUEST).send();
            return null;
        } else {
            //Now retrieve the user based on the information provided.
            var user: User = this.userService.findUserByDateOfBirthAndNameAndCompany(new Date(dateOfBirth), name.split(" ")[0], name.split(" ")[1], company);
            //If user is null then return 204.
            if ( user == null ) {
                res.status(HttpStatus.NO_CONTENT).send();
            }
            //Convert to UserResponse object and return 200.
            res.status(HttpStatus.OK).send();
            return UserUtils.convertUserToUserResponse(user);
        }
  }

  /**
     * Private helper method to verify that at least username, company and token are all supplied and valid.
     * @param company a <code>String</code> containing the name of the company.
     * @param username a <code>String</code> containing the username.
     * @param token a <code>String</code> containing the token to verify that the user is logged in.
     * @return a <code>HttpStatus</code> which is either filled if it was not authenticated or null if authenticated and valid.
     */
    private validateAndAuthenticateRequest ( company: string, username: string, token: string ): HttpStatus {
        //First of all, check if the username field is empty or null, then return bad request.
        if (username === '' || company === '') {
            return HttpStatus.BAD_REQUEST;
        }
        //Verify that user is logged in.
        if ( token == null || !this.userService.checkAuthToken(token) ) {
            return HttpStatus.FORBIDDEN;
        }
        //If everything was ok then return null.
        return HttpStatus.OK;
    }
}
