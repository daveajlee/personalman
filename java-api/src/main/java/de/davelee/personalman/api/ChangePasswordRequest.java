package de.davelee.personalman.api;

import lombok.*;

/**
 * This class is part of the PersonalMan REST API. It represents a request to change the password of a particular user.
 * @author Dave Lee
 */
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChangePasswordRequest {

    // company associated with
    private String company;

    // username who's password should be changed
    private String username;

    // The token of the user to verify that they are logged in
    private String token;

    // current password for this user
    private String currentPassword;

    // new password to set for this user
    private String newPassword;

}
