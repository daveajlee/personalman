package de.davelee.personalman.server.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.persistence.Id;
import javax.persistence.GenerationType;
import javax.persistence.GeneratedValue;
import javax.persistence.ElementCollection;
import javax.persistence.FetchType;
import javax.persistence.Enumerated;
import javax.persistence.EnumType;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

/**
 * Class to represent users in PersonalMan.
 * @author Dave Lee
 */
@Entity
@Table( name = "USER", uniqueConstraints=@UniqueConstraint(columnNames = {"userName"}) )
public class User {

    /**
     * A unique id for this user.
     */
    @Id
    @GeneratedValue( strategy = GenerationType.AUTO )
    @Column( name = "ID" )
    private long id;

    /**
     * The first name of this user.
     */
    @Column ( name = "FIRSTNAME" )
    private String firstName;

    /**
     * The surname of this user.
     */
    @Column ( name = "LASTNAME" )
    private String lastName;

    /**
     * The username for this user.
     */
    @Column ( name = "USERNAME" )
    private String userName;

    /**
     * The company that the user works for.
     */
    @Column ( name = "COMPANY" )
    private String company;

    /**
     * The leave entitlement of the person in days per year.
     */
    @Column ( name = "LEAVEENTITLEMENT" )
    private int leaveEntitlementPerYear;

    /**
     * The names of the working days that the user is normally expected to work.
     */
    @Column ( name = "WORKINGDAYS" )
    @ElementCollection(fetch=FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    private List<DayOfWeek> workingDays;

    /**
     * The current job title of the user.
     */
    @Column ( name = "POSITION" )
    private String position;

    /**
     * The date that the person started at the company.
     */
    @Column ( name = "STARTDATE" )
    private LocalDate startDate;

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
     * Return the first name of this user.
     * @return a <code>String</code> containing the first name of this user.
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Change or set the first name of the user.
     * @param firstName a <code>String</code> containing the first name of this user.
     */
    public void setFirstName(final String firstName) {
        this.firstName = firstName;
    }

    /**
     * Return the surname of this user.
     * @return a <code>String</code> containing the surname of this user.
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Change or set the surname of the user.
     * @param lastName a <code>String</code> containing the surname of this user.
     */
    public void setLastName(final String lastName) {
        this.lastName = lastName;
    }

    /**
     * Return the username of this user.
     * @return a <code>String</code> containing the username of this user.
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Change or set the username of the user.
     * @param userName a <code>String</code> containing the username of this user.
     */
    public void setUserName(final String userName) {
        this.userName = userName;
    }

    /**
     * Return the company that this user works for.
     * @return a <code>String</code> containing the company that the user works for.
     */
    public String getCompany() {
        return company;
    }

    /**
     * Set the company that this user works for.
     * @param company a <code>String</code> containing the company that the user works for.
     */
    public void setCompany(final String company) {
        this.company = company;
    }

    /**
     * Return the leave entitlement of this user in days per year.
     * @return a <code>int</code> containing the leave entitlement of this user in days per year.
     */
    public int getLeaveEntitlementPerYear() {
        return leaveEntitlementPerYear;
    }

    /**
     * Set the leave entitlement for this user in days per year.
     * @param leaveEntitlementPerYear a <code>int</code> containing the leave entitlement in days per year.
     */
    public void setLeaveEntitlementPerYear(final int leaveEntitlementPerYear) {
        this.leaveEntitlementPerYear = leaveEntitlementPerYear;
    }

    /**
     * Return the days of the week that this user normally works.
     * @return a <code>List</code> of <code>DayOfWeek</code> object containing the days that this user works.
     */
    public List<DayOfWeek> getWorkingDays() {
        return workingDays;
    }

    /**
     * Set the days of the week that this user normally works.
     * @param workingDays a <code>List</code> of <code>DayOfWeek</code> object containing the days that the user works.
     */
    public void setWorkingDays(final List<DayOfWeek> workingDays) {
        this.workingDays = workingDays;
    }

    /**
     * Return the current job title of this user.
     * @return a <code>String</code> containing the current job title of this user.
     */
    public String getPosition() {
        return position;
    }

    /**
     * Set the current job title of this user.
     * @param position a <code>String</code> containing the current job title of this user.
     */
    public void setPosition(final String position) {
        this.position = position;
    }

    /**
     * Return the start date of this user.
     * @return a <code>LocalDate</code> object containing the start date of this user.
     */
    public LocalDate getStartDate() {
        return startDate;
    }

    /**
     * Set the start date of this user.
     * @param startDate a <code>LocalDate</code> object containing the start date of this user.
     */
    public void setStartDate(final LocalDate startDate) {
        this.startDate = startDate;
    }
}
