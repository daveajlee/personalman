import { Body, Controller, Delete, Get, HttpStatus, Param, Post, Res } from '@nestjs/common';
import { ApiOperation, ApiOkResponse, ApiResponse } from '@nestjs/swagger';
import { CompanyResponse } from './responses/company.response';
import { RegisterCompanyRequest } from './requests/registercompany.request';
import type { Response } from 'express';
import { CompanyService } from './company.service';
import { UsersService } from 'src/users/users.service';
import { AbsencesService } from 'src/absences/absences.service';
import { Company } from './models/company.model';
@Controller('company')
export class CompanyController {

    constructor(private readonly companyService: CompanyService, private readonly userService: UsersService, 
        private readonly absenceService: AbsencesService) {}

  @Get('/')
  @ApiOperation({ summary: 'Retrieve a company', description: 'Retrieve a company from the system.' })
  @ApiOkResponse({
    description: 'Successfully retrieved company details',
    type: CompanyResponse
  })
  @ApiResponse({ status: 404, description: 'Company not found'})
  retrieve(@Param('name') name: string, @Param('token') token: string, @Res() res: Response): CompanyResponse | null {
    //Check valid request including authentication
        var status: Response = this.validateAndAuthenticateRequest(name, token, res);
        //If the status is not null then produce response and return.
        if ( status != null ) {
            res.send();
        }
        var company: Company = this.companyService.getCompany(name);
        if ( company != null ) {
            res.status(HttpStatus.OK).send();
            return new CompanyResponse(company.getName(), company.getDefaultAnnualLeaveInDays(), company.getCountry());
        }
        //Otherwise 404 to indicate not found.
        res.status(HttpStatus.NOT_FOUND).send();
        return null;
  }

  @Post('/')
  @ApiOperation({ summary: 'Add a company', description: 'Add a company to the system.' })
  @ApiResponse({ status: 201, description: 'Successfully created company'})
  add(@Body() registerCompanyRequest: RegisterCompanyRequest, @Res() res: Response): void {
    this.companyService.save(new Company(registerCompanyRequest.getName(), registerCompanyRequest.getDefaultAnnualLeaveInDays(),
        registerCompanyRequest.getCountry()));
        //Return 201 if saved successfully.
        res.status(HttpStatus.CREATED).send();
  }

  @Delete('/')
  @ApiOperation({ summary: 'Delete a company', description: 'Delete a company from the system.' })
  @ApiResponse({ status: 200, description: 'Successfully deleted company'})
  @ApiResponse({ status: 404, description: 'Company not found'})
  async delete(@Param('name') name: string, @Param('token') token: string, @Res() res: Response): Promise<void> {
    //Check valid request including authentication
        var status: Response = this.validateAndAuthenticateRequest(name, token, res);
        //If the status is not null then produce response and return.
        if ( status != null ) {
            res.send();
        }
        //First of all, delete all users and absences belonging to this company.
        this.absenceService.delete(name, "", new Date(), new Date());
        var user = await this.userService.findByCompanyAndUserName(name, "");
        if ( user ) {
            this.userService.delete(user);
        }
        //Now delete the company.
        if ( this.companyService.delete(name) ) {
            //Return 200 if successful delete.
            res.status(HttpStatus.OK).send();
        } else {
            //Otherwise 404.
            res.status(HttpStatus.NOT_FOUND).send();
        }
  }

  /**
     * Private helper method to verify that at least name and token are supplied and valid.
     * @param name a <code>String</code> containing the name of the company.
     * @param token a <code>String</code> containing the token to verify that the user is logged in.
     * @return a <code>HttpStatus</code> which is either filled if it was not authenticated or null if authenticated and valid.
     */
    private validateAndAuthenticateRequest ( name: string, token: string, @Res() res: Response ) : Response {
        //First of all, check if the name field is empty or null, then return bad request.
        if (name === '') {
            res.status(HttpStatus.BAD_REQUEST).send();
        }
        //Verify that user is logged in.
        if ( token == null || !this.userService.checkAuthToken(token) ) {
            res.status(HttpStatus.FORBIDDEN).send();
        }
        //If everything was ok then return null.
        return res;
    }
}
