package de.davelee.personalman.server.services;

import de.davelee.personalman.api.AbsencesResponse;
import de.davelee.personalman.server.model.Absence;
import de.davelee.personalman.server.model.AbsenceCategory;
import de.davelee.personalman.server.model.User;
import de.davelee.personalman.server.repository.AbsenceRepository;
import de.davelee.personalman.server.utils.AbsenceUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Class to provide service operations for absences in the PersonalMan program.
 * @author Dave Lee
 */
@Service
public class AbsenceService {

    @Autowired
    private AbsenceRepository absenceRepository;

    @Autowired
    private UserService userService;

    private final static Logger LOG = LoggerFactory.getLogger(AbsenceService.class);

    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     * Save the specified absence object in the database.
     * @param absence a <code>Absence</code> object to save in the database.
     * @return a <code>boolean</code> which is true iff the absence has been validated and saved successfully.
     */
    public boolean save ( final Absence absence ) {
        //Store result.
        boolean result = true;
        //Get the employee information.
        User user = userService.findByCompanyAndUserName(absence.getCompany(), absence.getUsername());
        List<Absence> absences = new ArrayList<>();
        //Special processing for particular types of categories.
        if ( absence.getCategory() == AbsenceCategory.HOLIDAY ) {
            if ( absence.getStartDate().getYear() != absence.getEndDate().getYear() &&
                    ChronoUnit.YEARS.between(absence.getStartDate(), absence.getEndDate()) < 2 ) {
                //Generate absences according to free days excluding these from the actual absences - just for the start year.
                absences.addAll(AbsenceUtils.generateAbsences(user, absence.getStartDate(),
                        LocalDate.of(absence.getStartDate().getYear(),12,31), absence.getCategory()));
                result = controlAbsencesForYear ( absence.getCompany(), absence.getUsername(),
                        absence.getStartDate().getYear(), absence.getCategory(), user, absences );
                if ( result ) {
                    List<Absence> absences2 = AbsenceUtils.generateAbsences(user,
                            LocalDate.of(absence.getEndDate().getYear(),1,1), absence.getEndDate(), absence.getCategory());
                    result = controlAbsencesForYear ( absence.getCompany(), absence.getUsername(),
                            absence.getEndDate().getYear(), absence.getCategory(), user, absences2 );
                    absences.addAll(absences2);
                }
            } else if ( absence.getStartDate().getYear() == absence.getEndDate().getYear() ) {
                absences.addAll(AbsenceUtils.generateAbsences(user, absence.getStartDate(), absence.getEndDate(), absence.getCategory()));
                result = controlAbsencesForYear ( absence.getCompany(), absence.getUsername(),
                        absence.getStartDate().getYear(), absence.getCategory(), user, absences );
            } else {
                //Holiday absences of more than one year are automatically rejected because annual leave will be exhausted.
                result = false;
            }
        } else if ( absence.getCategory()==AbsenceCategory.CONFERENCE || absence.getCategory()==AbsenceCategory.TRIP ) {
            //We save the absence for the whole length.
            Absence newAbsence = new Absence();
            newAbsence.setCategory(absence.getCategory());
            newAbsence.setCompany(absence.getCompany());
            newAbsence.setUsername(absence.getUsername());
            newAbsence.setStartDate(absence.getStartDate());
            newAbsence.setEndDate(absence.getEndDate());
            absences.add(newAbsence);
            //We then add days in lieu if applicable.
            absences.addAll(AbsenceUtils.generateDaysInLieu(user, absence.getStartDate(), absence.getEndDate()));
        } else if ( absence.getCategory()==AbsenceCategory.DAY_IN_LIEU ) {
            //Days In Lieu only bookable within same year.
            if ( absence.getStartDate().getYear()!=absence.getEndDate().getYear()) {
                result = false;
            } else {
                long numDayInLieuDaysRequests = countAbsences(absence.getCompany(), absence.getUsername(),
                        LocalDate.of(absence.getStartDate().getYear(),1,1), LocalDate.of(absence.getStartDate().getYear(),12,31),
                        AbsenceCategory.DAY_IN_LIEU_REQUEST);
                long numDayInLieuDays = countAbsences(absence.getCompany(), absence.getUsername(),
                        LocalDate.of(absence.getStartDate().getYear(),1,1), LocalDate.of(absence.getStartDate().getYear(),12,31),
                        AbsenceCategory.DAY_IN_LIEU);
                long numDayInLieuDaysAvailable = numDayInLieuDaysRequests - numDayInLieuDays;
                long numDaysDesired = Period.between(absence.getStartDate(), absence.getEndDate()).getDays() + 1;
                result = numDaysDesired <= numDayInLieuDaysAvailable;
                absences.addAll(AbsenceUtils.generateAbsences(user, absence.getStartDate(),
                        absence.getEndDate(), absence.getCategory()));
            }
        } else {
            // No special processing needed so just add the absence to the list.
            absences.addAll(AbsenceUtils.generateAbsences(user, absence.getStartDate(),
                    absence.getEndDate(), absence.getCategory()));
        }
        if ( result ) {
            for ( Absence myAbsence : absences ) {
                absenceRepository.save(myAbsence);
            }
        }
        return result;
    }

    /**
     * Calculate the name of absences per year for a particular reason based on current absences and the planned absence list.
     * @param company a <code>String</code> with the company that the user is associated with.
     * @param employeeName a <code>String</code> containing the name of the employee.
     * @param year a <code>String</code> with the year to perform the calculation for.
     * @param category a <code>AbsenceCategory</code> enum with the category for absence.
     * @param user a <code>User</code> object representing the user taking absence.
     * @param absences a <code>List</code> of <code>Absence/code> objects representing planned absences.
     * @return a <code>boolean</code> which is true iff the planned absences can be taken without exhausting all annual leave for the supplied year.
     */
    private boolean controlAbsencesForYear ( final String company, final String employeeName, final int year, final AbsenceCategory category, final User user, final List<Absence> absences ) {
        try {
            long numAnnualLeave = countAbsences(company, employeeName, LocalDate.of(year,1,1), LocalDate.of(year,12,31), category);
            numAnnualLeave += AbsenceUtils.countAbsencesInDays(absences);
            return numAnnualLeave <= user.getLeaveEntitlementPerYear();
        } catch ( NumberFormatException nfe ) {
            LOG.error("Could not convert year " + year + " to a number as it is not a valid number!");
            return false;
        }
    }

    /**
     * Find absences taking place within the specified date range.
     * @param company a <code>String</code> with the company to retrieve absences for.
     * @param username a <code>String</code> with the username to retrieve absences for.
     * @param startDate a <code>LocalDate</code> with the specified start date (inclusive).
     * @param endDate a <code>LocalDate</code> with the specified start date (inclusive).
     * @return a <code>List</code> of <code>Absence</code> objects containing all absences for the specified date.
     */
    public List<Absence> findAbsences ( final String company, final String username, final LocalDate startDate,
                                        final LocalDate endDate ) {
        //Call the appropriate DB method depending on whether a specified username is supplied.
        Query query = new Query();
        if ( username == null ) {
            query.addCriteria(Criteria.where("company").is(company).and("startDate").gte(startDate).and("endDate").lte(endDate));
            return mongoTemplate.find(query, Absence.class);
        }
        query.addCriteria(Criteria.where("company").is(company).and("username").is(username).and("startDate").gte(startDate).and("endDate").lte(endDate));
        return mongoTemplate.find(query, Absence.class);
    }

    /**
     * Count absences taking place within the specified date range.
     * @param company a <code>String</code> with the company to retrieve absences for.
     * @param username a <code>String</code> with the username to retrieve absences for.
     * @param startDate a <code>LocalDate</code> with the specified start date (inclusive).
     * @param endDate a <code>LocalDate</code> with the specified start date (inclusive).
     * @param absenceCategory a <code>AbsenceCategory</code> object representing the category of absences which should be retrieved.
     * @return a <code>Long</code> object containing the count of absences for the specified date.
     */
    public Long countAbsences ( final String company, final String username, final LocalDate startDate,
                                final LocalDate endDate, final AbsenceCategory absenceCategory) {
        long count = 0;
        List<Absence> matchingAbsences = findAbsences (company, username, startDate, endDate);
        for ( Absence matchingAbsence : matchingAbsences ) {
            if ( matchingAbsence.getCategory() == absenceCategory ) {
                count = Period.between(matchingAbsence.getStartDate(), matchingAbsence.getEndDate()).getDays() + 1;
            }
        }
        return count;
    }

    /**
     * Delete absences taking place within the specified date range.
     * @param company a <code>String</code> with the company to retrieve absences for.
     * @param username a <code>String</code> with the username to retrieve absences for.
     * @param startDate a <code>LocalDate</code> with the specified start date (inclusive).
     * @param endDate a <code>LocalDate</code> with the specified start date (inclusive).
     */
    public void delete ( final String company, final String username, final LocalDate startDate,
                         final LocalDate endDate ) {
        List<Absence> absencesToDelete = findAbsences(company, username, startDate, endDate);
        absencesToDelete.forEach(absenceRepository::delete);
    }

    /**
     * Helper method to prepare AbsencesResponse.
     * @return a <code>AbsencesResponse</code> object containing the basic statistics map to.
     */
    public AbsencesResponse prepareAbsencesResponse ( ) {
        HashMap<String, Integer> statisticsMap = new HashMap<>();
        AbsenceCategory[] absenceCategories = AbsenceCategory.values();
        for ( AbsenceCategory absenceCategory : absenceCategories ) {
            statisticsMap.put(absenceCategory.toString(), 0);
        }
        return AbsencesResponse.builder()
                .statisticsMap(statisticsMap)
                .build();
    }

}
