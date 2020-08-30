package de.davelee.personalman.server.model;

import com.google.common.base.Objects;

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
	
	/**
	 * Default constructor for Spring etc.
	 */
	public Absence() {
		
	}
	
	/**
	 * Create a new absence.
	 * @param username a <code>String</code> with the user name of the person who is absent.
	 * @param company a <code>String</code> with the company that the person works for.
	 * @param startDate a <code>LocalDate</code> with the start date of the absence.
	 * @param endDate a <code>LocalDate</code> with the end date of the absence.
	 * @param absenceCategory a <code>AbsenceCategory</code> with the category for the absence (see AbsenceCategory Enum).
	 */
	public Absence(final String username, final String company, final LocalDate startDate, final LocalDate endDate,
				   final AbsenceCategory absenceCategory) {
		this.username = username;
		this.company = company;
		this.startDate = startDate;
		this.endDate = endDate;
		this.category = absenceCategory;
	}

	/**
	 * Return the id of this absence.
	 * @return a <code>long</code> with the absence id.
	 */
	public long getId() {
		return id;
	}

	/**
	 * Set a new id for this absence.
	 * @param id a <code>long</code> containing the new id.
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * Return the user name of the person who is absent.
	 * @return a <code>String</code> containing the user name.
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * Set the user name of the person who is absent.
	 * @param username a <code>String</code> with the new user name.
	 */
	public void setUsername(final String username) {
		this.username = username;
	}

	/**
	 * Return the start date of the absence.
	 * @return a <code>LocalDate</code> containing the start date of the absence.
	 */
	public LocalDate getStartDate() {
		return startDate;
	}

	/**
	 * Set the start date of the absence.
	 * @param startDate a <code>LocalDate</code> containing the start date of the absence.
	 */
	public void setStartDate(final LocalDate startDate) {
		this.startDate = startDate;
	}

	/**
	 * Return the end date of the absence.
	 * @return a <code>LocalDate</code> containing the end date of the absence.
	 */
	public LocalDate getEndDate() {
		return endDate;
	}

	/**
	 * Set the end date of the absence.
	 * @param endDate a <code>LocalDate</code> containing the end date of the absence.
	 */
	public void setEndDate(final LocalDate endDate) {
		this.endDate = endDate;
	}

	/**
	 * Get the category for the absence.
	 * @return a <code>AbsenceCategory</code> object containing the category for the absence.
	 */
	public AbsenceCategory getCategory() {
		return category;
	}

	/**
	 * Set the category for the absence.
	 * @param category a <code>AbsenceCategory</code> object containing the category for the absence.
	 */
	public void setCategory(final AbsenceCategory category) {
		this.category = category;
	}

	/**
	 * Get the company for the absence.
	 * @return a <code>String</code> object containing the company for the absence.
	 */
	public String getCompany() {
		return company;
	}

	/**
	 * Set the company for the absence.
	 * @param company a <code>String</code> object containing the company for the absence.
	 */
	public void setCompany(String company) {
		this.company = company;
	}

	/**
	 * Return a string representation of this object.
	 * @return a <code>String</code> with all variables contained in this object.
	 */
	public String toString() {
		return Objects.toStringHelper(this.getClass()).add("username", username)
				.add("startDate", startDate).add("endDate", endDate)
				.add("company", company)
				.add("category", category).toString();
	}

}
