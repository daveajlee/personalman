import { Body, Controller, Delete, Get, HttpStatus, Param, Patch, Post, Query, Res, ValidationPipe } from '@nestjs/common';
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
import { UserAccountStatus } from './models/useraccountstatus.enum';
import { CompanyService } from 'src/company/company.service';
import { Company } from 'src/company/models/company.model';
import { DeactivateUserResponse } from './responses/deactivateuser.response';

@Controller('user')
export class UserController {

    constructor(private readonly userService: UsersService, private readonly companyService: CompanyService) {}

  @Post('logout')
  @ApiOperation({ summary: 'Logout', description: 'Logout from the system' })
  @ApiResponse({ status: 200, description: 'Successfully processed logout request'})
  logout(@Body(new ValidationPipe({transform: true})) logoutRequest: LogoutRequest, @Res() res: Response): void {
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
  async login(@Body(new ValidationPipe({transform: true})) loginRequest: LoginRequest, @Res() res: Response): Promise<void> {
    var user: User | null = await this.userService.findByCompanyAndUserName(loginRequest.getCompany(), loginRequest.getUsername());
    if ( user != null && user["accountStatus"]==='ACTIVE' && user["password"] == loginRequest.getPassword() ) {
        res.status(HttpStatus.OK).json(new LoginResponse("", this.userService.generateAuthToken(loginRequest.getUsername())));
    } else if ( user != null ) {
        res.status(HttpStatus.FORBIDDEN).json(new LoginResponse("Password was incorrect!", ""));
    } else {
        res.status(HttpStatus.FORBIDDEN).json(new LoginResponse("User was not found", ""));
    }
  }

  @Get('/')
  @ApiOperation({ summary: 'Find a user', description: 'Find a user in the system.' })
  @ApiOkResponse({
      description: 'Successfully found user',
      type: UserResponse,
  })
  @ApiResponse({ status: 204, description: 'Successful but no user found'})
  async findUser(@Query('company') company: string, @Query('username') username: string, @Query('token') token: string, @Res() res: Response): Promise<void> {
        //Verify that user is logged in.
        if ( token == null || !this.userService.checkAuthToken(token)) {
            res.status(HttpStatus.FORBIDDEN).send();
        } else {
            //Now retrieve the user based on the username.
            var user: any = await this.userService.findByCompanyAndUserName(company, username);
            //If user is null then return 204.
            if ( user == null ) {
                res.status(HttpStatus.NO_CONTENT).send();
            } else {
                //Convert to UserResponse object and return 200.
                res.status(HttpStatus.OK).json(UserUtils.convertUserToUserResponse(user));
            }
        }
  }

  @Post('/')
  @ApiOperation({ summary: 'Add a user', description: 'Add a user to the system.' })
  @ApiResponse({ status: 201, description: 'Successfully created user'})
  async addUser(@Body(new ValidationPipe({transform: true})) userRequest: UserRequest, @Res() res: Response): Promise<void> {
    //First of all, check if any of the fields are empty or null, then return bad request.
        if (userRequest.getFirstName() === "" || userRequest.getSurname() === ""
                || userRequest.getPosition() === "" || userRequest.getStartDate() === ""
                || userRequest.getUsername() === "" || userRequest.getWorkingDays() === ""
                || userRequest.getCompany() === "" ) {
            res.status(HttpStatus.BAD_REQUEST).send();
        }
        // If the leave entitlement is 0 then set it to company default.
        if ( userRequest.getLeaveEntitlementPerYear() <= 0 ) {
            let company: Company = await this.companyService.getCompany(userRequest.getCompany());
            userRequest.setLeaveEntitlementPerYear(company.getDefaultAnnualLeaveInDays());
        }
        // Convert string to date.
        var startDate: Date = this.convertToDate(userRequest.getStartDate());
        var dateOfBirth: Date = this.convertToDate(userRequest.getDateOfBirth());
        // Check dates are not null.
        if ( startDate == null || dateOfBirth == null ) {
            res.status(HttpStatus.BAD_REQUEST).send();
        }
        //Now convert to user object.
        var user: User = UserUtils.convertUserRequestToUser(userRequest, startDate, dateOfBirth);
        //Return 201 if saved successfully.
        this.userService.save(user) ? res.status(HttpStatus.CREATED).send() : res.status(HttpStatus.INTERNAL_SERVER_ERROR).send();
  }

  // Helper method to convert dates.
  convertToDate(date: string): Date {
    // First split the date.
    let dateSplit = date.split("-");
    return new Date(parseInt(dateSplit[2]), parseInt(dateSplit[1])-1, parseInt(dateSplit[0]));
  }

  @Delete('/')
  @ApiOperation({ summary: 'Delete a user', description: 'Delete a user from the system.' })
  @ApiResponse({ status: 200, description: 'Successfully delete user'})
  @ApiResponse({ status: 204, description: 'Successful but no user found'})
  async deleteUser(@Query('company') company: string, @Query('username') username: string, @Query('token') token: string, @Res() res: Response): Promise<void> {
        //Verify that user is logged in.
        if ( token == null || !this.userService.checkAuthToken(token)) {
            res.status(HttpStatus.FORBIDDEN).send();
        } else {
            //Now retrieve the user based on the username.
            var user: any = await this.userService.findByCompanyAndUserName(company, username);
            //If user is null then return 204.
            if ( user == null ) {
                res.status(HttpStatus.NO_CONTENT).send();
            } else {
                //Now delete the user based on the username.
                await this.userService.delete(user);
                //Return 200.
                res.status(HttpStatus.OK).send();
            }
        }   
  }

  @Patch('/training')
  @ApiOperation({ summary: 'Add a training course', description: 'Add a training course for a particular user.' })
  @ApiResponse({ status: 200, description: 'Successfully added training course'})
  @ApiResponse({ status: 204, description: 'No user found'})
  async addTraining(@Body(new ValidationPipe({transform: true})) addTrainingRequest: AddTrainingRequest, @Res() res: Response): Promise<void> {
    //Check valid request including authentication
        var status: Response = this.validateAndAuthenticateRequest(addTrainingRequest.getCompany(), addTrainingRequest.getUsername(), addTrainingRequest.getToken(), res);
        //If the status is not null then produce response and return.
        if ( status != null ) {
            res.status(status.statusCode).send();
        }
        //Now retrieve the user based on the username.
        var user: any = await this.userService.findByCompanyAndUserName(addTrainingRequest.getCompany(), addTrainingRequest.getUsername());
        //If user is null then return 204.
        if ( user == null ) {
            res.status(HttpStatus.NO_CONTENT).send();
        } else {
            //Now add training course and return 200 or 500 depending on DB success.
            await this.userService.addTrainingCourse(user, addTrainingRequest.getTrainingCourse()) ?
                res.status(HttpStatus.OK).send() : res.status(HttpStatus.INTERNAL_SERVER_ERROR).send();
        }
  }

  @Get('/timesheet')
  @ApiOperation({ summary: "Retrieve the user's timesheet", description: "Retrieve number of hours for a specified date (range) for a specified user." })
  @ApiOkResponse({
      description: 'Successfully retrieved hours',
      type: Number,
  })
  @ApiResponse({ status: 204, description: 'No user found'})
  async retrieveTimesheet(@Param('company') company: string, @Param('username') username: string, @Param('token') token: string, @Param('startDate') startDate: string, @Param('endDate') endDate: string, @Res() res: Response): Promise<number | undefined> {
    //Check valid request including authentication
        var status: Response = this.validateAndAuthenticateRequest(company, username, token, res);
        //If the status is not null then produce response and return.
        if ( status != null ) {
            res.send();
        }
        //Now retrieve the user based on the username.
        var user: User | null = await this.userService.findByCompanyAndUserName(company, username);
        //If user is null then return 204.
        if ( user == null ) {
            res.status(HttpStatus.NO_CONTENT).send();
        } else {
            //Perform either date range or single date.
            if ( startDate != null && endDate != null && startDate == endDate ) {
                res.status(HttpStatus.OK).send();
                return this.userService.getHoursForDate(user, startDate);
            } else {
                res.status(HttpStatus.OK).send();
                return this.userService.getHoursForDateRange(user, startDate, endDate);
            }
        }
  }

  @Patch('/timesheet')
  @ApiOperation({ summary: "Add a number of hours to the user's timesheet", description: "Add a number of hours to a specified date for a specified user." })
  @ApiResponse({ status: 200, description: 'Successfully added hours'})
  @ApiResponse({ status: 204, description: 'No user found'})
  async addHours(@Body(new ValidationPipe({transform: true})) addHoursRequest: AddTimesheetHoursRequest, @Res() res: Response): Promise<void> {
    //Check valid request including authentication
    if ( addHoursRequest.getToken() == null || !this.userService.checkAuthToken(addHoursRequest.getToken()) ) {
        res.status(HttpStatus.FORBIDDEN).send();
    } else {
        //Now retrieve the user based on the username.
        var user: User | null = await this.userService.findByCompanyAndUserName(addHoursRequest.getCompany(), addHoursRequest.getUsername());
        //If user is null then return 204.
        if ( user == null ) {
            res.status(HttpStatus.NO_CONTENT).send();
        } else {
        //Now add the hours and return 200 or 500 depending on DB success.
            this.userService.addHoursForDate(user, addHoursRequest.getHours(), addHoursRequest.getDate()) ?
                res.status(HttpStatus.OK).send() : res.status(HttpStatus.INTERNAL_SERVER_ERROR).send();
        }
    }
  }

  @Patch('/salary')
  @ApiOperation({ summary: 'Update salary information', description: "Update salary information for a particular user." })
  @ApiResponse({ status: 200, description: 'Successfully updated salary information'})
  @ApiResponse({ status: 204, description: 'No user found'})
  async updateSalary(@Body(new ValidationPipe({transform: true})) updateSalaryRequest: UpdateSalaryRequest, @Res() res: Response): Promise<void> {
    //Check valid request including authentication
    if ( updateSalaryRequest.getToken() == null || !this.userService.checkAuthToken(updateSalaryRequest.getToken()) ) {
        res.status(HttpStatus.FORBIDDEN).send();
    } else {
        //Now retrieve the user based on the username.
        var user: User | null = await this.userService.findByCompanyAndUserName(updateSalaryRequest.getCompany(), updateSalaryRequest.getUsername());
        //If user is null then return 204.
        if ( user == null ) {
            res.status(HttpStatus.NO_CONTENT).send();
        } else {
            //Now update salary information and return 200 or 500 depending on DB success.
            this.userService.updateSalaryInformation(user, updateSalaryRequest.getHourlyWage(), updateSalaryRequest.getContractedHoursPerWeek() ) ?
                res.status(HttpStatus.OK).send() : res.status(HttpStatus.INTERNAL_SERVER_ERROR).send();
        }
    }
  }

  @Patch('/reset')
  @ApiOperation({ summary: 'Reset user', description: 'Reset password for a user' })
  @ApiResponse({ status: 200, description: 'Successfully processed reset user request'})
  async resetUser(@Body(new ValidationPipe({transform: true})) resetUserRequest: ResetUserRequest, @Res() res: Response): Promise<void> {
    //Verify that user is logged in.
        if ( resetUserRequest.getToken() == null || !this.userService.checkAuthToken(resetUserRequest.getToken()) ) {
            res.status(HttpStatus.FORBIDDEN).send();
        }
        var result: boolean= await this.userService.resetUserPassword(resetUserRequest.getCompany(), resetUserRequest.getUsername(), resetUserRequest.getPassword());
        //If result is true, then return 200 otherwise return 404 to indicate user not found.
        result ? res.status(HttpStatus.OK).send() : res.status(HttpStatus.NOT_FOUND).send();
  }

  @Patch('/password')
  @ApiOperation({ summary: 'Change Password', description: 'Change password for a user' })
  @ApiResponse({ status: 200, description: 'Successfully processed change password request'})
  async changePassword(@Body(new ValidationPipe({transform: true})) changePasswordRequest: ChangePasswordRequest, @Res() res: Response): Promise<void> {
    //Check valid request including authentication
    if ( changePasswordRequest.getToken() == null || !this.userService.checkAuthToken(changePasswordRequest.getToken()) ) {
        res.status(HttpStatus.FORBIDDEN).send();
    } else {
        var result: boolean = await this.userService.changePassword(changePasswordRequest.getCompany(), changePasswordRequest.getUsername(),
                changePasswordRequest.getCurrentPassword(), changePasswordRequest.getNewPassword());
        //If result is true, then return 200 otherwise return 404 to indicate user not found.
        result ? res.status(HttpStatus.OK).send() : res.status(HttpStatus.NOT_FOUND).send();
    }
  }

  @Patch('/history')
  @ApiOperation({ summary: 'Add a new history entry', description: 'Add a new history entry for a particular user.' })
  @ApiResponse({ status: 200, description: 'Successfully added history entry'})
  @ApiResponse({ status: 204, description: 'No user found'})
  async addHistoryEntry(@Body(new ValidationPipe({transform: true})) addHistoryRequest: AddHistoryRequest, @Res() res: Response): Promise<void> {
    //Check valid request including authentication
    if ( addHistoryRequest.getToken() == null || !this.userService.checkAuthToken(addHistoryRequest.getToken()) ) {
        res.status(HttpStatus.FORBIDDEN).send();
    } else {
        //Now retrieve the user based on the username.
        var user: any = await this.userService.findByCompanyAndUserName(addHistoryRequest.getCompany(), addHistoryRequest.getUsername());
        //If user is null then return 204.
        if ( user == null ) {
            res.status(HttpStatus.NO_CONTENT).send();
        } else {
            //Now add training course and return 200 or 500 depending on DB success.
            this.userService.addUserHistoryEntry(user, this.convertToDate(addHistoryRequest.getDate()),
                addHistoryRequest.getReason(), addHistoryRequest.getComment()) ?
                res.status(HttpStatus.OK).send() : res.status(HttpStatus.INTERNAL_SERVER_ERROR).send();
        }
    }
  }

  @Patch('/deactivate')
  @ApiOperation({ summary: 'Deactivate user', description: 'Deactivate a user from the system' })
  @ApiResponse({ status: 200, description: 'Successfully deactivated user' })
  @ApiResponse({ status: 204, description: 'Successful but no user found' })
  async deactivate(@Body(new ValidationPipe({transform: true})) deactivateUserRequest: DeactivateUserRequest, @Res() res: Response): Promise<void> {
    //Check valid request including authentication
    if ( deactivateUserRequest.getToken() == null || !this.userService.checkAuthToken(deactivateUserRequest.getToken()) ) {
        res.status(HttpStatus.FORBIDDEN).send();
    } else {
            //Now retrieve the user based on the username.
            var user: User | null = await this.userService.findByCompanyAndUserName(deactivateUserRequest.getCompany(), deactivateUserRequest.getUsername());
            //If user is null then return 204.
            if ( user == null ) {
                res.status(HttpStatus.NO_CONTENT).send();
            } else {
                //Now deactivate the user based on the username and return the result.
                res.status(HttpStatus.OK).json(new DeactivateUserResponse(await this.userService.deactivate(user, this.convertToDate(deactivateUserRequest.getLeavingDate()),
                        deactivateUserRequest.isResigned(), deactivateUserRequest.getReason())));
            }
    }  
  }

  @Get('/getUser')
  @ApiOperation({ summary: 'Get user', description: 'Method to get a users details by name and date of birth.' })
  @ApiOkResponse({
    description: 'Successfully retrieved user details',
    type: UserResponse
  })
  @ApiResponse({ status: 500, description: 'Database not available' })
  async getUser(@Query('name') name: string, @Query('dateOfBirth') dateOfBirth: string, @Query('company') company: string, @Query('token') token: string, @Res() res: Response): Promise<void> {
        //Check valid request including authentication
        if ( token == null || !this.userService.checkAuthToken(token) ) {
            res.status(HttpStatus.FORBIDDEN).send();
        }
        //If name or date of birth is null then bad request.
        else if ( name == null || dateOfBirth == null || company === '' ) {
            res.status(HttpStatus.BAD_REQUEST).send();
        } else {
            //Now retrieve the user based on the information provided.
            var user: any = await this.userService.findUserByDateOfBirthAndNameAndCompany(this.convertToDate(dateOfBirth), name.split(" ")[0], name.split(" ")[1], company);
            //If user is null then return 204.
            if ( user == null ) {
                res.status(HttpStatus.NO_CONTENT).send();
            } else {
                //Convert to UserResponse object and return 200.
                res.status(HttpStatus.OK).json(UserUtils.convertUserToUserResponse(user));
            }
        }
  }

  /**
     * Private helper method to verify that at least username, company and token are all supplied and valid.
     * @param company a <code>String</code> containing the name of the company.
     * @param username a <code>String</code> containing the username.
     * @param token a <code>String</code> containing the token to verify that the user is logged in.
     * @return a <code>HttpStatus</code> which is either filled if it was not authenticated or null if authenticated and valid.
     */
    private validateAndAuthenticateRequest ( company: string, username: string, token: string, @Res() res: Response ): Response {
        //First of all, check if the username field is empty or null, then return bad request.
        if (username === '' || company === '') {
            return res.status(HttpStatus.BAD_REQUEST).send();
        }
        //Verify that user is logged in.
        if ( token == null || !this.userService.checkAuthToken(token) ) {
            return res.status(HttpStatus.FORBIDDEN).send();
        }
        //If everything was ok then return null.
        return res.status(HttpStatus.OK).send();
    }
}
