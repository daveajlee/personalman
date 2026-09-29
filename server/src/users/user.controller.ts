import { Body, Controller, Delete, Get, HttpStatus, Param, Patch, Post, Query, Res, UseGuards, ValidationPipe } from '@nestjs/common';
import { ApiOperation, ApiResponse, ApiOkResponse } from '@nestjs/swagger';
import { AddHistoryRequest } from './requests/addhistory.request';
import { AddTimesheetHoursRequest } from './requests/addtimesheethours.request';
import { AddTrainingRequest } from './requests/addtraining.request';
import { ChangePasswordRequest } from './requests/changepassword.request';
import { DeactivateUserRequest } from './requests/deactivateuser.request';
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
import { AuthGuard } from 'src/auth/auth.guard';

@Controller('user')
export class UserController {

  constructor(private readonly userService: UsersService, private readonly companyService: CompanyService) {}

  @UseGuards(AuthGuard)
  @Get('/')
  @ApiOperation({ summary: 'Find a user', description: 'Find a user in the system.' })
  @ApiOkResponse({
      description: 'Successfully found user',
      type: UserResponse,
  })
  @ApiResponse({ status: 204, description: 'Successful but no user found'})
  async findUser(@Query('company') company: string, @Query('username') username: string, @Res() res: Response): Promise<void> {
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
    if ( !userRequest.getLeaveEntitlementPerYear() || userRequest.getLeaveEntitlementPerYear() <= 0 ) {
        let company: Company = await this.companyService.getCompany(userRequest.getCompany());
        userRequest.setLeaveEntitlementPerYear(company["defaultAnnualLeaveInDays"]);
    }
    //Now convert to user object.
    var user: User = UserUtils.convertUserRequestToUser(userRequest);
    //Return 201 if saved successfully.
    this.userService.save(user) ? res.status(HttpStatus.CREATED).send() : res.status(HttpStatus.INTERNAL_SERVER_ERROR).send();
  }

  @UseGuards(AuthGuard)
  @Delete('/')
  @ApiOperation({ summary: 'Delete a user', description: 'Delete a user from the system.' })
  @ApiResponse({ status: 200, description: 'Successfully delete user'})
  @ApiResponse({ status: 204, description: 'Successful but no user found'})
  async deleteUser(@Query('company') company: string, @Query('username') username: string, @Res() res: Response): Promise<void> {
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

  @UseGuards(AuthGuard)
  @Patch('/training')
  @ApiOperation({ summary: 'Add a training course', description: 'Add a training course for a particular user.' })
  @ApiResponse({ status: 200, description: 'Successfully added training course'})
  @ApiResponse({ status: 204, description: 'No user found'})
  async addTraining(@Body(new ValidationPipe({transform: true})) addTrainingRequest: AddTrainingRequest, @Res() res: Response): Promise<void> {
        //retrieve the user based on the username.
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

  @UseGuards(AuthGuard)
  @Get('/timesheet')
  @ApiOperation({ summary: "Retrieve the user's timesheet", description: "Retrieve number of hours for a specified date (range) for a specified user." })
  @ApiOkResponse({
      description: 'Successfully retrieved hours',
      type: Number,
  })
  @ApiResponse({ status: 204, description: 'No user found'})
  async retrieveTimesheet(@Query('company') company: string, @Query('username') username: string, @Query('startDate') startDate: string, @Query('endDate') endDate: string, @Res() res: Response): Promise<void> {
        //Retrieve the user based on the username.
        var user: User | null = await this.userService.findByCompanyAndUserName(company, username);
        //If user is null then return 204.
        if ( user == null ) {
            res.status(HttpStatus.NO_CONTENT).send();
        } else {
            //Perform either date range or single date.
            if ( startDate != null && endDate != null && startDate == endDate ) {
                res.status(HttpStatus.OK).json(this.userService.getHoursForDate(user, startDate));
            } else {
                res.status(HttpStatus.OK).json(this.userService.getHoursForDateRange(user, startDate, endDate));
            }
        }
  }

  @UseGuards(AuthGuard)
  @Patch('/timesheet')
  @ApiOperation({ summary: "Add a number of hours to the user's timesheet", description: "Add a number of hours to a specified date for a specified user." })
  @ApiResponse({ status: 200, description: 'Successfully added hours'})
  @ApiResponse({ status: 204, description: 'No user found'})
  async addHours(@Body(new ValidationPipe({transform: true})) addHoursRequest: AddTimesheetHoursRequest, @Res() res: Response): Promise<void> {
        //Retrieve the user based on the username.
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

  @UseGuards(AuthGuard)
  @Patch('/salary')
  @ApiOperation({ summary: 'Update salary information', description: "Update salary information for a particular user." })
  @ApiResponse({ status: 200, description: 'Successfully updated salary information'})
  @ApiResponse({ status: 204, description: 'No user found'})
  async updateSalary(@Body(new ValidationPipe({transform: true})) updateSalaryRequest: UpdateSalaryRequest, @Res() res: Response): Promise<void> {
        //Retrieve the user based on the username.
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

  @UseGuards(AuthGuard)
  @Patch('/reset')
  @ApiOperation({ summary: 'Reset user', description: 'Reset password for a user' })
  @ApiResponse({ status: 200, description: 'Successfully processed reset user request'})
  async resetUser(@Body(new ValidationPipe({transform: true})) resetUserRequest: ResetUserRequest, @Res() res: Response): Promise<void> {
        var result: boolean= await this.userService.resetUserPassword(resetUserRequest.getCompany(), resetUserRequest.getUsername(), resetUserRequest.getPassword());
        //If result is true, then return 200 otherwise return 404 to indicate user not found.
        result ? res.status(HttpStatus.OK).send() : res.status(HttpStatus.NOT_FOUND).send();
  }

  @UseGuards(AuthGuard)
  @Patch('/password')
  @ApiOperation({ summary: 'Change Password', description: 'Change password for a user' })
  @ApiResponse({ status: 200, description: 'Successfully processed change password request'})
  async changePassword(@Body(new ValidationPipe({transform: true})) changePasswordRequest: ChangePasswordRequest, @Res() res: Response): Promise<void> {
    var result: boolean = await this.userService.changePassword(changePasswordRequest.getCompany(), changePasswordRequest.getUsername(),
            changePasswordRequest.getCurrentPassword(), changePasswordRequest.getNewPassword());
    //If result is true, then return 200 otherwise return 404 to indicate user not found.
    result ? res.status(HttpStatus.OK).send() : res.status(HttpStatus.NOT_FOUND).send();
  }

  @UseGuards(AuthGuard)
  @Patch('/history')
  @ApiOperation({ summary: 'Add a new history entry', description: 'Add a new history entry for a particular user.' })
  @ApiResponse({ status: 200, description: 'Successfully added history entry'})
  @ApiResponse({ status: 204, description: 'No user found'})
  async addHistoryEntry(@Body(new ValidationPipe({transform: true})) addHistoryRequest: AddHistoryRequest, @Res() res: Response): Promise<void> {
        //Retrieve the user based on the username.
        var user: any = await this.userService.findByCompanyAndUserName(addHistoryRequest.getCompany(), addHistoryRequest.getUsername());
        //If user is null then return 204.
        if ( user == null ) {
            res.status(HttpStatus.NO_CONTENT).send();
        } else {
            //Now add training course and return 200 or 500 depending on DB success.
            this.userService.addUserHistoryEntry(user, UserUtils.convertToDate(addHistoryRequest.getDate()),
                addHistoryRequest.getReason(), addHistoryRequest.getComment()) ?
                res.status(HttpStatus.OK).send() : res.status(HttpStatus.INTERNAL_SERVER_ERROR).send();
        }
  }

  @UseGuards(AuthGuard)
  @Patch('/deactivate')
  @ApiOperation({ summary: 'Deactivate user', description: 'Deactivate a user from the system' })
  @ApiResponse({ status: 200, description: 'Successfully deactivated user' })
  @ApiResponse({ status: 204, description: 'Successful but no user found' })
  async deactivate(@Body(new ValidationPipe({transform: true})) deactivateUserRequest: DeactivateUserRequest, @Res() res: Response): Promise<void> {
        //Retrieve the user based on the username.
        var user: User | null = await this.userService.findByCompanyAndUserName(deactivateUserRequest.getCompany(), deactivateUserRequest.getUsername());
        //If user is null then return 204.
        if ( user == null ) {
            res.status(HttpStatus.NO_CONTENT).send();
        } else {
            //Now deactivate the user based on the username and return the result.
            res.status(HttpStatus.OK).json(new DeactivateUserResponse(await this.userService.deactivate(user, UserUtils.convertToDate(deactivateUserRequest.getLeavingDate()),
                    deactivateUserRequest.isResigned(), deactivateUserRequest.getReason())));
        }
  }

  @UseGuards(AuthGuard)
  @Get('/getUser')
  @ApiOperation({ summary: 'Get user', description: 'Method to get a users details by name and date of birth.' })
  @ApiOkResponse({
    description: 'Successfully retrieved user details',
    type: UserResponse
  })
  @ApiResponse({ status: 500, description: 'Database not available' })
  async getUser(@Query('name') name: string, @Query('dateOfBirth') dateOfBirth: string, @Query('company') company: string, @Res() res: Response): Promise<void> {
        //If name or date of birth is null then bad request.
        if ( name == null || dateOfBirth == null || company === '' ) {
            res.status(HttpStatus.BAD_REQUEST).send();
        } else {
            //Now retrieve the user based on the information provided.
            var user: any = await this.userService.findUserByDateOfBirthAndNameAndCompany(UserUtils.convertToDate(dateOfBirth), name.split(" ")[0], name.split(" ")[1], company);
            //If user is null then return 204.
            if ( user == null ) {
                res.status(HttpStatus.NO_CONTENT).send();
            } else {
                //Convert to UserResponse object and return 200.
                res.status(HttpStatus.OK).json(UserUtils.convertUserToUserResponse(user));
            }
        }
  }

}
