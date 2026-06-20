import { Controller, Get, HttpStatus, Res } from '@nestjs/common';
import { ApiOperation, ApiResponse, ApiOkResponse } from '@nestjs/swagger';
import { CompanyResponse } from './responses/company.response';
import type { Response } from 'express';
import { CompanyService } from './company.service';

@Controller('companies')
export class CompaniesController {

  constructor(private readonly companyService: CompanyService) {}

  @Get('/')
  @ApiOperation({ summary: 'Find all companies', description: 'Find all companies stored in PersonalMan.' })
  @ApiOkResponse({
    description: 'Successfully found companies',
    type: [CompanyResponse],
  })
  @ApiResponse({ status: 204, description: 'Successful but no companies in database'})
  getAll(@Res() res: Response): string[] {
    //Retrieve the list of companies.
        var companyNames: string[] = this.companyService.getAllCompanies();
        //If no companies then return 204.
        if ( companyNames.length == 0 ) {
            res.status(HttpStatus.NO_CONTENT).send();
        }
        //Otherwise return 200.
        res.status(HttpStatus.OK).send();
        return companyNames;
  }
}
