package de.davelee.personalman.server.model;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Class to represent companies in PersonalMan.
 * @author Dave Lee
 */
@Entity
@Table( name = "COMPANY", uniqueConstraints=@UniqueConstraint(columnNames = {"name"})  )
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ToString
public class Company implements Serializable {

    /**
     * A unique id for this absence.
     */
    @Id
    @GeneratedValue( strategy = GenerationType.AUTO )
    @Column ( name = "ID" )
    private long id;

    /**
     * The name of the company.
     */
    @Column ( name = "NAME" )
    private String name;

    /**
     * The default number of days of annual leave for this company.
     */
    @Column ( name = "ANNUAL_LEAVE")
    private int defaultAnnualLeaveInDays;

    /**
     * The country in which this company is based.
     */
    @Column ( name = "COUNTRY")
    private String country;

}
