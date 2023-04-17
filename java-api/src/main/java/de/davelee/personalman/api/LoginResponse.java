package de.davelee.personalman.api;

import lombok.*;

/**
 * This class is part of the PersonalMan REST API. It represents the response to a login request with either a token
 * if login was successful or error message if login was not successful.
 * @author Dave Lee
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class LoginResponse {

    //The error message to show the user if the login was not successful which can be null if login was successful.
    private String errorMessage;

    //The authentication token which can be null if the login was not successful.
    private String token;

}
