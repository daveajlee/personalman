import { Controller, Delete, Get, Patch, Post } from '@nestjs/common';
import { ApiOperation } from '@nestjs/swagger';

@Controller('user')
export class UserController {
  @Post('logout')
  @ApiOperation({ summary: 'Logout' })
  logout(): void {
    //TODO: implement logout
  }

  @Post('login')
  @ApiOperation({ summary: 'Login' })
  login(): void {
    //TODO: implement login
  }

  @Get('/')
  @ApiOperation({ summary: 'Find a user' })
  findUser(): void {
    //TODO: find user.
  }

  @Post('/')
  @ApiOperation({ summary: 'Add a user' })
  addUser(): void {
    //TODO: add user.
  }

  @Delete('/')
  @ApiOperation({ summary: 'Delete a user' })
  deleteUser(): void {
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
