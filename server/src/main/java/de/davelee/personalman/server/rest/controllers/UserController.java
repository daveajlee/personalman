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
        userService.save(user);
        //Return 201 if saved successfully.
        return ResponseEntity.status(201).build();
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
        //Now update salary information.
        userService.updateSalaryInformation(user, updateSalaryRequest.getHourlyWage(), updateSalaryRequest.getContractedHoursPerWeek() );
        //Return 200.
        return ResponseEntity.status(200).build();
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
        //Now add training course.
        userService.addTrainingCourse(user, addTrainingRequest.getTrainingCourse());
        //Return 200.
        return ResponseEntity.status(200).build();
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
        //Now add the hours.
        userService.addHoursForDate(user, addTimeSheetHoursRequest.getHours(), DateUtils.convertDateToLocalDate(addTimeSheetHoursRequest.getDate()));
        //Return 200.
        return ResponseEntity.status(200).build();
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
