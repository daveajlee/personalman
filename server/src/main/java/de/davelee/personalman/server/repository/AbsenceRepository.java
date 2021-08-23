package de.davelee.personalman.server.repository;

import de.davelee.personalman.server.model.Absence;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Interface class for database operations on absences - uses Spring Data Mongo.
 * @author Dave Lee
 */
public interface AbsenceRepository extends MongoRepository<Absence, Long> {

}
