import { Controller, Get, } from '@nestjs/common';
import { ApiOperation } from '@nestjs/swagger';

@Controller('companies')
export class CompaniesController {
  @Get('/')
  @ApiOperation({ summary: 'Find all companies' })
  getAll(): void {
    //TODO: find all companies.
  }
}
