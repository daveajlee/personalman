package de.davelee.personalman.server.repository;

import de.davelee.personalman.server.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

/**
 * Interface class for database operations on users - uses Spring Data Mongo.
 * @author Dave Lee
 */
public interface UserRepository extends MongoRepository<User, Long> {

    /**
     * Find a user according to their company and user name.
     * @param company a <code>String</code> with the company to retrieve user for.
     * @param userName a <code>String</code> with the user name.
     * @return a <code>User</code> representing the user which has this user name. Returns null if no matching user. User name is a unique field so no chance of more than one result!
     */
    User findByCompanyAndUserName ( @Param("company") final String company, @Param("userName") final String userName);

    /**
     * Find a user by their date of birth, name and company they are working for.
     * @param dateOfBirth a <code>LocalDate</code> containing the date of birth for this user in format dd-MM-yyyy.
     * @param firstName a <code>String</code> with the first name of the user.
     * @param lastName a <code>String</code> with the surname of the user.
     * @param company a <code>String</code> containing the company.
     * @return a <code>User</code> object representing the user matching the criteria or null if none found.
     */
    User findByDateOfBirthAndFirstNameAndLastNameAndCompany(LocalDate dateOfBirth, String firstName, String lastName, String company);

    /**
     * Find all users belonging to a company.
     * @param company a <code>String</code> with the company to retrieve users for.
     * @return a <code>List</code> of <code>User</code> objects representing the users belonging to this company. Returns null if no matching users.
     */
    List<User> findByCompany ( @Param("company") final String company );

}
