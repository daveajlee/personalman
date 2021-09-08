package de.davelee.personalman.server.rest.controllers;

import de.davelee.personalman.api.*;
import de.davelee.personalman.server.model.User;
import de.davelee.personalman.server.services.UserService;
import de.davelee.personalman.server.utils.DateUtils;
import de.davelee.personalman.server.utils.UserUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

/**
 * This class defines the endpoints for the REST API which manipulate users and delegates the actions to the UserService class.
 * @author Dave Lee
 */
@RestController
@Api(value="/personalman/user")
@RequestMapping(value="/personalman/user")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * Add a user to the system.
     * @param userRequest a <code>UserRequest</code> object representing the user to add.
     * @return a <code>ResponseEntity</code> containing the result of the action.
     */
    @ApiOperation(value = "Add a user", notes="Add a user to the system.")
    @PostMapping(value="/")
    @ApiResponses(value = {@ApiResponse(code=201,message="Successfully created user")})
    public ResponseEntity<Void> addUser (@RequestBody final UserRequest userRequest ) {
        //First of all, check if any of the fields are empty or null, then return bad request.
        if (StringUtils.isBlank(userRequest.getFirstName()) || StringUtils.isBlank(userRequest.getSurname())
                || StringUtils.isBlank(userRequest.getPosition()) || StringUtils.isBlank(userRequest.getStartDate())
                || StringUtils.isBlank(userRequest.getUsername()) || StringUtils.isBlank(userRequest.getWorkingDays())
                || userRequest.getLeaveEntitlementPerYear() <= 0 || StringUtils.isBlank(userRequest.getCompany())) {
            return ResponseEntity.badRequest().build();
        }
        //Now convert the dates to LocalDate. If end date is before start date then return bad request.
        LocalDate startLocalDate = DateUtils.convertDateToLocalDate(userRequest.getStartDate());
        if ( startLocalDate == null ) {
            return ResponseEntity.badRequest().build();
        }
        //Now convert to user object.
        User user = UserUtils.convertUserRequestToUser(userRequest, startLocalDate);
        //Return 201 if saved successfully.
        return userService.save(user) ? ResponseEntity.status(201).build() : ResponseEntity.status(500).build();
    }

    /**
     * Find a user based on their username and company.
     * @param company a <code>String</code> containing the name of the company.
     * @param username a <code>String</code> containing the username.
     * @param token a <code>String</code> containing the token to verify that the user is logged in.
     * @return a <code>ResponseEntity</code> containing the user found.
     */
    @ApiOperation(value = "Find a user", notes="Find a user to the system.")
    @GetMapping(value="/")
    @ApiResponses(value = {@ApiResponse(code=200,message="Successfully found user"), @ApiResponse(code=204,message="Successful but no user found")})
    public ResponseEntity<UserResponse> getUser (@RequestParam("company") final String company, @RequestParam("username") final String username,
                                                 @RequestParam("token") final String token) {
        //Check valid request including authentication
        HttpStatus status = validateAndAuthenticateRequest(company, username, token);
        //If the status is not null then produce response and return.
        if ( status != null ) {
            return new ResponseEntity<>(status);
        }
        //Now retrieve the user based on the username.
        User user = userService.findByCompanyAndUserName(company, username);
        //If user is null then return 204.
        if ( user == null ) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        //Convert to UserResponse object and return 200.
        return ResponseEntity.ok(UserUtils.convertUserToUserResponse(user));
    }


    /**
     * Delete a specific user from the database based on their username and company.
     * @param company a <code>String</code> containing the name of the company.
     * @param username a <code>String</code> containing the username.
     * @param token a <code>String</code> containing the token to verify that the user is logged in.
     * @return a <code>ResponseEntity</code> containing the results of the action.
     */
    @ApiOperation(value = "Delete a user", notes="Delete a user from the system.")
    @DeleteMapping(value="/")
    @ApiResponses(value = {@ApiResponse(code=200,message="Successfully delete user"), @ApiResponse(code=204,message="Successful but no user found")})
    public ResponseEntity<Void> deleteUser (@RequestParam("company") final String company, @RequestParam("username") final String username,
                                            @RequestParam("token") final String token) {
        //Check valid request including authentication
        HttpStatus status = validateAndAuthenticateRequest(company, username, token);
        //If the status is not null then produce response and return.
        if ( status != null ) {
            return new ResponseEntity<>(status);
        }
        //Now retrieve the user based on the username.
        User user = userService.findByCompanyAndUserName(company, username);
        //If user is null then return 204.
        if ( user == null ) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        //Now delete the user based on the username.
        userService.delete(user);
        //Return 200.
        return ResponseEntity.status(200).build();
    }

    /**
     * Update or set the salary information for a particular user. If the user already has salary information
     * then these will be overwritten.
     * @param updateSalaryRequest a <code>UpdateSalaryRequest</code> object containing the information to update.
     * @return a <code>ResponseEntity</code> containing the results of the action.
     */
    @ApiOperation(value = "Update salary information", notes="Update salary information for a particular user.")
    @PutMapping(value="/salary")
    @ApiResponses(value = {@ApiResponse(code=200,message="Successfully updated salary information"), @ApiResponse(code=204,message="No user found")})
    public ResponseEntity<Void> updateSalaryInformation (@RequestBody UpdateSalaryRequest updateSalaryRequest) {
        //Check valid request including authentication
        HttpStatus status = validateAndAuthenticateRequest(updateSalaryRequest.getCompany(), updateSalaryRequest.getUsername(), updateSalaryRequest.getToken());
        //If the status is not null then produce response and return.
        if ( status != null ) {
            return new ResponseEntity<>(status);
        }
        //Now retrieve the user based on the username.
        User user = userService.findByCompanyAndUserName(updateSalaryRequest.getCompany(), updateSalaryRequest.getUsername());
        //If user is null then return 204.
        if ( user == null ) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        //Now update salary information and return 200 or 500 depending on DB success.
        return userService.updateSalaryInformation(user, updateSalaryRequest.getHourlyWage(), updateSalaryRequest.getContractedHoursPerWeek() ) ?
            ResponseEntity.status(200).build() : ResponseEntity.status(500).build();
    }

    /**
     * Add a training course or qualification for this user. If the user already has a training list, the information
     * will be added otherwise a training list will be created.
     * @param addTrainingRequest a <code>AddTrainingRequest</code> object containing the information to update.
     * @return a <code>ResponseEntity</code> containing the results of the action.
     */
    @ApiOperation(value = "Add a training course", notes="Add a training course for a particular user.")
    @PutMapping(value="/training")
    @ApiResponses(value = {@ApiResponse(code=200,message="Successfully added training course"), @ApiResponse(code=204,message="No user found")})
    public ResponseEntity<Void> addTraining (@RequestBody AddTrainingRequest addTrainingRequest) {
        //Check valid request including authentication
        HttpStatus status = validateAndAuthenticateRequest(addTrainingRequest.getCompany(), addTrainingRequest.getUsername(), addTrainingRequest.getToken());
        //If the status is not null then produce response and return.
        if ( status != null ) {
            return new ResponseEntity<>(status);
        }
        //Now retrieve the user based on the username.
        User user = userService.findByCompanyAndUserName(addTrainingRequest.getCompany(), addTrainingRequest.getUsername());
        //If user is null then return 204.
        if ( user == null ) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        //Now add training course and return 200 or 500 depending on DB success.
        return userService.addTrainingCourse(user, addTrainingRequest.getTrainingCourse()) ?
                ResponseEntity.status(200).build() : ResponseEntity.status(500).build();
    }

    /**
     * Add a number of hours for this user for the particular date. If the user already has hours for this
     * particular date, the hours will be increased by the supplied amount.
     * @param addTimeSheetHoursRequest a <code>AddTimeSheetHoursRequest</code> object containing the information to update.
     * @return a <code>ResponseEntity</code> containing the results of the action.
     */
    @ApiOperation(value = "Add a number of hours to the user's timesheet", notes="Add a number of hours to a specified date for a specified user.")
    @PutMapping(value="/timesheet")
    @ApiResponses(value = {@ApiResponse(code=200,message="Successfully added hours"), @ApiResponse(code=204,message="No user found")})
    public ResponseEntity<Void> addHoursForDate (@RequestBody AddTimeSheetHoursRequest addTimeSheetHoursRequest) {
        //Check valid request including authentication
        HttpStatus status = validateAndAuthenticateRequest(addTimeSheetHoursRequest.getCompany(), addTimeSheetHoursRequest.getUsername(), addTimeSheetHoursRequest.getToken());
        //If the status is not null then produce response and return.
        if ( status != null ) {
            return new ResponseEntity<>(status);
        }
        //Now retrieve the user based on the username.
        User user = userService.findByCompanyAndUserName(addTimeSheetHoursRequest.getCompany(), addTimeSheetHoursRequest.getUsername());
        //If user is null then return 204.
        if ( user == null ) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        //Now add the hours and return 200 or 500 depending on DB success.
        return userService.addHoursForDate(user, addTimeSheetHoursRequest.getHours(), DateUtils.convertDateToLocalDate(addTimeSheetHoursRequest.getDate())) ?
                ResponseEntity.status(200).build() : ResponseEntity.status(500).build();
    }

    /**
     * Retrieve the number of hours for this user for the particular date or date range.
     * @param company a <code>String</code> containing the name of the company.
     * @param username a <code>String</code> containing the username.
     * @param token a <code>String</code> containing the token to verify that the user is logged in.
     * @param startDate a <code>String</code> containing the start date to retrieve hours for in format dd-MM-yyyy.
     * @param endDate a <code>String</code> containing the end date to retrieve hours for in format dd-MM-yyyy.
     * @return a <code>ResponseEntity</code> containing the results of the action.
     */
    @ApiOperation(value = "Retrieve the user's timesheet", notes="Retrieve number of hours for a specified date (range) for a specified user.")
    @GetMapping(value="/timesheet")
    @ApiResponses(value = {@ApiResponse(code=200,message="Successfully retrieved hours"), @ApiResponse(code=204,message="No user found")})
    public ResponseEntity<Integer> getHoursForDate (@RequestParam("company") final String company, @RequestParam("username") final String username,
                                                    @RequestParam("token") final String token, @RequestParam("startDate") final String startDate,
                                                    @RequestParam("endDate") final String endDate ) {
        //Check valid request including authentication
        HttpStatus status = validateAndAuthenticateRequest(company, username, token);
        //If the status is not null then produce response and return.
        if ( status != null ) {
            return new ResponseEntity<>(status);
        }
        //Now retrieve the user based on the username.
        User user = userService.findByCompanyAndUserName(company, username);
        //If user is null then return 204.
        if ( user == null ) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        //Convert dates to LocalDates.
        LocalDate localStartDate = DateUtils.convertDateToLocalDate(startDate);
        LocalDate localEndDate = DateUtils.convertDateToLocalDate(endDate);
        //Perform either date range or single date.
        if ( localStartDate.isEqual(localEndDate) ) {
            return ResponseEntity.ok(userService.getHoursForDate(user, localStartDate));
        } else {
            return ResponseEntity.ok(userService.getHoursForDateRange(user, localStartDate, localEndDate));
        }
    }

    /**
     * Change the password of the supplied user. Return 200 if the password was changed successfully or 404 if the user was not found
     * or the password supplied did not match the current password of the user.
     * @param changePasswordRequest a <code>ChangePasswordRequest</code> object containing the company, username, old password and new password.
     * @return a <code>ResponseEntity</code> object with status 200 if password changed or 404 if user not found.
     */
    @ApiOperation(value="changePassword", notes="Change password for a user")
    @PutMapping(value="/password")
    @ApiResponses(@ApiResponse(code=200,message="Successfully processed change password request"))
    public ResponseEntity<Void> changePassword (@RequestBody final ChangePasswordRequest changePasswordRequest) {
        //Check valid request including authentication
        HttpStatus status = validateAndAuthenticateRequest(changePasswordRequest.getCompany(), changePasswordRequest.getUsername(), changePasswordRequest.getToken());
        //If the status is not null then produce response and return.
        if ( status != null ) {
            return new ResponseEntity<>(status);
        }
        boolean result = userService.changePassword(changePasswordRequest.getCompany(), changePasswordRequest.getUsername(),
                changePasswordRequest.getCurrentPassword(), changePasswordRequest.getNewPassword());
        //If result is true, then return 200 otherwise return 404 to indicate user not found.
        return result ? ResponseEntity.status(200).build() : ResponseEntity.status(404).build();
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
     * @param logoutRequest a <code>LogoutRequest</code> containing the token to remove.
     * @return a <code>LoginResponse</code> object which contains a token and response code 200 if login was successful or an error message and response code 403 if login was not successful.
     */
    @ApiOperation(value="Logout", notes="Logout from the system")
    @PostMapping(value="/logout")
    @ApiResponses(@ApiResponse(code=200,message="Successfully processed logout request"))
    public ResponseEntity<Void> logout (@RequestBody final LogoutRequest logoutRequest) {
        //Remove the token from the authenticated tokens.
        userService.removeAuthToken(logoutRequest.getToken());
        //Return 200.
        return ResponseEntity.status(200).build();
    }

    /**
     * Reset the password of the supplied user. Return 200 if the password was changed successfully or 404 if the user was not found.
     * @param resetUserRequest a <code>ResetUserRequest</code> object containing the company, username and new password.
     * @return a <code>ResponseEntity</code> object with status 200 if password changed or 404 if user not found.
     */
    @ApiOperation(value="resetUser", notes="Reset password for a user")
    @PostMapping(value="/reset")
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

    /**
     * Private helper method to verify that at least username, company and token are all supplied and valid.
     * @param company a <code>String</code> containing the name of the company.
     * @param username a <code>String</code> containing the username.
     * @param token a <code>String</code> containing the token to verify that the user is logged in.
     * @return a <code>HttpStatus</code> which is either filled if it was not authenticated or null if authenticated and valid.
     */
    private HttpStatus validateAndAuthenticateRequest ( final String company, final String username, final String token ) {
        //First of all, check if the username field is empty or null, then return bad request.
        if (StringUtils.isBlank(username) || StringUtils.isBlank(company)) {
            return HttpStatus.BAD_REQUEST;
        }
        //Verify that user is logged in.
        if ( token == null || !userService.checkAuthToken(token) ) {
            return HttpStatus.FORBIDDEN;
        }
        //If everything was ok then return null.
        return null;
    }

}
