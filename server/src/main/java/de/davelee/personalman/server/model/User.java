package de.davelee.personalman.server.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Setter;
import lombok.Builder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.persistence.Id;
import javax.persistence.GenerationType;
import javax.persistence.GeneratedValue;
import javax.persistence.ElementCollection;
import javax.persistence.FetchType;
import javax.persistence.Enumerated;
import javax.persistence.EnumType;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

/**
 * Class to represent users in PersonalMan.
 * @author Dave Lee
 */
@Entity
@Table( name = "USER", uniqueConstraints=@UniqueConstraint(columnNames = {"userName"}) )
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class User {

    /**
     * A unique id for this user.
     */
    @Id
    @GeneratedValue( strategy = GenerationType.AUTO )
    @Column( name = "ID" )
    private long id;

    /**
     * The first name of this user.
     */
    @Column ( name = "FIRSTNAME" )
    private String firstName;

    /**
     * The surname of this user.
     */
    @Column ( name = "LASTNAME" )
    private String lastName;

    /**
     * The username for this user.
     */
    @Column ( name = "USERNAME" )
    private String userName;

    /**
     * The password for this user.
     */
    @Column ( name = "PASSWORD" )
    private String password;

    /**
     * The company that the user works for.
     */
    @Column ( name = "COMPANY" )
    private String company;

    /**
     * The leave entitlement of the person in days per year.
     */
    @Column ( name = "LEAVEENTITLEMENT" )
    private int leaveEntitlementPerYear;

    /**
     * The names of the working days that the user is normally expected to work.
     */
    @Column ( name = "WORKINGDAYS" )
    @ElementCollection(fetch=FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    private List<DayOfWeek> workingDays;

    /**
     * The current job title of the user.
     */
    @Column ( name = "POSITION" )
    private String position;

    /**
     * The date that the person started at the company.
     */
    @Column ( name = "STARTDATE" )
    private LocalDate startDate;

}
