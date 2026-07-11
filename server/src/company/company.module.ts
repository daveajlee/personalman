import { Module } from '@nestjs/common';
import { CompanyService } from './company.service';
import { CompanyController } from './company.controller';
import { CompaniesController } from './companies.controller';
import { MongooseModule } from '@nestjs/mongoose';
import { Company } from './models/company.model';
import { CompanySchema } from './company.schema';

@Module({
  imports: [MongooseModule.forFeature([{ name: Company.name, schema: CompanySchema }])],
  providers: [CompanyService],
  controllers: [CompanyController, CompaniesController],
  exports: [CompanyService]
})
export class CompanyModule {}
