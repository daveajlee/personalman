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
import {Response} from 'express';
import { AbsencesResponse } from './responses/absences.response';
import { AbsenceRequest } from './requests/absence.request';

@Controller('absences')
export class AbsencesController {
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
  ): void {
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
            var count: number = absenceService.countAbsences(company, username, DateUtils.convertDateToLocalDate(startDate),
                    DateUtils.convertDateToLocalDate(endDate), absenceCategory);
            //Set count.
            absencesResponse.setCount(count);
        } else {
            //Now try and find absences. Convert the absences to a list of absence responses.
            var absenceResponses: AbsenceResponse[] = AbsenceUtils.convertAbsencesToAbsenceResponses(absenceService.findAbsences(company, username,  DateUtils.convertDateToLocalDate(startDate),
                    DateUtils.convertDateToLocalDate(endDate)));
            absencesResponse.setCount(absenceResponses.length);
            absencesResponse.setAbsenceResponseList(absenceResponses);
            absencesResponse = AbsenceUtils.calculateAbsencesResponseStatistics(absencesResponse);
        }
        //Return 200 and results.
        return ResponseEntity.ok(absencesResponse);
  }

  @Post('/')
  @ApiOperation({
    summary: 'Add an absence',
    description: 'Add an absence to the system.',
  })
  @ApiResponse({ status: 201, description: 'Successfully created absence' })
  add(@Body() absenceRequest: AbsenceRequest): void {
    //Verify request was valid and authenticated.
        var status: HttpStatus = this.validateAndAuthenticateRequest(absenceRequest.getStartDate(), absenceRequest.getEndDate(), absenceRequest.getToken());
        if ( status != null ) {
            return ResponseEntity.status(status).build();
        }
        //First of all, check if any of the fields are empty or null, then return bad request.
        if (StringUtils.isBlank(absenceRequest.getCategory()) || StringUtils.isBlank(absenceRequest.getCompany())
                || StringUtils.isBlank(absenceRequest.getEndDate()) || StringUtils.isBlank(absenceRequest.getStartDate())
                || StringUtils.isBlank(absenceRequest.getUsername()) ) {
            return ResponseEntity.badRequest().build();
        }
        //Now convert to absence object.
        absenceService.save(AbsenceUtils.convertAbsenceRequestToAbsence(absenceRequest,
                DateUtils.convertDateToLocalDate(absenceRequest.getStartDate()), DateUtils.convertDateToLocalDate(absenceRequest.getEndDate())));
        //Return 201 if saved successfully.
        return ResponseEntity.status(201).build();
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
    @Query('username') username?: string,
  ): void {
    //Verify request was valid and authenticated.
        var status: HttpStatus = validateAndAuthenticateRequest(startDate, endDate, token);
        if ( status != null ) {
            return ResponseEntity.status(status).build();
        }
        //Now try and delete absences.
        absenceService.delete(company, username, DateUtils.convertDateToLocalDate(startDate), DateUtils.convertDateToLocalDate(endDate));
        //Return 200 if deleted successfully or nothing to delete.
        return ResponseEntity.status(200).build();
  }

  /**
     * Private helper method to verify that token is supplied and valid and that the start and end dates are valid.
     * @param startDate a <code>String</code> containing the name of the company.
     * @param endDate a <code>String</code> containing the username.
     * @param token a <code>String</code> containing the token to verify that the user is logged in.
     * @return a <code>HttpStatus</code> which is either filled if it was not authenticated or null if authenticated and valid.
     */
    private validateAndAuthenticateRequest ( startDate: string, endDate: string, token: string ): HttpStatus {
        //First of all, check if the start and end date fields are valid. If not, then return bad request.
        if ( StringUtils.isBlank(startDate) || StringUtils.isBlank(endDate) || StringUtils.isBlank(token) ) {
            return HttpStatus.BAD_REQUEST;
        }
        var startLocalDate: LocalDate = DateUtils.convertDateToLocalDate(startDate);
        var endLocalDate: LocalDate = DateUtils.convertDateToLocalDate(endDate);
        if ( startLocalDate == null || endLocalDate == null || endLocalDate.isBefore(startLocalDate) ) {
            return HttpStatus.BAD_REQUEST;
        }
        //Verify that user is logged in.
        if ( token == null || !userService.checkAuthToken(token) ) {
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
        var statisticsMap: Map<String, number> = new Map<>();
        var absenceCategories: AbsenceCategory[] = AbsenceCategory.values();
        absenceCategories.forEach(absenceCategory => {
            statisticsMap.set(absenceCategory.toString(), 0);
        })
        return AbsencesResponse.builder()
                .statisticsMap(statisticsMap)
                .build();
    }
}
