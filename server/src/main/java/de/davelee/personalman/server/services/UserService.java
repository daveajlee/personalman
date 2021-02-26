package de.davelee.personalman.server.services;

import de.davelee.personalman.server.model.User;
import de.davelee.personalman.server.repository.UserRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

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

    private HashMap<String, LocalDateTime> loggedInTokens = new HashMap<>();

    /**
     * Save the specified user object in the database.
     * @param user a <code>User</code> object to save in the database.
     */
    public void save ( final User user ) {
        userRepository.save(user);
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
     */
    public boolean resetUserPassword ( final String company, final String username, final String newPassword ) {
        User user = userRepository.findByCompanyAndUserName(company, username);
        if ( user != null ) {
            user.setPassword(newPassword);
            userRepository.save(user);
            return true;
        }
        return false;
    }

    /**
     * Change the password for a particular user if the old password matches the current password in database.
     * @param company a <code>String</code> containing the company that the user is associated with.
     * @param username a <code>String</code> containing the username of the user who's password should be reset.
     * @param oldPassword a <code>String</code> containing the old password that the user had.
     * @param newPassword a <code>String</code> containing the new password that the user should receive.
     */
    public boolean changePassword ( final String company, final String username, final String oldPassword, final String newPassword ) {
        User user = userRepository.findByCompanyAndUserName(company, username);
        if ( user != null ) {
            if ( user.getPassword().contentEquals(oldPassword) ) {
                user.setPassword(newPassword);
                userRepository.save(user);
                return true;
            }
        }
        return false;
    }

}
