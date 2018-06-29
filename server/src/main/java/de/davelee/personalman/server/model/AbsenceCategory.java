package de.davelee.personalman.server.model;

/**
 * Represents the various categories for absences in PersonalMan as Enum.
 * @author Dave Lee
 */
public enum AbsenceCategory {

	/**
	 * represent absences caused by illness.
	 */
	ILLNESS("Illness"),
	/**
	 * represent absences caused by employee taking annual leave or holiday.
	 */
	HOLIDAY("Holiday"),
	/**
	 * represent absences caused by working trips for employees.
	 */
	TRIP("Trip"),
	/**
	 * represent absences caused by employees attending conferences.
	 */
	CONFERENCE("Conference"),
	/**
	 * represent absences caused by when an employee takes a day in lieu as they have worked on a normal free day.
	 */
	DAY_IN_LIEU("Day in Lieu"),
	/**
	 * represents days in lieu that the employee is entitled to.
	 */
	DAY_IN_LIEU_REQUEST("Day in Lieu Request"),
	/**
	 * represent absences caused by federal or public holidays e.g. Christmas.
	 */
	FEDERAL_HOLIDAY("Federal Holiday");

	private String displayText;

	/**
	 * Create a new <code>AbsenceCategory</code> based on the display text.
	 * @param displayText a <code>String</code> with the display text to generate the <code>AbsenceCategory</code> from.
	 */
	AbsenceCategory(String displayText) {
		this.displayText = displayText;
	}
	
	/**
	 * Retrieve a <code>AbsenceCategory</code> object based on the display text.
	 * @param displayText a <code>String</code> with the display text to generate the <code>AbsenceCategory</code> from.
	 * @return a <code>Reason</code> object representing the reason matching the display text or null if no match found.
	 */
	public static AbsenceCategory fromString(String displayText) {
		for (AbsenceCategory absenceCategory: AbsenceCategory.values() ) {
			if ( displayText.contentEquals(absenceCategory.displayText) ) {
				return absenceCategory;
			}
		}
		return null;
	}
	
	/**
	 * Return a string representation of this <code>Reason</code> object - with the display text.
	 * @return a <code>String</code> with the string representation.
	 */
	public String toString() {
		return displayText;
	}

}
