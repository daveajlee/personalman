export enum AbsenceCategory {

    /**
	 * represent absences caused by illness.
	 */
	ILLNESS = "Illness",
	/**
	 * represent absences caused by employee taking annual leave or holiday.
	 */
	HOLIDAY = "Holiday",
	/**
	 * represent absences caused by working trips for employees.
	 */
	TRIP = "Trip" ,
	/**
	 * represent absences caused by employees attending conferences.
	 */
	CONFERENCE = "Conference",
	/**
	 * represent absences caused by when an employee takes a day in lieu as they have worked on a normal free day.
	 */
	DAY_IN_LIEU = "Day in Lieu",
	/**
	 * represents days in lieu that the employee is entitled to.
	 */
	DAY_IN_LIEU_REQUEST = "Day in Lieu Request",
	/**
	 * represent absences caused by federal or public holidays e.g. Christmas.
	 */
	FEDERAL_HOLIDAY = "Federal Holiday", 

}