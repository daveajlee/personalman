package de.davelee.personalman.server.rest.controllers;

import de.davelee.personalman.api.UserResponse;
import de.davelee.personalman.api.UsersResponse;
import de.davelee.personalman.server.model.User;
import de.davelee.personalman.server.services.UserService;
import de.davelee.personalman.server.utils.UserUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * This class defines the endpoints for the REST API which manipulate users and delegates the actions to the UserService class.
 * @author Dave Lee
 */
@RestController
@Api(value="/personalman/users")
@RequestMapping(value="/personalman/users")
public class UsersController {

    @Autowired
    private UserService userService;

    /**
     * Find all users for a specific company.
     * @param company a <code>String</code> containing the name of the company.
     * @param token a <code>String</code> containing the token to verify that the user is logged in.
     * @return a <code>ResponseEntity</code> containing the users for this company.
     */
    @ApiOperation(value = "Find all users for a company", notes="Find all users for a company to the system.")
    @GetMapping(value="/")
    @ApiResponses(value = {@ApiResponse(code=200,message="Successfully found user(s)"), @ApiResponse(code=204,message="Successful but no users found")})
    public ResponseEntity<UsersResponse> getUsers (@RequestParam("company") final String company,
                                                   @RequestParam("token") final String token) {
        //Verify that user is logged in.
        if ( token == null || !userService.checkAuthToken(token) ) {
            return ResponseEntity.status(403).build();
        }
        //First of all, check if the compny field is empty or null, then return bad request.
        if ( StringUtils.isBlank(company)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        //Now retrieve the user based on the username.
        List<User> users = userService.findByCompany(company);
        //If users is empty then return 204.
        if ( users.size() == 0 ) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        //Convert to UserResponse object and return 200.
        UserResponse[] userResponses = new UserResponse[users.size()];
        for ( int i = 0; i < users.size(); i++ ) {
            userResponses[i] = UserUtils.convertUserToUserResponse(users.get(i));
        }
        return ResponseEntity.ok(UsersResponse.builder()
                .count((long) userResponses.length)
                .userResponses(userResponses)
                .build());
    }

}
