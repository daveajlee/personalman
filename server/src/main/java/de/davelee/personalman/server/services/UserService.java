package de.davelee.personalman.server.services;

import de.davelee.personalman.server.model.User;
import de.davelee.personalman.server.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Class to provide service operations for users in the PersonalMan program.
 * @author Dave Lee
 */
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

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

}
