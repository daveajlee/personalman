package de.davelee.personalman.api;

/**
 * This class is part of the PersonalMan REST API. It represents a response from the server containing details
 * of a planned absence. The absence details include company and username of the person requesting the absence as well
 * as the start date, end date and category of the absence.
 * @author Dave Lee
 */
public class AbsenceResponse {

    // Which company the person who is absent works for
    private String company;

    // The username of the person who is absent
    private String username;

    // The start date of the absence in the format dd-mm-yyyy
    private String startDate;

    // The end date of the absence in the format dd-mm-yyyy
    private String endDate;

    // The category of the absence
    private String category;

    /**
     * Create a new absence response based on the supplied information.
     * @param company a <code>String</code> of the company that the person who is absent works for.
     * @param username a <code>String</code> of the username of the person making the absence request.
     * @param startDate a <code>String</code> of the start date of the absence in the format dd-mm-yyyy.
     * @param endDate a <code>String</code> of the end date of the absence in the format dd-mm-yyyy.
     * @param category a <code>String</code> containing the category of the absence.
     */
    public AbsenceResponse(final String company, final String username, final String startDate, final String endDate,
                           final String category) {
        this.company = company;
        this.username = username;
        this.startDate = startDate;
        this.endDate = endDate;
        this.category = category;
    }

    /**
     * Retrieve the company that the person who is absent works for.
     * @return a <code>String</code> with the company that the person who is absent works for.
     */
    public String getCompany() {
        return company;
    }

    /**
     * Set the company that the person who is absent works for.
     * @param company a <code>String</code> with the new company that the person who is absent works for.
     */
    public void setCompany(final String company) {
        this.company = company;
    }

    /**
     * Retrieve the username of the person who is absent.
     * @return a <code>String</code> with the username of the person who is absent.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Set the username of the person who is absent.
     * @param username a <code>String</code> with the new  username of the person who is absent.
     */
    public void setUsername(final String username) {
        this.username = username;
    }

    /**
     * Retrieve the start date of the absence in the format dd-mm-yyyy.
     * @return a <code>String</code> with the start date of the absence in the format dd-mm-yyyy.
     */
    public String getStartDate() {
        return startDate;
    }

    /**
     * Set the start date of the absence in the format dd-mm-yyyy.
     * @param startDate a <code>String</code> with the new start date of the absence in the format dd-mm-yyyy.
     */
    public void setStartDate(final String startDate) {
        this.startDate = startDate;
    }

    /**
     * Retrieve the end date of the absence in the format dd-mm-yyyy.
     * @return a <code>String</code> with the end date of the absence in the format dd-mm-yyyy.
     */
    public String getEndDate() {
        return endDate;
    }

    /**
     * Set the end date of the absence in the format dd-mm-yyyy.
     * @param endDate a <code>String</code> with the new end date of the absence in the format dd-mm-yyyy.
     */
    public void setEndDate(final String endDate) {
        this.endDate = endDate;
    }

    /**
     * Retrieve the category of the absence.
     * @return a <code>String</code> with the category of the absence.
     */
    public String getCategory() {
        return category;
    }

    /**
     * Set the category for the absence.
     * @param category a <code>String</code> with the new category of the absence.
     */
    public void setCategory(final String category) {
        this.category = category;
    }

    /**
     * Retrieve a String with all attributes in format name=value,
     * @return a <code>String</code> representation of this object with all attribute names and values.
     */
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("username=" + username + ",");
        stringBuilder.append("startDate=" + startDate + ",");
        stringBuilder.append("endDate=" + endDate + ",");
        stringBuilder.append("category=" + category + ",");
        stringBuilder.append("company=" + company);
        return stringBuilder.toString();
    }

}
