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
} from '@nestjs/common';
import {
  ApiQuery,
  ApiOperation,
  ApiOkResponse,
  ApiResponse,
} from '@nestjs/swagger';
import { AbsencesResponse } from './responses/absences.response';
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
  findOrCount(
    @Param('company') company: string,
    @Param('startDate') startDate: string,
    @Param('endDate') endDate: string,
    @Param('token') token: string,
    @Query('username') username?: string,
    @Query('onlyCount') onlyCount?: string,
    @Query('category') category?: string,
    @Res() res: Response
  ): AbsenceResponse[] {
        //Verify request was valid and authenticated.
        var status: HttpStatus = this.validateAndAuthenticateRequest(startDate, endDate, token);
        if ( status != null ) {
            res.status(status).send();
        }
        //Prepare response object.
        var absencesResponse: AbsencesResponse = this.prepareAbsencesResponse();
        //Check if only count parameter was set to true.
        if ( onlyCount ) {
            //Convert category which is required for count.
            var absenceCategory: AbsenceCategory = null;
            if ( category != null ) {
                absenceCategory = AbsenceCategory.fromString(category);
            }
            if ( absenceCategory == null ) {
                res.status(HttpStatus.BAD_REQUEST).send();
            }
            //Now try and count absences.
            if ( username != null ) {
              var count: number = this.absenceService.countAbsences(company, username, new Date(startDate),
                    new Date(endDate), absenceCategory);
              //Set count.
              absencesResponse.setCount(count);
            }
        } else {
            //Now try and find absences. Convert the absences to a list of absence responses.
            var absenceResponses: AbsenceResponse[] = AbsenceUtils.convertAbsencesToAbsenceResponses(this.absenceService.findAbsences(company, username,  new Date(startDate),
                    new Date(endDate)));
            absencesResponse.setCount(absenceResponses.length);
            absencesResponse.setAbsenceResponseList(absenceResponses);
            absencesResponse = AbsenceUtils.calculateAbsencesResponseStatistics(absencesResponse);
        }
        //Return 200 and results.
        return absencesResponse;
  }

  @Post('/')
  @ApiOperation({
    summary: 'Add an absence',
    description: 'Add an absence to the system.',
  })
  @ApiResponse({ status: 201, description: 'Successfully created absence' })
  add(@Body() absenceRequest: AbsenceRequest, @Res() res: Response): void {
    //Verify request was valid and authenticated.
        var status: HttpStatus = this.validateAndAuthenticateRequest(absenceRequest.getStartDate(), absenceRequest.getEndDate(), absenceRequest.getToken());
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
        this.absenceService.save(AbsenceUtils.convertAbsenceRequestToAbsence(absenceRequest,
                new Date(absenceRequest.getStartDate()), new Date(absenceRequest.getEndDate())));
        //Return 201 if saved successfully.
        res.status(HttpStatus.CREATED).send();
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
  delete(
    @Param('company') company: string,
    @Param('startDate') startDate: string,
    @Param('endDate') endDate: string,
    @Param('token') token: string,
    @Res() res: Response,
    @Query('username') username?: string,
  ): void {
    //Verify request was valid and authenticated.
        var status: HttpStatus | null = this.validateAndAuthenticateRequest(startDate, endDate, token);
        if ( status != null ) {
            res.send();
        }
        //Now try and delete absences.
        if ( username != null ) {
            this.absenceService.delete(company, username, new Date(startDate), new Date(endDate));
        }
        //Return 200 if deleted successfully or nothing to delete.
        res.status(HttpStatus.OK).send();
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
        var startLocalDate: Date = new Date(startDate);
        var endLocalDate: Date = new Date(endDate);
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
        let statisticsMap: Map<String, number> = new Map<String, number>();
        var absenceCategories: AbsenceCategory[] = AbsenceCategory.values();
        absenceCategories.forEach(absenceCategory => {
            statisticsMap.set(absenceCategory.toString(), 0);
        })
        return AbsencesResponse.builder()
                .statisticsMap(statisticsMap)
                .build();
    }
}
