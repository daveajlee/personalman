package de.davelee.personalman.server.repository;

import de.davelee.personalman.server.model.Absence;
import de.davelee.personalman.server.model.AbsenceCategory;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDate;
import java.util.List;

/**
 * Interface class for database operations on absences - uses Spring Data JPA.
 * @author Dave Lee
 */
public interface AbsenceRepository extends CrudRepository<Absence, Long> {

    /**
     * Find all absences for the specified date range in the specified company.
     * @param company a <code>String</code> with the company to retrieve absences for.
     * @param startDate a <code>LocalDate</code> with the requested start date for absences (inclusive).
     * @param endDate a <code>LocalDate</code> with the requested end date for absences (inclusive).
     * @return a <code>List</code> of <code>Absence</code> objects containing all absences for the specified date range.
     */
    @Query("select a from Absence a " +
            "where a.company = ?1 and a.startDate >= ?2 and a.startDate <= ?3 and a.endDate >= ?2 and a.endDate <= ?3")
    List<Absence> findByCompanyAndDate(final String company, final LocalDate startDate, final LocalDate endDate);

    /**
     * Find all absences for the specified user name between the startDate and endDate (both dates in the range are inclusive)
     * @param company a <code>String</code> with the company to retrieve absences for.
     * @param username a <code>String</code> with the desired user name whose absences should be retrieved.
     * @param startDate a <code>LocalDate</code> with the start date of the range (inclusive).
     * @param endDate a <code>LocalDate</code> with the end date of the range (inclusive)
     * @return a <code>List</code> of <code>Absence</code> objects containing all absences for the specified date range.
     */
    @Query("select a from Absence a " +
            "where a.company = ?1 and a.username = ?2 and a.startDate >= ?3 and a.startDate <= ?4 and a.endDate >= ?3 and a.endDate <= ?4")
    List<Absence> findByCompanyAndNameAndDate(final String company, final String username, final LocalDate startDate, final LocalDate endDate);

    /**
     * Count all absences for the specified user name between the startDate and endDate (both dates in the range are inclusive) and a specified category.
     * @param company a <code>String</code> with the company to retrieve absences for.
     * @param username a <code>String</code> with the desired user name whose absences should be retrieved.
     * @param startDate a <code>LocalDate</code> with the start date of the range (inclusive).
     * @param endDate a <code>LocalDate</code> with the end date of the range (inclusive)
     * @param absenceCategory a <code>AbsenceCategory</code> object representing the category of absences which should be retrieved.
     * @return a <code>Long</code> object containing the count of  all absences for the specified date range and matching the specified category.
     */
    @Query("select count(a) from Absence a " +
            "where a.company = ?1 and a.username = ?2 and a.startDate >= ?3 and a.startDate <= ?4 and a.endDate >= ?3 and a.endDate <= ?4 and a.category = ?5")
    Long countByCompanyAndNameAndDate(final String company, final String username, final LocalDate startDate, final LocalDate endDate, final AbsenceCategory absenceCategory);

}
