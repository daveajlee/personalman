package de.davelee.personalman.api;

import lombok.*;

/**
 * This class is part of the PersonalMan REST API. It represents a response to deactivate a particular user.
 * The amount of days that the user may take for annual leave this year.
 * @author Dave Lee
 */
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DeactivateUserResponse {

    //leave entitlement for this year
    private int leaveEntitlementForThisYear;

}
