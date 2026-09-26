import { Injectable, UnauthorizedException } from '@nestjs/common';
import { UsersService } from '../users/users.service.js';
import { User } from 'src/users/models/user.model.js';

@Injectable()
export class AuthService {
  constructor(private readonly usersService: UsersService) {}

  async login(company: string, username: string, pass: string): Promise<any> {
    const user: User | null = await this.usersService.findByCompanyAndUserName(company, username);
    if ( user != null && user["accountStatus"]==='ACTIVE' && user["password"] == pass ) {
        // TODO: Generate a JWT and return it here
        // instead of the user object
        return user;
    } else {
        throw new UnauthorizedException();
    }
  }
}