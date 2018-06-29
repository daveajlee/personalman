package de.davelee.personalman.api;

/**
 * This class is part of the PersonalMan REST API. It represents a response from the server for a particular user
 * containing first name, surname, username, company they work for, how much leave they are entitled to per year,
 * which days they work, their position and their start date.
 * @author Dave Lee
 */
public class UserResponse {

    // first name of the user
    private String firstName;

    // surname of the user
    private String surname;

    // username
    private String username;

    // company associated with
    private String company;

    // leave entitlement for this user (in days per year)
    private int leaveEntitlementPerYear;

    // which days of the week that the users works comma-separated (e.g. Monday,Tuesday,Wednesday,Thursday)
    private String workingDays;

    // the position of this user
    private String position;

    // start date for the user in format dd-MM-yyyy
    private String startDate;

    /**
     * Default constructor which takes no parameters and sets all parameters to either an empty string or 0 when it is
     * an integer value.
     */
    public UserResponse() {
    }

    /**
     * This constructor creates a new User Response based on the information retrieved by the server.
     * @param firstName a <code>String</code> containing the first name of the user.
     * @param surname a <code>String</code> containing the surname of the user.
     * @param username a <code>String</code> containing the user name of the user.
     * @param company a <code>String</code> containing the name of the company that the user works for.
     * @param leaveEntitlementPerYear a <code>int</code> containing the leave entitlement for this year in days.
     * @param workingDays a comma-separated <code>String</code> with the days that the user works.
     * @param position a <code>String</code> containing the position that the user works for in the company.
     * @param startDate a <code>String</code> containing the start date of the user in format dd-MM-yyyy.
     */
    public UserResponse(final String firstName, final String surname, final String username, final String company,
                        final int leaveEntitlementPerYear, final String workingDays, final String position,
                        final String startDate) {
        this.firstName = firstName;
        this.surname = surname;
        this.username = username;
        this.company = company;
        this.leaveEntitlementPerYear = leaveEntitlementPerYear;
        this.workingDays = workingDays;
        this.position = position;
        this.startDate = startDate;
    }

    /**
     * Retrieve the first name of the user.
     * @return a <code>String</code> containing the first name of the user.
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Set the first name of the user.
     * @param firstName a <code>String</code> containing the new first name of the user.
     */
    public void setFirstName(final String firstName) {
        this.firstName = firstName;
    }

    /**
     * Retrieve the surname of the user.
     * @return a <code>String</code> containing the surname of the user.
     */
    public String getSurname() {
        return surname;
    }

    /**
     * Set the surname of the user.
     * @param surname a <code>String</code> containing the new surname of the user.
     */
    public void setSurname(final String surname) {
        this.surname = surname;
    }

    /**
     * Retrieve the user name of the user.
     * @return a <code>String</code> containing the user name of the user.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Set the user name of the user.
     * @param username a <code>String</code> containing the new user name of the user.
     */
    public void setUsername(final String username) {
        this.username = username;
    }

    /**
     * Retrieve the name of the company that the user works for.
     * @return a <code>String</code> containing the name of the company that the user works for.
     */
    public String getCompany() {
        return company;
    }

    /**
     * Set the name of the company that the user works for.
     * @param company a <code>String</code> containing the new name of the company that the user works for.
     */
    public void setCompany(final String company) {
        this.company = company;
    }

    /**
     * Retrieve the leave entitlement of the user per year (in number of days).
     * @return a <code>int</code> containing the leave entitlement of the user per year (in number of days).
     */
    public int getLeaveEntitlementPerYear() {
        return leaveEntitlementPerYear;
    }

    /**
     * Set the leave entitlement of the user per year (in number of days).
     * @param leaveEntitlementPerYear a <code>int</code> containing the new leave entitlement of the user per
     *                                year (in number of days).
     */
    public void setLeaveEntitlementPerYear(final int leaveEntitlementPerYear) {
        this.leaveEntitlementPerYear = leaveEntitlementPerYear;
    }

    /**
     * Retrieve the days that the user works in comma-separated form e.g. Monday,Tuesday,Wednesday.
     * @return a <code>String</code> containing the days that the user works in comma-separated form e.g.
     * Monday,Tuesday,Wednesday.
     */
    public String getWorkingDays() {
        return workingDays;
    }

    /**
     * Set or change the days that the user works in comma-separated form e.g. Monday,Tuesday,Wednesday.
     * @param workingDays a <code>String</code> containing the days that the user works in comma-separated form e.g.
     * Monday,Tuesday,Wednesday.
     */
    public void setWorkingDays(final String workingDays) {
        this.workingDays = workingDays;
    }

    /**
     * Retrieve the position of the user in the company which he works for.
     * @return a <code>String</code> containing the position of the user in the company which he works for.
     */
    public String getPosition() {
        return position;
    }

    /**
     * Set or change the position of the company that the user works for.
     * @param position a <code>String</code> containing the new position of the company that the user works for.
     */
    public void setPosition(final String position) {
        this.position = position;
    }

    /**
     * Retrieve the date that the user started working for this company.
     * @return a <code>String</code> containing the start date in format dd-MM-yyyy.
     */
    public String getStartDate() {
        return startDate;
    }

    /**
     * Set the date that the user started working for this company.
     * @param startDate a <code>String</code> containing the start date in format dd-MM-yyyy.
     */
    public void setStartDate(final String startDate) {
        this.startDate = startDate;
    }

    /**
     * Retrieve a String with all attributes in format name=value,
     * @return a <code>String</code> representation of this object with all attribute names and values.
     */
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("firstName=" + firstName + ",");
        stringBuilder.append("surname=" + surname + ",");
        stringBuilder.append("username=" + username + ",");
        stringBuilder.append("startDate=" + startDate + ",");
        stringBuilder.append("position=" + position + ",");
        stringBuilder.append("workingDays=" + workingDays + ",");
        stringBuilder.append("leaveEntitlementPerYear=" + leaveEntitlementPerYear + ",");
        stringBuilder.append("company=" + company);
        return stringBuilder.toString();
    }

}
