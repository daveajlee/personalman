import { Body, Controller, Delete, Get, Param, Patch, Post } from '@nestjs/common';
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

@Controller('user')
export class UserController {
  @Post('logout')
  @ApiOperation({ summary: 'Logout', description: 'Logout from the system' })
  @ApiResponse({ status: 200, description: 'Successfully processed logout request'})
  logout(@Body() logoutRequest: LogoutRequest): void {
    //TODO: implement logout
  }

  @Post('login')
  @ApiOperation({ summary: 'Login', description: 'Login to the system' })
  @ApiOkResponse({
      description: 'Successfully processed login request',
      type: LoginResponse,
  })
  login(@Body() loginRequest: LoginRequest): void {
    //TODO: implement login
  }

  @Get('/')
  @ApiOperation({ summary: 'Find a user', description: 'Find a user in the system.' })
  @ApiOkResponse({
      description: 'Successfully found user',
      type: UserResponse,
  })
  @ApiResponse({ status: 204, description: 'Successful but no user found'})
  findUser(@Param('company') company: string, @Param('username') username: string, @Param('token') token: string): void {
    //TODO: find user.
  }

  @Post('/')
  @ApiOperation({ summary: 'Add a user', description: 'Add a user to the system.' })
  @ApiResponse({ status: 201, description: 'Successfully created user'})
  addUser(@Body() userRequest: UserRequest): void {
    //TODO: add user.
  }

  @Delete('/')
  @ApiOperation({ summary: 'Delete a user', description: 'Delete a user from the system.' })
  @ApiResponse({ status: 200, description: 'Successfully delete user'})
  @ApiResponse({ status: 204, description: 'Successful but no user found'})
  deleteUser(@Param('company') company: string, @Param('username') username: string, @Param('token') token: string): void {
    //TODO: delete user.
  }

  @Patch('/training')
  @ApiOperation({ summary: 'Add a training course', description: 'Add a training course for a particular user.' })
  @ApiResponse({ status: 200, description: 'Successfully added training course'})
  @ApiResponse({ status: 204, description: 'No user found'})
  addTraining(@Body() addTrainingRequest: AddTrainingRequest): void {
    //TODO: add training course.
  }

  @Get('/timesheet')
  @ApiOperation({ summary: "Retrieve the user's timesheet", description: "Retrieve number of hours for a specified date (range) for a specified user." })
  @ApiOkResponse({
      description: 'Successfully retrieved hours',
      type: Number,
  })
  @ApiResponse({ status: 204, description: 'No user found'})
  retrieveTimesheet(@Param('company') company: string, @Param('username') username: string, @Param('token') token: string, @Param('startDate') startDate: string, @Param('endDate') endDate: string): void {
    //TODO: retrieve timesheet.
  }

  @Patch('/timesheet')
  @ApiOperation({ summary: "Add a number of hours to the user's timesheet", description: "Add a number of hours to a specified date for a specified user." })
  @ApiResponse({ status: 200, description: 'Successfully added hours'})
  @ApiResponse({ status: 204, description: 'No user found'})
  addHours(@Body() addHoursRequest: AddTimesheetHoursRequest): void {
    //TODO: add hours.
  }

  @Patch('/salary')
  @ApiOperation({ summary: 'Update salary information', description: "Update salary information for a particular user." })
  @ApiResponse({ status: 200, description: 'Successfully updated salary information'})
  @ApiResponse({ status: 204, description: 'No user found'})
  updateSalary(@Body() updateSalaryRequest: UpdateSalaryRequest): void {
    //TODO: update salary information.
  }

  @Patch('/reset')
  @ApiOperation({ summary: 'Reset user', description: 'Reset password for a user' })
  @ApiResponse({ status: 200, description: 'Successfully processed reset user request'})
  resetUser(@Body() resetUserRequest: ResetUserRequest): void {
    //TODO: reset user.
  }

  @Patch('/password')
  @ApiOperation({ summary: 'Change Password', description: 'Change password for a user' })
  @ApiResponse({ status: 200, description: 'Successfully processed change password request'})
  changePassword(@Body() changePasswordRequest: ChangePasswordRequest): void {
    //TODO: change password.
  }

  @Patch('/history')
  @ApiOperation({ summary: 'Add a new history entry', description: 'Add a new history entry for a particular user.' })
  @ApiResponse({ status: 200, description: 'Successfully added history entry'})
  @ApiResponse({ status: 204, description: 'No user found'})
  addHistoryEntry(@Body() addHistoryRequest: AddHistoryRequest): void {
    //TODO: add a new history entry.
  }

  @Patch('/deactivate')
  @ApiOperation({ summary: 'Deactivate user', description: 'Deactivate a user from the system' })
  @ApiResponse({ status: 200, description: 'Successfully deactivated user' })
  @ApiResponse({ status: 204, description: 'Successful but no user found' })
  deactivate(@Body() deactivateUserRequest: DeactivateUserRequest): void {
    //TODO: deactivate user.
  }

  @Patch('/getUser')
  @ApiOperation({ summary: 'Get user', description: 'Method to get a users details by name and date of birth.' })
  @ApiOkResponse({
    description: 'Successfully retrieved user details',
    type: UserResponse
  })
  @ApiResponse({ status: 500, description: 'Database not available' })
  getUser(@Param('name') name: string, @Param('dateOfBirth') dateOfBirth: string, @Param('company') company: string, @Param('token') token: string): void {
    //TODO: get user by name and date of birth.
  }
}
