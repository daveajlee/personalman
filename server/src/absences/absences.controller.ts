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
  UseGuards,
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
import { AuthGuard } from 'src/auth/auth.guard';

@Controller('absences')
export class AbsencesController {

  constructor(private readonly absenceService: AbsencesService, private readonly userService: UsersService) {}

  @UseGuards(AuthGuard)
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
    @Res() res: Response,
    @Query('username') username?: string,
    @Query('onlyCount') onlyCount?: string,
    @Query('category') category?: string,
  ): Promise<void> {
        //Prepare response object.
        //var absencesResponse: AbsencesResponse = this.prepareAbsencesResponse();
        var absencesResponse: AbsencesResponse = new AbsencesResponse();
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
              var count: number = await this.absenceService.countAbsences(company, username, AbsenceUtils.convertToDate(startDate),
                    AbsenceUtils.convertToDate(endDate), AbsenceUtils.absenceCategoryFromString(absenceCategory));
              //Set count.
              absencesResponse.setCount(count);
            }
        } else if (username != null) {
            //Now try and find absences. Convert the absences to a list of absence responses.
            var absenceResponses: AbsenceResponse[] = AbsenceUtils.convertAbsencesToAbsenceResponses(await this.absenceService.findAbsences(company, username, AbsenceUtils.convertToDate(startDate),
                    AbsenceUtils.convertToDate(endDate)));
            absencesResponse.setCount(absenceResponses.length);
            absencesResponse.setAbsenceResponseList(absenceResponses);
            absencesResponse = AbsenceUtils.calculateAbsencesResponseStatistics(absencesResponse);
        }
        //Return 200 and results.
        res.status(HttpStatus.OK).json(absencesResponse).send();
  }

  @UseGuards(AuthGuard)
  @Post('/')
  @ApiOperation({
    summary: 'Add an absence',
    description: 'Add an absence to the system.',
  })
  @ApiResponse({ status: 201, description: 'Successfully created absence' })
  async add(@Body(new ValidationPipe({transform: true})) absenceRequest: AbsenceRequest, @Res() res: Response): Promise<void> {
        //First of all, check if any of the fields are empty or null, then return bad request.
        if (absenceRequest.getCategory() === "" || absenceRequest.getCompany() === ""
                || absenceRequest.getEndDate() === "" || absenceRequest.getStartDate() === ""
                || absenceRequest.getUsername() === "" ) {
            res.status(HttpStatus.BAD_REQUEST).send();
        }
        //Now convert to absence object.
        var result = await this.absenceService.save(AbsenceUtils.convertAbsenceRequestToAbsence(absenceRequest));
        //Return 201 if saved successfully.
        result ? res.status(HttpStatus.CREATED).send() : res.status(HttpStatus.INTERNAL_SERVER_ERROR).send();
  }

  @UseGuards(AuthGuard)
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
    @Res() res: Response,
    @Query('username') username?: string,
  ): Promise<void> {
      //Now try and delete absences.
      if ( username != null ) {
          await this.absenceService.delete(company, username, AbsenceUtils.convertToDate(startDate), AbsenceUtils.convertToDate(endDate));
      }
      //Return 200 if deleted successfully or nothing to delete.
      res.status(HttpStatus.OK).send();
  }

}
