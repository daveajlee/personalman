import { Module } from '@nestjs/common';
import { CompanyService } from './company.service';
import { CompanyController } from './company.controller';
import { CompaniesController } from './companies.controller';

@Module({
  providers: [CompanyService],
  controllers: [CompanyController, CompaniesController]
})
export class CompanyModule {}
