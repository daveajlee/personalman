package de.davelee.personalman.server.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.persistence.Id;
import javax.persistence.GenerationType;
import javax.persistence.Column;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * Class to represent absences in PersonalMan.
 * @author Dave Lee
 */
@Entity
@Table( name = "ABSENCE", uniqueConstraints=@UniqueConstraint(columnNames = {"company", "username", "startDate", "endDate"})  )
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
	@Id
	@GeneratedValue( strategy = GenerationType.AUTO )
	@Column ( name = "ID" )
	private long id;

	/**
	 * The user who will be absent.
	 */
	@Column ( name = "USERNAME" )
	private String username;

	/**
	 * The company that the user works for.
	 */
	@Column ( name = "COMPANY" )
	private String company;

	/**
	 * The start date of the absence.
	 */
	@Column ( name = "STARTDATE" )
	private LocalDate startDate;

	/**
	 * The final date of absence.
	 */
	@Column ( name = "ENDDATE" )
	private LocalDate endDate;

	/**
	 * The category of the absence.
	 */
	@Column ( name = "CATEGORY")
	private AbsenceCategory category;

}
