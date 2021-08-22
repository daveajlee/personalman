package de.davelee.personalman.server.model;

import lombok.*;

import java.io.Serializable;

/**
 * Class to represent companies in PersonalMan.
 * @author Dave Lee
 */
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
    private long id;

    /**
     * The name of the company.
     */
    private String name;

    /**
     * The default number of days of annual leave for this company.
     */
    private int defaultAnnualLeaveInDays;

    /**
     * The country in which this company is based.
     */
    private String country;

}
