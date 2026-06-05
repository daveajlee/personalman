import { Body, Controller, Delete, Get, Param, Post, Query } from '@nestjs/common';
import { ApiQuery, ApiOperation, ApiOkResponse, ApiResponse } from '@nestjs/swagger';
import { AbsencesResponse } from './responses/absences.response';
import { AbsenceRequest } from './requests/absence.request';

@Controller('absences')
export class AbsencesController {
  @Get('/')
  @ApiQuery({
    name: "username",
    type: String,
    description: "Username",
    required: false
  })
  @ApiQuery({
    name: "onlyCount",
    type: Boolean,
    description: "Only count absences",
    required: false,
    default: false
  })
  @ApiQuery({
    name: "category",
    type: String,
    description: "Absence category",
    required: false
  })
  @ApiOperation({ summary: 'Find or count absences', description: 'Find or count absences in the system according to the specified criteria.' })
  @ApiOkResponse({
    description: 'Successfully completed the search for absences',
    type: [AbsencesResponse]
  }) 
  findOrCount(@Param('company') company: string, @Param('startDate') startDate: string, @Param('endDate') endDate: string, 
  @Param('token') token: string, @Query('username') username?: string, @Query('onlyCount') onlyCount?: string, @Query('category') category?: string
  ): void {
    //TODO: find or count absences.
  }

  @Post('/')
  @ApiOperation({ summary: 'Add an absence', description: 'Add an absence to the system.' })
  @ApiResponse({ status: 201, description: 'Successfully created absence'})
  add(@Body() absenceRequest: AbsenceRequest): void {
    //TODO: add absence.
  }

  @Delete('/')
  @ApiOperation({ summary: 'Delete absences', description: 'Delete absences in the system according to the specified criteria.' })
  @ApiQuery({
    name: "username",
    type: String,
    description: "Username",
    required: false
  })
  @ApiResponse({ status: 200, description: 'Successfully deleted absences'})
  delete(@Param('company') company: string, @Param('startDate') startDate: string, @Param('endDate') endDate: string, 
  @Param('token') token: string, @Query('username') username?: string): void {
    //TODO: delete absence.
  }
}
