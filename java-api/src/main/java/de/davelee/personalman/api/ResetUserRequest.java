package de.davelee.personalman.api;

import lombok.*;

/**
 * This class is part of the PersonalMan REST API. It represents a request to reset the password of a particular user.
 * @author Dave Lee
 */
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResetUserRequest {

    // company associated with
    private String company;

    // username who's password should be reset
    private String username;

    // new password to set for this user
    private String password;

    // The token of the user to verify that they are logged in
    private String token;

}
