package de.davelee.personalman.server.repository;

import de.davelee.personalman.server.model.Company;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;

/**
 * Interface class for database operations on companies - uses Spring Data Mongo.
 * @author Dave Lee
 */
public interface CompanyRepository extends MongoRepository<Company, Long> {

    /**
     * Find the company according to the name.
     * @param name a <code>String</code> with the company to retrieve users for.
     * @return a <code>Company</code> object representing the company info with this name. Returns null if no matching users.
     */
    Company findByName (@Param("name") final String name );

}
