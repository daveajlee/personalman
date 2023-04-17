package de.davelee.personalman.api;

import lombok.*;

/**
 * This class is part of the PersonalMan REST API. It represents a request for a login with username and password.
 * @author Dave Lee
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class LoginRequest {

    //The company that the user is using to login.
    private String company;

    //The username who wants to login
    private String username;

    //The password used for login
    private String password;

}
