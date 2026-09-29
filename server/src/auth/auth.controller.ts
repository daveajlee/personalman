import { Body, Controller, Post, HttpCode, HttpStatus } from '@nestjs/common';
import { AuthService } from './auth.service.js';
import { LoginRequest } from './requests/login.request.js';

@Controller('auth')
export class AuthController {
  constructor(private readonly authService: AuthService) {}

  @HttpCode(HttpStatus.OK)
  @Post('login')
  signIn(@Body() loginRequest: LoginRequest) {
    return this.authService.login(loginRequest["company"], loginRequest["username"], loginRequest["password"]);
  }
}