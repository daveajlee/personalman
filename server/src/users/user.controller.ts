import { Body, Controller, Delete, Get, Param, Patch, Post } from '@nestjs/common';
import { ApiOperation, ApiResponse, ApiOkResponse } from '@nestjs/swagger';
import { LogoutRequest } from './requests/logout.request';
import { LoginResponse } from './responses/login.response';
import { LoginRequest } from './requests/login.request';
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
  @ApiOperation({ summary: 'Add a training course' })
  addTraining(): void {
    //TODO: add training course.
  }

  @Get('/timesheet')
  @ApiOperation({ summary: "Retrieve the user's timesheet" })
  retrieveTimesheet(): void {
    //TODO: retrieve timesheet.
  }

  @Patch('/timesheet')
  @ApiOperation({ summary: "Add a number of hours to the user's timesheet" })
  addHours(): void {
    //TODO: add hours.
  }

  @Patch('/salary')
  @ApiOperation({ summary: 'Update salary information' })
  updateSalary(): void {
    //TODO: update salary information.
  }

  @Patch('/reset')
  @ApiOperation({ summary: 'Reset user' })
  resetUser(): void {
    //TODO: reset user.
  }

  @Patch('/password')
  @ApiOperation({ summary: 'Change Password' })
  changePassword(): void {
    //TODO: change password.
  }

  @Patch('/history')
  @ApiOperation({ summary: 'Add a new history entry' })
  addHistoryEntry(): void {
    //TODO: add a new history entry.
  }

  @Patch('/deactivate')
  @ApiOperation({ summary: 'Deactivate user' })
  deactivate(): void {
    //TODO: deactivate user.
  }

  @Patch('/getUser')
  @ApiOperation({ summary: 'Get user' })
  getUser(): void {
    //TODO: get user by name and date of birth.
  }
}
