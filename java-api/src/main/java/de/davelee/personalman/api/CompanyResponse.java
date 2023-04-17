package de.davelee.personalman.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

/**
 * This class is part of the PersonalMan REST API. It represents a response from the server for a particular company
 * containing name, default leave they are entitled to per year and the country that the company is based in.
 * @author Dave Lee
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CompanyResponse {

    //name
    private String name;

    //default leave entitlement per year
    private int defaultAnnualLeaveInDays;

    //base country
    private String country;

}
