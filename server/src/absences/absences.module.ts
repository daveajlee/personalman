import { Module } from '@nestjs/common';
import { AbsencesService } from './absences.service';
import { AbsencesController } from './absences.controller';
import { AbsenceSchema } from './absence.schema';
import { MongooseModule } from '@nestjs/mongoose';
import { Absence } from './models/absence.model';
import { UsersService } from 'src/users/users.service';
import { UsersModule } from 'src/users/users.module';

@Module({
  imports: [MongooseModule.forFeature([{ name: Absence.name, schema: AbsenceSchema }]), UsersModule],
  providers: [AbsencesService],
  controllers: [AbsencesController]
})
export class AbsencesModule {}
