import { Module } from '@nestjs/common';
import { UsersService } from './users.service';
import { UsersController } from './users.controller';
import { UserController } from './user.controller';
import { ConfigModule } from '@nestjs/config';
import { ConfigService } from '@nestjs/config';
import { User } from './models/user.model';

@Module({
  imports: [ConfigModule.forRoot({
      isGlobal: true,
      envFilePath: `.env`,
  }),],
  providers: [UsersService, ConfigService, User],
  controllers: [UsersController, UserController]
})
export class UsersModule {}
