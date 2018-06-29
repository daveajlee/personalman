package de.davelee.personalman.api;

/**
 * This class is part of the PersonalMan REST API. It represents a response from the server containing details
 * of all matched user according to specified criteria. As well as containing details about the users in form of
 * an array of <code>UserResponse</code> objects, the object also contains a simple count of the users.
 * @author Dave Lee
 */
public class UsersResponse {

    //a count of the number of users which were found by the server.
    private Long count;

    //an array of all users found by the server.
    private UserResponse[] userResponses;

    /**
     * Retrieve the number of users found by the server. This is usually used when counting the users is sufficient
     * and no further details about the users are required.
     * @return a <code>Long</code> object containing the number of users found by the server.
     */
    public Long getCount() {
        return count;
    }

    /**
     * Set the number of users found. This is usually set when counting the users is sufficient
     * and no further details about the users are required.
     * @param count a <code>Long</code> containing the number of users found.
     */
    public void setCount(final Long count) {
        this.count = count;
    }

    /**
     * Retrieve the array of users found by the server. This array may be null if the server was instructed to only
     * count users.
     * @return an array of <code>UserResponse</code> objects containing details of the users found.
     */
    public UserResponse[] getUserResponses() {
        return userResponses;
    }

    /**
     * Set the array of users found. This array does not have to be set if only instructed to count the users.
     * @param userResponses an array of <code>UserResponse</code> objects containing details of the
     *                            users found.
     */
    public void setUserResponses(final UserResponse[] userResponses) {
        this.userResponses = userResponses;
    }
}
