package de.davelee.personalman.server.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.bson.types.ObjectId;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * Class to represent absences in PersonalMan.
 * @author Dave Lee
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ToString
public class Absence implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2356067291275584025L;
	
	/**
	 * A unique id for this absence.
	 */
	private ObjectId id;

	/**
	 * The user who will be absent.
	 */
	private String username;

	/**
	 * The company that the user works for.
	 */
	private String company;

	/**
	 * The start date of the absence.
	 */
	private LocalDate startDate;

	/**
	 * The final date of absence.
	 */
	private LocalDate endDate;

	/**
	 * The category of the absence.
	 */
	private AbsenceCategory category;

}
