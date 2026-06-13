import { Controller, Get, } from '@nestjs/common';
import { ApiOperation, ApiResponse, ApiOkResponse } from '@nestjs/swagger';
import { CompanyResponse } from './responses/company.response';

@Controller('companies')
export class CompaniesController {
  @Get('/')
  @ApiOperation({ summary: 'Find all companies', description: 'Find all companies stored in PersonalMan.' })
  @ApiOkResponse({
    description: 'Successfully found companies',
    type: [CompanyResponse],
  })
  @ApiResponse({ status: 204, description: 'Successful but no companies in database'})
  getAll(): void {
    //Retrieve the list of companies.
        var companyNames: string[] = companyService.getAllCompanies();
        //If no companies then return 204.
        if ( companyNames.length == 0 ) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        //Otherwise return 200.
        return ResponseEntity.ok(companyNames);
  }
}
