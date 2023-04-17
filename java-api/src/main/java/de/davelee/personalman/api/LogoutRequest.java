package de.davelee.personalman.api;

import lombok.*;

/**
 * This class is part of the PersonalMan REST API. It represents a request for a logout with a token to invalidate.
 * @author Dave Lee
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class LogoutRequest {

    //The token to invalidate
    private String token;

}
