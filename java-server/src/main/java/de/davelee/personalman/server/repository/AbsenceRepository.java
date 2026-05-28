package de.davelee.personalman.server.repository;

import de.davelee.personalman.server.model.Absence;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Interface class for database operations on absences - uses Spring Data Mongo.
 * @author Dave Lee
 */
public interface AbsenceRepository extends MongoRepository<Absence, Long> {

    /**
     * Find all absences for a particular company.
     * @param company a <code>String</code> with the company to retrieve absences for.
     * @return a <code>List</code> of <code>Absence</code> objects representing all absences belonging to this company.
     * Returns null if no matching absences.
     */
    List<Absence> findByCompany(@Param("company") final String company);

}
