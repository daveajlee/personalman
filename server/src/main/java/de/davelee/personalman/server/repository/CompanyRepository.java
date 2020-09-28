package de.davelee.personalman.server.repository;

import de.davelee.personalman.server.model.Company;
import org.springframework.data.repository.CrudRepository;

/**
 * Interface class for database operations on companies - uses Spring Data JPA.
 * @author Dave Lee
 */
public interface CompanyRepository extends CrudRepository<Company, Long> {
}
