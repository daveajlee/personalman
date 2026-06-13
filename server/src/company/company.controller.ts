import { Body, Controller, Delete, Get, HttpStatus, Param, Post } from '@nestjs/common';
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
    //Check valid request including authentication
        var status: HttpStatus = this.validateAndAuthenticateRequest(name, token);
        //If the status is not null then produce response and return.
        if ( status != null ) {
            return new ResponseEntity<>(status);
        }
        var company: Company= companyService.getCompany(name);
        if ( company != null ) {
            return ResponseEntity.ok(CompanyResponse.builder()
                    .name(company.getName())
                    .defaultAnnualLeaveInDays(company.getDefaultAnnualLeaveInDays())
                    .country(company.getCountry())
                    .build());
        }
        //Otherwise 404 to indicate not found.
        return ResponseEntity.status(404).build();
  }

  @Post('/')
  @ApiOperation({ summary: 'Add a company', description: 'Add a company to the system.' })
  @ApiResponse({ status: 201, description: 'Successfully created company'})
  add(@Body() registerCompanyRequest: RegisterCompanyRequest): void {
    companyService.save(Company.builder()
                .id(new ObjectId())
                .name(registerCompanyRequest.getName())
                .defaultAnnualLeaveInDays(registerCompanyRequest.getDefaultAnnualLeaveInDays())
                .country(registerCompanyRequest.getCountry())
                .build());
        //Return 201 if saved successfully.
        return ResponseEntity.status(201).build();
  }

  @Delete('/')
  @ApiOperation({ summary: 'Delete a company', description: 'Delete a company from the system.' })
  @ApiResponse({ status: 200, description: 'Successfully deleted company'})
  @ApiResponse({ status: 404, description: 'Company not found'})
  delete(@Param('name') name: string, @Param('token') token: string): void {
    //Check valid request including authentication
        var status: HttpStatus = this.validateAndAuthenticateRequest(name, token);
        //If the status is not null then produce response and return.
        if ( status != null ) {
            return new ResponseEntity<>(status);
        }
        //First of all, delete all users and absences belonging to this company.
        absenceService.delete(name);
        userService.delete(name);
        //Now delete the company.
        if ( companyService.delete(name) ) {
            //Return 200 if successful delete.
            return ResponseEntity.status(200).build();
        } else {
            //Otherwise 404.
            return ResponseEntity.status(404).build();
        }
  }

  /**
     * Private helper method to verify that at least name and token are supplied and valid.
     * @param name a <code>String</code> containing the name of the company.
     * @param token a <code>String</code> containing the token to verify that the user is logged in.
     * @return a <code>HttpStatus</code> which is either filled if it was not authenticated or null if authenticated and valid.
     */
    private validateAndAuthenticateRequest ( name: string, token: string ) : HttpStatus {
        //First of all, check if the name field is empty or null, then return bad request.
        if (StringUtils.isBlank(name) ) {
            return HttpStatus.BAD_REQUEST;
        }
        //Verify that user is logged in.
        if ( token == null || !userService.checkAuthToken(token) ) {
            return HttpStatus.FORBIDDEN;
        }
        //If everything was ok then return null.
        return null;
    }
}
