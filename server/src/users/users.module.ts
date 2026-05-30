import { Module } from '@nestjs/common';
import { UsersService } from './users.service';
import { UsersController } from './users.controller';
import { UserController } from './user.controller';

@Module({
  providers: [UsersService],
  controllers: [UsersController, UserController]
})
export class UsersModule {}
