import { Body, Controller, Delete, Get, Param, Post } from '@nestjs/common';
import { ApiOperation, ApiOkResponse, ApiResponse } from '@nestjs/swagger';
import { CompanyResponse } from './responses/company.response';
import { RegisterCompanyRequest } from './requests/registercompany.request';

@Controller('company')
export class CompanyController {
  @Get('/')
  @ApiOperation({ summary: 'Retrieve a company', description: 'Retrieve a company from the system.' })
  @ApiOkResponse({
    description: 'Successfully retrieved company details',
    type: CompanyResponse
  })
  @ApiResponse({ status: 404, description: 'Company not found'})
  retrieve(@Param('name') name: string, @Param('token') token: string): void {
    //TODO: retrieve a company.
  }

  @Post('/')
  @ApiOperation({ summary: 'Add a company', description: 'Add a company to the system.' })
  @ApiResponse({ status: 201, description: 'Successfully created company'})
  add(@Body() registerCompanyRequest: RegisterCompanyRequest): void {
    //TODO: add company.
  }

  @Delete('/')
  @ApiOperation({ summary: 'Delete a company', description: 'Delete a company from the system.' })
  @ApiResponse({ status: 200, description: 'Successfully deleted company'})
  @ApiResponse({ status: 404, description: 'Company not found'})
  delete(@Param('name') name: string, @Param('token') token: string): void {
    //TODO: delete company.
  }
}
