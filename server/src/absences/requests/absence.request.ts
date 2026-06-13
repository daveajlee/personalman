import { ApiProperty } from '@nestjs/swagger';

export class AbsenceRequest {
  // Which company the person making the absence request works for
  @ApiProperty()
  private company: string;

  // The username of the person making the absence request
  @ApiProperty()
  private username: string;

  // The start date of the absence in the format dd-mm-yyyy
  @ApiProperty()
  private startDate: string;

  // The end date of the absence in the format dd-mm-yyyy
  @ApiProperty()
  private endDate: string;

  // The category of the absence
  @ApiProperty()
  private category: string;

  // The token of the user to verify that they are logged in
  @ApiProperty()
  private token: string;

}