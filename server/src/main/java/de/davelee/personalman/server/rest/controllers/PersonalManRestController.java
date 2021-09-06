package de.davelee.personalman.server.rest.controllers;

import de.davelee.personalman.api.*;

import de.davelee.personalman.server.model.Company;
import de.davelee.personalman.server.model.User;
import de.davelee.personalman.server.services.CompanyService;
import de.davelee.personalman.server.services.UserService;
import de.davelee.personalman.server.utils.UserUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * This class defines the endpoints for the REST API and delegates the actions to either the AbsenceService or the UserService,
 * @author Dave Lee
 */
@RestController
@Api(value="/personalman")
@RequestMapping(value="/personalman")
public class PersonalManRestController {

    @Autowired
    private CompanyService companyService;

    @Autowired
    private UserService userService;

    /**
     * Add an absence to the database based on the supplied register company request.
     * @param registerCompanyRequest a <code>RegisterCompanyRequest</code> object representing the company to add.
     * @return a <code>ResponseEntity</code> containing the result of the action.
     */
    @ApiOperation(value = "Add a company", notes="Add a company to the system.")
    @PostMapping(value="/company")
    @ApiResponses(value = {@ApiResponse(code=201,message="Successfully created company")})
    public ResponseEntity<Void> addCompany (@RequestBody final RegisterCompanyRequest registerCompanyRequest ) {
        companyService.save(Company.builder()
                .id(new ObjectId())
                .name(registerCompanyRequest.getName())
                .defaultAnnualLeaveInDays(registerCompanyRequest.getDefaultAnnualLeaveInDays())
                .country(registerCompanyRequest.getCountry())
                .build());
        //Return 201 if saved successfully.
        return ResponseEntity.status(201).build();
    }

    /**
     * Get a specific company from the database based on their name.
     * @param name a <code>String</code> containing the name of the company.
     * @param token a <code>String</code> to verify if the user is logged in.
     * @return a <code>ResponseEntity</code> containing the results of the action.
     */
    @ApiOperation(value = "Retrieve a company", notes="Retrieve a company from the system.")
    @GetMapping(value="/company")
    @ApiResponses(value = {@ApiResponse(code=200,message="Successfully retrieved company"), @ApiResponse(code=404,message="Company not found")})
    @ResponseBody
    public ResponseEntity<CompanyResponse> getCompany (@RequestParam("name") final String name, @RequestParam("token") final String token ) {
        //Verify that user is logged in.
        if ( token == null || !userService.checkAuthToken(token) ) {
            return ResponseEntity.status(403).build();
        }
        //First of all, check if the name field is empty or null, then return bad request.
        if (StringUtils.isBlank(name) ) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Company company = companyService.getCompany(name);
        if ( company != null ) {
            return ResponseEntity.ok(CompanyResponse.builder()
                    .name(company.getName())
                    .defaultAnnualLeaveInDays(company.getDefaultAnnualLeaveInDays())
                    .country(company.getCountry())
                    .build());
        }
        //Otherwise 404 to indicate not found.
        return ResponseEntity.status(404).build();
    }

    /**
     * Delete a specific company from the database based on their name.
     * @param name a <code>String</code> containing the name of the company.
     * @param token a <code>String</code> to verify if the user is logged in.
     * @return a <code>ResponseEntity</code> containing the results of the action.
     */
    @ApiOperation(value = "Delete a company", notes="Delete a company from the system.")
    @DeleteMapping(value="/company")
    @ApiResponses(value = {@ApiResponse(code=200,message="Successfully delete company"), @ApiResponse(code=404,message="Company not found")})
    public ResponseEntity<Void> deleteCompany (@RequestParam("name") final String name, @RequestParam("token") final String token ) {
        //Verify that user is logged in.
        if ( token == null || !userService.checkAuthToken(token) ) {
            return ResponseEntity.status(403).build();
        }
        //First of all, check if the name field is empty or null, then return bad request.
        if (StringUtils.isBlank(name) ) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        //Now delete the company.
        if ( companyService.delete(name) ) {
            //Return 200 if successful delete.
            return ResponseEntity.status(200).build();
        } else {
            //Otherwise 404.
            return ResponseEntity.status(404).build();
        }
    }

    /**
     * Find all users for a specific company.
     * @param company a <code>String</code> containing the name of the company.
     * @param token a <code>String</code> containing the token to verify that the user is logged in.
     * @return a <code>ResponseEntity</code> containing the users for this company.
     */
    @ApiOperation(value = "Find all users for a company", notes="Find all users for a company to the system.")
    @GetMapping(value="/users")
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

    /**
     * Take a LoginRequest and attempt to login in the user. If successful, return a token which can be used for this session.
     * @param loginRequest a <code>LoginRequest</code> containing the company, username and password information for this request.
     * @return a <code>ResponseEntity</code> with response status 200 indicating that it was successful.
     */
    @ApiOperation(value="Login", notes="Login to the system")
    @PostMapping(value="/login")
    @ApiResponses(@ApiResponse(code=200,message="Successfully processed login request"))
    public ResponseEntity<LoginResponse> login (@RequestBody final LoginRequest loginRequest) {
        User user = userService.findByCompanyAndUserName(loginRequest.getCompany(), loginRequest.getUsername());
        if ( user != null && user.getPassword().contentEquals(loginRequest.getPassword()) ) {
            return ResponseEntity.ok().body(LoginResponse.builder().token(userService.generateAuthToken(loginRequest.getUsername())).build());
        } else if ( user != null ) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(LoginResponse.builder().errorMessage("Password was incorrect!").build());
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(LoginResponse.builder().errorMessage("User was not found").build());
    }

    /**
     * Take a token and attempt to log the user out. For security reasons, a 200 is returned even if logout was not successful.
     * @param token a <code>String</code> containing the token to remove.
     * @return a <code>LoginResponse</code> object which contains a token and response code 200 if login was successful or an error message and response code 403 if login was not successful.
     */
    @ApiOperation(value="Logout", notes="Logout from the system")
    @PostMapping(value="/logout")
    @ApiResponses(@ApiResponse(code=200,message="Successfully processed logout request"))
    public ResponseEntity<Void> logout (@RequestBody final String token) {
        //Remove the token from the authenticated tokens.
        userService.removeAuthToken(token);
        //Return 200.
        return ResponseEntity.status(200).build();
    }

    /**
     * Reset the password of the supplied user. Return 200 if the password was changed successfully or 404 if the user was not found.
     * @param resetUserRequest a <code>ResetUserRequest</code> object containing the company, username and new password.
     * @return a <code>ResponseEntity</code> object with status 200 if password changed or 404 if user not found.
     */
    @ApiOperation(value="resetUser", notes="Reset password for a user")
    @PostMapping(value="/resetUser")
    @ApiResponses(@ApiResponse(code=200,message="Successfully processed reset user request"))
    public ResponseEntity<Void> resetUser (@RequestBody final ResetUserRequest resetUserRequest) {
        //Verify that user is logged in.
        if ( resetUserRequest.getToken() == null || !userService.checkAuthToken(resetUserRequest.getToken()) ) {
            return ResponseEntity.status(403).build();
        }
        boolean result = userService.resetUserPassword(resetUserRequest.getCompany(), resetUserRequest.getUsername(), resetUserRequest.getPassword());
        //If result is true, then return 200 otherwise return 404 to indicate user not found.
        return result ? ResponseEntity.status(200).build() : ResponseEntity.status(404).build();
    }

}
