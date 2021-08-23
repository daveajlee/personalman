package de.davelee.personalman.server.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Setter;
import lombok.Builder;
import org.bson.types.ObjectId;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

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
     * The role that the user has in PersonalMan for this company.
     */
    private String role;

}
