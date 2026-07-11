import { Module } from '@nestjs/common';
import { UsersService } from './users.service';
import { UsersController } from './users.controller';
import { UserController } from './user.controller';
import { ConfigModule } from '@nestjs/config';
import { ConfigService } from '@nestjs/config';
import { MongooseModule } from '@nestjs/mongoose';
import { User } from './models/user.model';
import { UserSchema } from './user.schema';
import { CompanyModule } from 'src/company/company.module';

@Module({
  imports: [ConfigModule.forRoot({
      isGlobal: true,
      envFilePath: `.env`,
  }), MongooseModule.forFeature([{ name: User.name, schema: UserSchema }]), CompanyModule],
  providers: [UsersService, ConfigService],
  controllers: [UsersController, UserController],
  exports: [UsersService]
})
export class UsersModule {}
