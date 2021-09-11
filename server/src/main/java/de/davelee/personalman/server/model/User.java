package de.davelee.personalman.server.model;

import lombok.*;
import org.bson.types.ObjectId;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Class to represent users in PersonalMan.
 * @author Dave Lee
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class User {

    /**
     * A unique id for this user.
     */
    private ObjectId id;

    /**
     * The first name of this user.
     */
    private String firstName;

    /**
     * The surname of this user.
     */
    private String lastName;

    /**
     * The username for this user.
     */
    private String userName;

    /**
     * The password for this user.
     */
    private String password;

    /**
     * The company that the user works for.
     */
    private String company;

    /**
     * The leave entitlement of the person in days per year.
     */
    private int leaveEntitlementPerYear;

    /**
     * The names of the working days that the user is normally expected to work.
     */
    private List<DayOfWeek> workingDays;

    /**
     * The current job title of the user.
     */
    private String position;

    /**
     * The date that the person started at the company.
     */
    private LocalDate startDate;

    /**
     * The date that the person stopped working for the company.
     */
    private LocalDate endDate;

    /**
     * The status of this user's account.
     */
    private UserAccountStatus accountStatus;

    /**
     * The person's date of birth.
     */
    private LocalDate dateOfBirth;

    /**
     * The role that the user has in PersonalMan for this company.
     */
    private String role;

    /**
     * The hourly wage that the user is paid.
     */
    private BigDecimal hourlyWage;

    /**
     * The number of contracted hours that the user works per week.
     */
    private int contractedHoursPerWeek;

    /**
     * Any trainings or qualifications that the user has.
     */
    private List<String> trainingsList;

    /**
     * The number of hours that a user has worked on a particular day.
     */
    private Map<LocalDate, Integer> timesheet;

    /**
     * A log of entries representing the history of this user whilst working for this company.
     */
    private List<UserHistoryEntry> userHistoryEntryList;

    /**
     * Add a new training to the list.
     * @param trainingCourse a <code>String</code> containing the number of the training course or qualification.
     */
    public void addTrainingCourse ( final String trainingCourse ) {
        trainingsList.add(trainingCourse);
    }

    /**
     * Add a number of hours for a particular day to the timesheet.
     * @param hours a <code>int</code> with the number of hours to add.
     * @param date a <code>LocalDate</code> object containing the day to add the hours to.
     */
    public void addHoursForDate ( final int hours, final LocalDate date ) {
        //If the date already exists then add the hours to the hours already there.
        if ( timesheet.get(date) != null ) {
            timesheet.put(date, timesheet.get(date).intValue() + hours);
        } else {
            //If no hours are present then just add it as first entry.
            timesheet.put(date, hours);
        }
    }

    /**
     * Retrieve the number of hours that the user has worked on a particular day.
     * @param date a <code>LocalDate</code> object containing the day to retrieve hours for.
     * @return a <code>int</code> with the number of hours.
     */
    public int getHoursForDate ( final LocalDate date ) {
        //If the date is null then return 0.
        if ( timesheet.get(date) == null ) {
            return 0;
        }
        //Otherwise return the number of hours.
        return timesheet.get(date);
    }

    /**
     * Add a new history entry to the list.
     * @param date a <code>LocalDate</code> containing the date that the entry/event took place.
     * @param userHistoryReason a <code>UserHistoryReason</code> containing the reason that the entry/event took place.
     * @param comment a <code>String</code> containing the comment about the entry/event.
     */
    public void addUserHistoryEntry ( final LocalDate date, final UserHistoryReason userHistoryReason, final String comment ) {
        if ( userHistoryEntryList == null ) {
            userHistoryEntryList = new ArrayList<>();
        }
        userHistoryEntryList.add(UserHistoryEntry.builder()
                .date(date)
                .userHistoryReason(userHistoryReason)
                .comment(comment)
                .build());
    }
}
