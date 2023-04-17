package de.davelee.personalman.api;

import lombok.*;

/**
 * This class is part of the PersonalMan REST API. It represents a request to deactivate a particular user.
 * @author Dave Lee
 */
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DeactivateUserRequest {

    // company associated with
    private String company;

    // username who's password should be changed
    private String username;

    // The token of the user to verify that they are logged in
    private String token;

    // did the user resign or were they sacked
    private boolean resigned;

    // leaving date in the format dd-MM-yyyy
    private String leavingDate;

    // reason for leaving
    private String reason;

}
