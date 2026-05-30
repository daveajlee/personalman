import { Controller, Delete, Get, Post } from '@nestjs/common';
import { ApiOperation } from '@nestjs/swagger';

@Controller('absences')
export class AbsencesController {
  @Get('/')
  @ApiOperation({ summary: 'Find or count absences' })
  findOrCount(): void {
    //TODO: find or count absences.
  }

  @Post('/')
  @ApiOperation({ summary: 'Add an absence' })
  add(): void {
    //TODO: add absence.
  }

  @Delete('/')
  @ApiOperation({ summary: 'Delete absences' })
  delete(): void {
    //TODO: delete absence.
  }
}
