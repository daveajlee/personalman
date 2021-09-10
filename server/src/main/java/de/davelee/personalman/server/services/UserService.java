package de.davelee.personalman.server.services;

import de.davelee.personalman.server.model.User;
import de.davelee.personalman.server.model.UserHistoryEntry;
import de.davelee.personalman.server.model.UserHistoryReason;
import de.davelee.personalman.server.repository.UserRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Class to provide service operations for users in the PersonalMan program.
 * @author Dave Lee
 */
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Value("${token.length}")
    private int tokenLength;

    @Value("${logout.minutes}")
    private int timeoutInMinutes;

    private final HashMap<String, LocalDateTime> loggedInTokens = new HashMap<>();

    /**
     * Save the specified user object in the database.
     * @param user a <code>User</code> object to save in the database.
     * @return a <code>boolean</code> which is true iff the user has been saved successfully.
     */
    public boolean save ( final User user ) {
        return userRepository.save(user) != null;
    }

    /**
     * Find a user according to their user name and company.
     * @param company a <code>String</code> with the company to retrieve user for.
     * @param userName a <code>String</code> with the user name.
     * @return a <code>User</code> representing the user which has this user name. Returns null if no matching user. User name is a unique field so no chance of more than one result!
     */
    public User findByCompanyAndUserName ( final String company, final String userName ) {
        return userRepository.findByCompanyAndUserName(company, userName);
    }

    /**
     * Find all users belonging to a company.
     * @param company a <code>String</code> with the company to retrieve users for.
     * @return a <code>List</code> of <code>User</code> objects representing the users belonging to this company. Returns null if no matching users.
     */
    public List<User> findByCompany ( final String company ) {
        return userRepository.findByCompany(company);
    }

    /**
     * Delete the specified user object from the database.
     * @param user a <code>User</code> object to delete from the database.
     */
    public void delete ( final User user ) {
        userRepository.delete(user);
    }

    /**
     * Update the salary information for the specified user object,
     * @param user a <code>User</code> object to set the salary information for.
     * @param hourlyWage a <code>BigDecimal</code> object containing the hourly wage to set for this user.
     * @param contractedHoursPerWeek a <code>int</code> containing the number of contracted hours per week to add.
     * @return a <code>boolean</code> which is true iff the user has been updated successfully.
     */
    public boolean updateSalaryInformation (final User user, final BigDecimal hourlyWage, final int contractedHoursPerWeek ) {
        user.setHourlyWage(hourlyWage);
        user.setContractedHoursPerWeek(contractedHoursPerWeek);
        return userRepository.save(user) != null;
    }

    /**
     * Add a training course or qualification for the specified user object.
     * @param user a <code>User</code> object to set the salary information for.
     * @param trainingCourse a <code>String</code> containing the name of the training course or qualification.
     * @return a <code>boolean</code> which is true iff the user has been updated successfully.
     */
    public boolean addTrainingCourse ( final User user, final String trainingCourse ) {
        if ( user.getTrainingsList() == null ) {
            user.setTrainingsList(new ArrayList<>());
        }
        user.addTrainingCourse(trainingCourse);
        return userRepository.save(user) != null;
    }

    /**
     * Add the number of hours for a particular date to the specified user object.
     * @param user a <code>User</code> object to set the hours for.
     * @param hours a <code>int</code> with the number of hours to add.
     * @param date a <code>LocalDate</code> object containing the day to add the hours to.
     * @return a <code>boolean</code> which is true iff the user has been updated successfully.
     */
    public boolean addHoursForDate ( final User user, final int hours, final LocalDate date ) {
        if ( user.getTimesheet() == null ) {
            user.setTimesheet(new HashMap<>());
        }
        user.addHoursForDate(hours, date);
        return userRepository.save(user) != null;
    }

    /**
     * Add a new history entry to the list.
     * @param user a <code>User</code> object to set the hours for.
     * @param date a <code>LocalDate</code> containing the date that the entry/event took place.
     * @param userHistoryReason a <code>UserHistoryReason</code> containing the reason that the entry/event took place.
     * @param comment a <code>String</code> containing the comment about the entry/event.
     * @return a <code>boolean</code> which is true iff the user has been updated successfully.
     */
    public boolean addUserHistoryEntry (final User user, final LocalDate date, final UserHistoryReason userHistoryReason, final String comment) {
        if ( user.getUserHistoryEntryList() == null ) {
            user.setUserHistoryEntryList(new ArrayList<>());
        }
        user.addUserHistoryEntry(date, userHistoryReason, comment);
        return userRepository.save(user) != null;
    }

    /**
     * Retrieve the number of hours for a particular date with the specified user object,
     * @param user a <code>User</code> object to get the hours for.
     * @param date a <code>LocalDate</code> object containing the day to get the hours for.
     * @return a <code>int</code> with the number of hours.
     */
    public int getHoursForDate ( final User user, final LocalDate date ) {
        return user.getHoursForDate(date);
    }

    /**
     * Retrieve the number of hours for a date range with the specified user object,
     * @param user a <code>User</code> object to get the hours for.
     * @param startDate a <code>LocalDate</code> object containing the first day to get the hours for.
     * @param endDate a <code>LocalDate</code> object containing the last day to get the hours for.
     * @return a <code>int</code> with the number of hours.
     */
    public int getHoursForDateRange ( final User user, final LocalDate startDate, final LocalDate endDate ) {
        int hours = 0;
        LocalDate date = startDate;
        while ( !date.isAfter(endDate) ) {
            hours += getHoursForDate(user, date);
            date = date.plusDays(1);
        }
        return hours;
    }

    /**
     * Generate a token for this user and add it to the list of logged in tokens. The token should expire after the specified amount of minutes in the config.
     * Return the token so that the client can also have access to it.
     * @param userName a <code>String</code> with the user name of the logged in user.
     * @return a <code>String</code> containing the token which is valid for a limited amount of time.
     */
    public String generateAuthToken ( final String userName ) {
        String token = userName + "-" + RandomStringUtils.randomAlphanumeric(tokenLength);
        loggedInTokens.put(token, LocalDateTime.now().plusMinutes(timeoutInMinutes));
        return token;
    }

    /**
     * Check if this is a valid token which is defined as a token that exists in the token storage and has not yet expired.
     * @param token a <code>String</code> containing the token to check.
     * @return a <code>boolean</code> which is true iff the token is valid.
     */
    public boolean checkAuthToken ( final String token ) {
        return loggedInTokens.containsKey(token) && (loggedInTokens.get(token).isEqual(LocalDateTime.now()) || loggedInTokens.get(token).isAfter(LocalDateTime.now()));
    }

    /**
     * Remove the supplied token from the list of logged in tokens.
     * @param token a <code>String</code> containing the token to remove from the list of logged in tokens.
     */
    public void removeAuthToken ( final String token ) {
        loggedInTokens.remove(token);
    }

    /**
     * Reset the password for a particular user.
     * @param company a <code>String</code> containing the company that the user is associated with.
     * @param username a <code>String</code> containing the username of the user who's password should be reset.
     * @param newPassword a <code>String</code> containing the new password that the user should receive.
     * @return a <code>boolean</code> which is true iff the password could be reset successfully.
     */
    public boolean resetUserPassword ( final String company, final String username, final String newPassword ) {
        User user = userRepository.findByCompanyAndUserName(company, username);
        if ( user != null ) {
            user.setPassword(newPassword);
            return userRepository.save(user) != null;
        }
        return false;
    }

    /**
     * Change the password for a particular user if the old password matches the current password in database.
     * @param company a <code>String</code> containing the company that the user is associated with.
     * @param username a <code>String</code> containing the username of the user who's password should be reset.
     * @param oldPassword a <code>String</code> containing the old password that the user had.
     * @param newPassword a <code>String</code> containing the new password that the user should receive.
     * @return a <code>boolean</code> which is true iff the password could be changed successfully.
     */
    public boolean changePassword ( final String company, final String username, final String oldPassword, final String newPassword ) {
        User user = userRepository.findByCompanyAndUserName(company, username);
        if ( user != null ) {
            if ( user.getPassword().contentEquals(oldPassword) ) {
                user.setPassword(newPassword);
                return userRepository.save(user) != null;
            }
        }
        return false;
    }

}
