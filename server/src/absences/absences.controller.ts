import {
  Body,
  Controller,
  Delete,
  Get,
  HttpStatus,
  Param,
  Post,
  Query,
  Res,
  ValidationPipe
} from '@nestjs/common';
import {
  ApiQuery,
  ApiOperation,
  ApiOkResponse,
  ApiResponse,
} from '@nestjs/swagger';
import { AbsencesResponse } from './responses/absences.response';
import { AbsenceResponse } from './responses/absence.response';
import { AbsenceRequest } from './requests/absence.request';
import { AbsencesService } from './absences.service';
import { AbsenceCategory } from './models/absencecategory.enum';
import type { Response } from 'express';
import { UsersService } from 'src/users/users.service';
import { AbsenceUtils } from './utils/absence.utils';

@Controller('absences')
export class AbsencesController {

  constructor(private readonly absenceService: AbsencesService, private readonly userService: UsersService) {}

  @Get('/')
  @ApiQuery({
    name: 'username',
    type: String,
    description: 'Username',
    required: false,
  })
  @ApiQuery({
    name: 'onlyCount',
    type: Boolean,
    description: 'Only count absences',
    required: false,
    default: false,
  })
  @ApiQuery({
    name: 'category',
    type: String,
    description: 'Absence category',
    required: false,
  })
  @ApiOperation({
    summary: 'Find or count absences',
    description:
      'Find or count absences in the system according to the specified criteria.',
  })
  @ApiOkResponse({
    description: 'Successfully completed the search for absences',
    type: [AbsencesResponse],
  })
  async findOrCount(
    @Query('company') company: string,
    @Query('startDate') startDate: string,
    @Query('endDate') endDate: string,
    @Query('token') token: string,
    @Res() res: Response,
    @Query('username') username?: string,
    @Query('onlyCount') onlyCount?: string,
    @Query('category') category?: string,
  ): Promise<void> {
        //Verify request was valid and authenticated.
        var status: HttpStatus | null = this.validateAndAuthenticateRequest(startDate, endDate, token);
        if ( status != null ) {
            res.status(status).send();
        }
        //Prepare response object.
        var absencesResponse: AbsencesResponse = this.prepareAbsencesResponse();
        //Check if only count parameter was set to true.
        if ( onlyCount === "true" ) {
            //Convert category which is required for count.
            var absenceCategory: AbsenceCategory | null = null;
            if ( category != null ) {
                absenceCategory = AbsenceUtils.absenceCategoryFromString(category);
            }
            if ( absenceCategory == null ) {
                res.status(HttpStatus.BAD_REQUEST).send();
            }
            //Now try and count absences.
            if ( username != null && absenceCategory != null ) {
              var count: number = await this.absenceService.countAbsences(company, username, this.convertToDate(startDate),
                    this.convertToDate(endDate), absenceCategory);
              //Set count.
              absencesResponse.setCount(count);
            }
        } else if (username != null) {
            //Now try and find absences. Convert the absences to a list of absence responses.
            var absenceResponses: AbsenceResponse[] = AbsenceUtils.convertAbsencesToAbsenceResponses(await this.absenceService.findAbsences(company, username,  this.convertToDate(startDate),
                    this.convertToDate(endDate)));
            absencesResponse.setCount(absenceResponses.length);
            absencesResponse.setAbsenceResponseList(absenceResponses);
            absencesResponse = AbsenceUtils.calculateAbsencesResponseStatistics(absencesResponse);
        }
        //Return 200 and results.
        res.status(HttpStatus.OK).json(absencesResponse);
  }

  @Post('/')
  @ApiOperation({
    summary: 'Add an absence',
    description: 'Add an absence to the system.',
  })
  @ApiResponse({ status: 201, description: 'Successfully created absence' })
  async add(@Body(new ValidationPipe({transform: true})) absenceRequest: AbsenceRequest, @Res() res: Response): Promise<void> {
    //Verify request was valid and authenticated.
    var status: HttpStatus | null = this.validateAndAuthenticateRequest(absenceRequest.getStartDate(), absenceRequest.getEndDate(), absenceRequest.getToken());
        if ( status != null ) {
            res.send();
        }
        //First of all, check if any of the fields are empty or null, then return bad request.
        if (absenceRequest.getCategory() === "" || absenceRequest.getCompany() === ""
                || absenceRequest.getEndDate() === "" || absenceRequest.getStartDate() === ""
                || absenceRequest.getUsername() === "" ) {
            res.status(HttpStatus.BAD_REQUEST).send();
        }
        //Now convert to absence object.
        var result = await this.absenceService.save(AbsenceUtils.convertAbsenceRequestToAbsence(absenceRequest,
                this.convertToDate(absenceRequest.getStartDate()), this.convertToDate(absenceRequest.getEndDate())));
        //Return 201 if saved successfully.
        result ? res.status(HttpStatus.CREATED).send() : res.status(HttpStatus.INTERNAL_SERVER_ERROR).send();
  }

  // Helper method to convert dates.
  convertToDate(date: string): Date {
    // First split the date.
    let dateSplit = date.split("-");
    return new Date(parseInt(dateSplit[2]), parseInt(dateSplit[1])-1, parseInt(dateSplit[0]));
  }

  @Delete('/')
  @ApiOperation({
    summary: 'Delete absences',
    description:
      'Delete absences in the system according to the specified criteria.',
  })
  @ApiQuery({
    name: 'username',
    type: String,
    description: 'Username',
    required: false,
  })
  @ApiResponse({ status: 200, description: 'Successfully deleted absences' })
  async delete(
    @Query('company') company: string,
    @Query('startDate') startDate: string,
    @Query('endDate') endDate: string,
    @Query('token') token: string,
    @Res() res: Response,
    @Query('username') username?: string,
  ): Promise<void> {
    //Verify request was valid and authenticated.
    var status: HttpStatus | null = this.validateAndAuthenticateRequest(startDate, endDate, token);
    if ( status != null ) {
        res.send();
    } else {
        //Now try and delete absences.
        if ( username != null ) {
            await this.absenceService.delete(company, username, this.convertToDate(startDate), this.convertToDate(endDate));
        }
        //Return 200 if deleted successfully or nothing to delete.
        res.status(HttpStatus.OK).send();
    }
  }

  /**
     * Private helper method to verify that token is supplied and valid and that the start and end dates are valid.
     * @param startDate a <code>String</code> containing the name of the company.
     * @param endDate a <code>String</code> containing the username.
     * @param token a <code>String</code> containing the token to verify that the user is logged in.
     * @return a <code>HttpStatus</code> which is either filled if it was not authenticated or null if authenticated and valid.
     */
    private validateAndAuthenticateRequest ( startDate: string, endDate: string, token: string ): HttpStatus | null {
        //First of all, check if the start and end date fields are valid. If not, then return bad request.
        if ( startDate === "" || endDate === "" || token === "" ) {
            return HttpStatus.BAD_REQUEST;
        }
        var startLocalDate: Date = this.convertToDate(startDate);
        var endLocalDate: Date = this.convertToDate(endDate);
        if ( startLocalDate == null || endLocalDate == null || endLocalDate < startLocalDate ) {
            return HttpStatus.BAD_REQUEST;
        }
        //Verify that user is logged in.
        if ( token == null || !this.userService.checkAuthToken(token) ) {
            return HttpStatus.FORBIDDEN;
        }
        //If everything was ok then return null.
        return null;
    }

    /**
     * Private helper method to prepare AbsencesResponse.
     * @return a <code>AbsencesResponse</code> object containing the basic statistics map to.
     */
    private prepareAbsencesResponse ( ) : AbsencesResponse {
        let statisticsMap: Map<string, number> = new Map<string, number>();
        for ( var absenceCategory in AbsenceCategory ) {
          statisticsMap.set(absenceCategory.toString(), 0);
        }
        return new AbsencesResponse(statisticsMap);
    }
}
