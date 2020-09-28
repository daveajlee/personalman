package de.davelee.personalman.server.rest.controllers;

import de.davelee.personalman.api.*;
import de.davelee.personalman.server.model.Absence;
import de.davelee.personalman.server.model.AbsenceCategory;
import de.davelee.personalman.server.model.Company;
import de.davelee.personalman.server.model.User;
import de.davelee.personalman.server.services.AbsenceService;
import de.davelee.personalman.server.services.CompanyService;
import de.davelee.personalman.server.services.UserService;
import de.davelee.personalman.server.utils.AbsenceUtils;
import de.davelee.personalman.server.utils.DateUtils;
import de.davelee.personalman.server.utils.UserUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.h2.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;

/**
 * This class defines the endpoints for the REST API and delegates the actions to either the AbsenceService or the UserService,
 * @author Dave Lee
 */
@RestController
@Api(value="/personalman", description="PersonalMan REST API")
@RequestMapping(value="/personalman")
public class PersonalManRestController {

    @Autowired
    private AbsenceService absenceService;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private UserService userService;

    @ApiOperation(value = "Add an absence", notes="Add an absence to the system.")
    @PostMapping(value="/absences")
    @ApiResponses(value = {@ApiResponse(code=201,message="Successfully created absence")})
    /**
     * Add an absence to the database based on the supplied absence request.
     * @param absenceRequest a <code>AbsenceRequest</code> object representing the absence to add.
     * @return a <code>ResponseEntity</code> containing the result of the action.
     */
    public ResponseEntity<Void> addAbsence (@RequestBody final AbsenceRequest absenceRequest ) {
        //First of all, check if any of the fields are empty or null, then return bad request.
        if (StringUtils.isNullOrEmpty(absenceRequest.getCategory()) || StringUtils.isNullOrEmpty(absenceRequest.getCompany())
                || StringUtils.isNullOrEmpty(absenceRequest.getEndDate()) || StringUtils.isNullOrEmpty(absenceRequest.getStartDate())
                || StringUtils.isNullOrEmpty(absenceRequest.getUsername()) ) {
            return ResponseEntity.badRequest().build();
        }
        //Now convert the dates to LocalDate. If end date is before start date then return bad request.
        LocalDate startLocalDate = DateUtils.convertDateToLocalDate(absenceRequest.getStartDate());
        LocalDate endLocalDate = DateUtils.convertDateToLocalDate(absenceRequest.getEndDate());
        if ( startLocalDate == null || endLocalDate == null || endLocalDate.isBefore(startLocalDate) ) {
            return ResponseEntity.badRequest().build();
        }
        //Now convert to absence object.
        Absence absence = AbsenceUtils.convertAbsenceRequestToAbsence(absenceRequest, startLocalDate, endLocalDate);
        absenceService.save(absence);
        //Return 201 if saved successfully.
        return ResponseEntity.status(201).build();
    }

    @ApiOperation(value = "Find or count absences", notes="Find or count absences in the system according to the specified criteria.")
    @GetMapping(value="/absences")
    @ApiResponses(value = {@ApiResponse(code=200,message="Successfully completed the search for absences")})
    /**
     * Find or count the amount of absences in the database based on the supplied criteria.
     * @param company a <code>String</code> containing the name of the company.
     * @param username a <code>String</code> containing the username which may be null (is optional).
     * @param startDate a <code>String</code> containing the start of the date range to search.
     * @param endDate a <code>String</code> containing the end of the date range to search.
     * @param category a <code>String</code> containing the category of the absences to find which may be null (is optional).
     * @param onlyCount a <code>boolean</code> which is true iff only the amount of absences should be retrieved (optimised performance). Default is false.
     * @return a <code>ResponseEntity</code> containing the absences found.
     */
    public ResponseEntity<AbsencesResponse> findAbsence (@RequestParam("company") final String company,
                                                         @RequestParam(value = "username", required=false) final String username,
                                                         @RequestParam("startDate") final String startDate,
                                                         @RequestParam("endDate") final String endDate,
                                                         @RequestParam(value="category", required=false) final String category,
                                                         @RequestParam(value = "onlyCount", defaultValue="false", required=false) final boolean onlyCount) {
        //Convert the dates to local date. If end date is before start date then return bad request.
        LocalDate startLocalDate = DateUtils.convertDateToLocalDate(startDate);
        LocalDate endLocalDate = DateUtils.convertDateToLocalDate(endDate);
        if ( startLocalDate == null || endLocalDate == null || endLocalDate.isBefore(startLocalDate) ) {
            return ResponseEntity.badRequest().body(null);
        }
        //Prepare response object.
        AbsencesResponse absencesResponse = absenceService.prepareAbsencesResponse();
        //Check if only count parameter was set to true.
        if ( onlyCount ) {
            //Convert category which is required for count.
            AbsenceCategory absenceCategory = null;
            if ( category != null ) {
                absenceCategory = AbsenceCategory.fromString(category);
            }
            if ( absenceCategory == null ) {
                return ResponseEntity.badRequest().body(null);
            }
            //Now try and count absences.
            Long count = absenceService.countAbsences(company, username, startLocalDate, endLocalDate, absenceCategory);
            //Set count.
            absencesResponse.setCount(count);
        } else {
            //Now try and find absences.
            List<Absence> absences = absenceService.findAbsences(company, username, startLocalDate, endLocalDate);
            //Convert the absences to a list of absence responses.
            List<AbsenceResponse> absenceResponses = AbsenceUtils.convertAbsencesToAbsenceResponses(absences);
            absencesResponse.setCount(new Long(absenceResponses.size()));
            absencesResponse.setAbsenceResponseList(absenceResponses);
            absencesResponse = AbsenceUtils.calculateAbsencesResponseStatistics(absencesResponse);
        }
        //Return 200 and results.
        return ResponseEntity.ok(absencesResponse);
    }

    @ApiOperation(value = "Delete absences", notes="Delete absences in the system according to the specified criteria.")
    @DeleteMapping(value="/absences")
    @ApiResponses(value = {@ApiResponse(code=200,message="Successfully deleted absences")})
    /**
     * Remove absences from the system based on the supplied criteria.
     * @param company a <code>String</code> containing the name of the company.
     * @param username a <code>String</code> containing the username which may be null (is optional).
     * @param startDate a <code>String</code> containing the start of the absence.
     * @param endDate a <code>String</code> containing the end of the absence.
     * @return a <code>ResponseEntity</code> containing the result of the action.
     */
    public ResponseEntity<Void> deleteAbsences (@RequestParam("company") final String company,
                                                         @RequestParam(value = "username", required=false) final String username,
                                                         @RequestParam("startDate") final String startDate,
                                                         @RequestParam("endDate") final String endDate) {
        //Convert the dates to local date. If end date is before start date then return bad request.
        LocalDate startLocalDate = DateUtils.convertDateToLocalDate(startDate);
        LocalDate endLocalDate = DateUtils.convertDateToLocalDate(endDate);
        if ( startLocalDate == null || endLocalDate == null || endLocalDate.isBefore(startLocalDate) ) {
            return ResponseEntity.badRequest().body(null);
        }
        //Now try and delete absences.
        absenceService.delete(company, username, startLocalDate, endLocalDate);
        //Return 200 if deleted successfully or nothing to delete.
        return ResponseEntity.status(200).build();
    }

    @ApiOperation(value = "Add a company", notes="Add a company to the system.")
    @PostMapping(value="/company")
    @ApiResponses(value = {@ApiResponse(code=201,message="Successfully created company")})
    /**
     * Add an absence to the database based on the supplied register company request.
     * @param registerCompanyRequest a <code>RegisterCompanyRequest</code> object representing the company to add.
     * @return a <code>ResponseEntity</code> containing the result of the action.
     */
    public ResponseEntity<Void> addCompany (@RequestBody final RegisterCompanyRequest registerCompanyRequest ) {
        companyService.save(Company.builder()
                .name(registerCompanyRequest.getName())
                .defaultAnnualLeaveInDays(registerCompanyRequest.getDefaultAnnualLeaveInDays())
                .country(registerCompanyRequest.getCountry())
                .build());
        //Return 201 if saved successfully.
        return ResponseEntity.status(201).build();
    }

    @ApiOperation(value = "Add a user", notes="Add a user to the system.")
    @PostMapping(value="/user")
    @ApiResponses(value = {@ApiResponse(code=201,message="Successfully created user")})
    /**
     * Add a user to the system.
     * @param userRequest a <code>UserRequest</code> object representing the user to add.
     * @return a <code>ResponseEntity</code> containing the result of the action.
     */
    public ResponseEntity<Void> addUser (@RequestBody final UserRequest userRequest ) {
        //First of all, check if any of the fields are empty or null, then return bad request.
        if (StringUtils.isNullOrEmpty(userRequest.getFirstName()) || StringUtils.isNullOrEmpty(userRequest.getSurname())
                || StringUtils.isNullOrEmpty(userRequest.getPosition()) || StringUtils.isNullOrEmpty(userRequest.getStartDate())
                || StringUtils.isNullOrEmpty(userRequest.getUsername()) || StringUtils.isNullOrEmpty(userRequest.getWorkingDays())
                || userRequest.getLeaveEntitlementPerYear() <= 0 || StringUtils.isNullOrEmpty(userRequest.getCompany())) {
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

    @ApiOperation(value = "Find a user", notes="Find a user to the system.")
    @GetMapping(value="/user")
    @ApiResponses(value = {@ApiResponse(code=200,message="Successfully found user"), @ApiResponse(code=204,message="Successful but no user found")})
    /**
     * Find a user based on their username and company.
     * @param company a <code>String</code> containing the name of the company.
     * @param username a <code>String</code> containing the username.
     * @return a <code>ResponseEntity</code> containing the user found.
     */
    public ResponseEntity<UserResponse> getUser (@RequestParam("company") final String company, @RequestParam("username") final String username ) {
        //First of all, check if the username field is empty or null, then return bad request.
        if (StringUtils.isNullOrEmpty(username) || StringUtils.isNullOrEmpty(company)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
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

    @ApiOperation(value = "Find all users for a company", notes="Find all users for a company to the system.")
    @GetMapping(value="/users")
    @ApiResponses(value = {@ApiResponse(code=200,message="Successfully found user(s)"), @ApiResponse(code=204,message="Successful but no users found")})
    /**
     * Find all users for a specific company.
     * @param company a <code>String</code> containing the name of the company.
     * @return a <code>ResponseEntity</code> containing the users for this company.
     */
    public ResponseEntity<UsersResponse> getUsers (@RequestParam("company") final String company ) {
        //First of all, check if the compny field is empty or null, then return bad request.
        if ( StringUtils.isNullOrEmpty(company)) {
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
                .count(Long.valueOf(userResponses.length))
                .userResponses(userResponses)
                .build());
    }

    @ApiOperation(value = "Delete a user", notes="Delete a user from the system.")
    @DeleteMapping(value="/user")
    @ApiResponses(value = {@ApiResponse(code=200,message="Successfully delete user"), @ApiResponse(code=204,message="Successful but no user found")})
    /**
     * Delete a specific user from the database based on their username and company.
     * @param company a <code>String</code> containing the name of the company.
     * @param username a <code>String</code> containing the username.
     * @return a <code>ResponseEntity</code> containing the results of the action.
     */
    public ResponseEntity<Void> deleteUser (@RequestParam("company") final String company, @RequestParam("username") final String username ) {
        //First of all, check if the username field is empty or null, then return bad request.
        if (StringUtils.isNullOrEmpty(username) || StringUtils.isNullOrEmpty(company)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
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

}
