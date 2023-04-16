package de.davelee.personalman.server.rest.controllers;

import de.davelee.personalman.api.*;
import de.davelee.personalman.server.model.User;
import de.davelee.personalman.server.model.UserAccountStatus;
import de.davelee.personalman.server.model.UserHistoryReason;
import de.davelee.personalman.server.services.UserService;
import de.davelee.personalman.server.utils.DateUtils;
import de.davelee.personalman.server.utils.UserUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * This class defines the endpoints for the REST API which manipulate users and delegates the actions to the UserService class.
 * @author Dave Lee
 */
@RestController
@Tag(name="/api/user")
@RequestMapping(value="/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * Add a user to the system.
     * @param userRequest a <code>UserRequest</code> object representing the user to add.
     * @return a <code>ResponseEntity</code> containing the result of the action.
     */
    @Operation(summary = "Add a user", description="Add a user to the system.")
    @PostMapping(value="/")
    @ApiResponses(value = {@ApiResponse(responseCode="201",description="Successfully created user")})
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
    @Operation(summary = "Find a user", description="Find a user to the system.")
    @GetMapping(value="/")
    @ApiResponses(value = {@ApiResponse(responseCode="200",description="Successfully found user"), @ApiResponse(responseCode="204",description="Successful but no user found")})
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
     * Find a user by name, date of birth and company.
     * @param name a <code>String</code> containing the name of the user to find.
     * @param dateOfBirth a <code>String</code> containing the date of the birth for the user to find.
     * @param company a <code>String</code> containing the company of the user to find.
     * @return a <code>ResponseEntity</code> object which contains the user found or bad request if the parameters are
     * invalid or an internal server error if the database is not available.
     */
    @Operation(summary = "Get user", description="Method to get a user's details by name and date of birth.")
    @GetMapping("/getUser")
    @ApiResponses(value = {@ApiResponse(responseCode="200",description="Successfully retrieved user details"), @ApiResponse(responseCode="500",description="Database not available")})
    public ResponseEntity<UserResponse> getUser( @RequestParam("name") final String name, @RequestParam("dateOfBirth") final String dateOfBirth,
                                                 @RequestParam("company") final String company, @RequestParam("token") final String token ) {
        //Check valid request including authentication
        if ( token == null || !userService.checkAuthToken(token) ) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        //If name or date of birth is null then bad request.
        if ( name == null || dateOfBirth == null || StringUtils.isBlank(company) ) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else {
            //Now retrieve the user based on the information provided.
            User user = userService.findUserByDateOfBirthAndNameAndCompany(LocalDate.parse(dateOfBirth), name.split(" ")[0], name.split(" ")[1], company);
            //If user is null then return 204.
            if ( user == null ) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            //Convert to UserResponse object and return 200.
            return ResponseEntity.ok(UserUtils.convertUserToUserResponse(user));
        }
    }

    /**
     * Delete a specific user from the database based on their username and company.
     * @param company a <code>String</code> containing the name of the company.
     * @param username a <code>String</code> containing the username.
     * @param token a <code>String</code> containing the token to verify that the user is logged in.
     * @return a <code>ResponseEntity</code> containing the results of the action.
     */
    @Operation(summary = "Delete a user", description="Delete a user from the system.")
    @DeleteMapping(value="/")
    @ApiResponses(value = {@ApiResponse(responseCode="200",description="Successfully delete user"), @ApiResponse(responseCode="204",description="Successful but no user found")})
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
     * Deactivate a specific user from the database based on their username and company.
     * @param deactivateUserRequest a <code>DeactivateUserRequest</code> object which contains the information on leaving.
     * @return a <code>ResponseEntity</code> containing the results of the action.
     */
    @Operation(summary = "Deactivate a user", description="Deactivate a user from the system.")
    @PatchMapping(value="/deactivate")
    @ApiResponses(value = {@ApiResponse(responseCode="200",description="Successfully deactivated user"), @ApiResponse(responseCode="204",description="Successful but no user found")})
    public ResponseEntity<DeactivateUserResponse> deactivateUser (@RequestBody final DeactivateUserRequest deactivateUserRequest) {
        //Check valid request including authentication
        HttpStatus status = validateAndAuthenticateRequest(deactivateUserRequest.getCompany(), deactivateUserRequest.getUsername(), deactivateUserRequest.getToken());
        //If the status is not null then produce response and return.
        if ( status != null ) {
            return new ResponseEntity<>(status);
        }
        //Now retrieve the user based on the username.
        User user = userService.findByCompanyAndUserName(deactivateUserRequest.getCompany(), deactivateUserRequest.getUsername());
        //If user is null then return 204.
        if ( user == null ) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        //Now deactivate the user based on the username and return the result.
        return ResponseEntity.ok(DeactivateUserResponse.builder()
                .leaveEntitlementForThisYear(userService.deactivate(user, DateUtils.convertDateToLocalDate(deactivateUserRequest.getLeavingDate()),
                        deactivateUserRequest.isResigned(), deactivateUserRequest.getReason()))
                .build());
    }

    /**
     * Update or set the salary information for a particular user. If the user already has salary information
     * then these will be overwritten.
     * @param updateSalaryRequest a <code>UpdateSalaryRequest</code> object containing the information to update.
     * @return a <code>ResponseEntity</code> containing the results of the action.
     */
    @Operation(summary = "Update salary information", description="Update salary information for a particular user.")
    @PatchMapping(value="/salary")
    @ApiResponses(value = {@ApiResponse(responseCode="200",description="Successfully updated salary information"), @ApiResponse(responseCode="204",description="No user found")})
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
        return userService.updateSalaryInformation(user, new BigDecimal(updateSalaryRequest.getHourlyWage()), updateSalaryRequest.getContractedHoursPerWeek() ) ?
            ResponseEntity.status(200).build() : ResponseEntity.status(500).build();
    }

    /**
     * Add a training course or qualification for this user. If the user already has a training list, the information
     * will be added otherwise a training list will be created.
     * @param addTrainingRequest a <code>AddTrainingRequest</code> object containing the information to update.
     * @return a <code>ResponseEntity</code> containing the results of the action.
     */
    @Operation(summary = "Add a training course", description="Add a training course for a particular user.")
    @PatchMapping(value="/training")
    @ApiResponses(value = {@ApiResponse(responseCode="200",description="Successfully added training course"), @ApiResponse(responseCode="204",description="No user found")})
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
    @Operation(summary = "Add a number of hours to the user's timesheet", description="Add a number of hours to a specified date for a specified user.")
    @PatchMapping(value="/timesheet")
    @ApiResponses(value = {@ApiResponse(responseCode="200",description="Successfully added hours"), @ApiResponse(responseCode="204",description="No user found")})
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
    @Operation(summary = "Retrieve the user's timesheet", description="Retrieve number of hours for a specified date (range) for a specified user.")
    @GetMapping(value="/timesheet")
    @ApiResponses(value = {@ApiResponse(responseCode="200",description="Successfully retrieved hours"), @ApiResponse(responseCode="204",description="No user found")})
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
    @Operation(summary="changePassword", description="Change password for a user")
    @PatchMapping(value="/password")
    @ApiResponses(@ApiResponse(responseCode="200",description="Successfully processed change password request"))
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
    @Operation(summary="Login", description="Login to the system")
    @PostMapping(value="/login")
    @ApiResponses(@ApiResponse(responseCode="200",description="Successfully processed login request"))
    public ResponseEntity<LoginResponse> login (@RequestBody final LoginRequest loginRequest) {
        User user = userService.findByCompanyAndUserName(loginRequest.getCompany(), loginRequest.getUsername());
        if ( user != null && user.getAccountStatus()== UserAccountStatus.ACTIVE && user.getPassword().contentEquals(loginRequest.getPassword()) ) {
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
    @Operation(summary="Logout", description="Logout from the system")
    @PostMapping(value="/logout")
    @ApiResponses(@ApiResponse(responseCode = "200",description="Successfully processed logout request"))
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
    @Operation(summary="resetUser", description="Reset password for a user")
    @PatchMapping(value="/reset")
    @ApiResponses(@ApiResponse(responseCode="200",description="Successfully processed reset user request"))
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
     * Add a new history entry to the list.
     * @param addHistoryEntryRequest a <code>AddHistoryEntryRequest</code> object containing the information to update.
     * @return a <code>ResponseEntity</code> containing the results of the action.
     */
    @Operation(summary = "Add a new history entry", description="Add a new history entry for a particular user.")
    @PatchMapping(value="/history")
    @ApiResponses(value = {@ApiResponse(responseCode="200",description="Successfully added history entry"), @ApiResponse(responseCode = "204",description="No user found")})
    public ResponseEntity<Void> addHistoryEntry (@RequestBody AddHistoryEntryRequest addHistoryEntryRequest) {
        //Check valid request including authentication
        HttpStatus status = validateAndAuthenticateRequest(addHistoryEntryRequest.getCompany(), addHistoryEntryRequest.getUsername(), addHistoryEntryRequest.getToken());
        //If the status is not null then produce response and return.
        if ( status != null ) {
            return new ResponseEntity<>(status);
        }
        //Now retrieve the user based on the username.
        User user = userService.findByCompanyAndUserName(addHistoryEntryRequest.getCompany(), addHistoryEntryRequest.getUsername());
        //If user is null then return 204.
        if ( user == null ) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        //Now add training course and return 200 or 500 depending on DB success.
        return userService.addUserHistoryEntry(user, DateUtils.convertDateToLocalDate(addHistoryEntryRequest.getDate()),
                UserHistoryReason.valueOf(addHistoryEntryRequest.getReason()), addHistoryEntryRequest.getComment()) ?
                ResponseEntity.status(200).build() : ResponseEntity.status(500).build();
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
