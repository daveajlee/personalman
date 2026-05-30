import { Controller, Delete, Get, Post } from '@nestjs/common';
import { ApiOperation } from '@nestjs/swagger';

@Controller('company')
export class CompanyController {
  @Get('/')
  @ApiOperation({ summary: 'Retrieve a company' })
  retrieve(): void {
    //TODO: retrieve a company.
  }

  @Post('/')
  @ApiOperation({ summary: 'Add a company' })
  add(): void {
    //TODO: add company.
  }

  @Delete('/')
  @ApiOperation({ summary: 'Delete a company' })
  delete(): void {
    //TODO: delete company.
  }
}
