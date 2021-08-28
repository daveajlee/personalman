package de.davelee.personalman.server.rest.controllers;

import de.davelee.personalman.api.*;

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
    private AbsenceService absenceService;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private UserService userService;

    /**
     * Add an absence to the database based on the supplied absence request.
     * @param absenceRequest a <code>AbsenceRequest</code> object representing the absence to add.
     * @return a <code>ResponseEntity</code> containing the result of the action.
     */
    @ApiOperation(value = "Add an absence", notes="Add an absence to the system.")
    @PostMapping(value="/absences")
    @ApiResponses(value = {@ApiResponse(code=201,message="Successfully created absence")})
    public ResponseEntity<Void> addAbsence (@RequestBody final AbsenceRequest absenceRequest ) {
        //Verify that user is logged in.
        if ( absenceRequest.getToken() == null || !userService.checkAuthToken(absenceRequest.getToken()) ) {
            return ResponseEntity.status(403).build();
        }
        //First of all, check if any of the fields are empty or null, then return bad request.
        if (StringUtils.isBlank(absenceRequest.getCategory()) || StringUtils.isBlank(absenceRequest.getCompany())
                || StringUtils.isBlank(absenceRequest.getEndDate()) || StringUtils.isBlank(absenceRequest.getStartDate())
                || StringUtils.isBlank(absenceRequest.getUsername()) ) {
            return ResponseEntity.badRequest().build();
        }
        //Now convert the dates to LocalDate. If end date is before start date then return bad request.
        LocalDate startLocalDate = DateUtils.convertDateToLocalDate(absenceRequest.getStartDate());
        LocalDate endLocalDate = DateUtils.convertDateToLocalDate(absenceRequest.getEndDate());
        if ( startLocalDate == null || endLocalDate == null || endLocalDate.isBefore(startLocalDate) ) {
            return ResponseEntity.badRequest().build();
        }
        //Now convert to absence object.
        absenceService.save(AbsenceUtils.convertAbsenceRequestToAbsence(absenceRequest, startLocalDate, endLocalDate));
        //Return 201 if saved successfully.
        return ResponseEntity.status(201).build();
    }

    /**
     * Find or count the amount of absences in the database based on the supplied criteria.
     * @param company a <code>String</code> containing the name of the company.
     * @param username a <code>String</code> containing the username which may be null (is optional).
     * @param startDate a <code>String</code> containing the start of the date range to search.
     * @param endDate a <code>String</code> containing the end of the date range to search.
     * @param category a <code>String</code> containing the category of the absences to find which may be null (is optional).
     * @param onlyCount a <code>boolean</code> which is true iff only the amount of absences should be retrieved (optimised performance). Default is false.
     * @param token a <code>String</code> to verify that the user is logged in.
     * @return a <code>ResponseEntity</code> containing the absences found.
     */
    @ApiOperation(value = "Find or count absences", notes="Find or count absences in the system according to the specified criteria.")
    @GetMapping(value="/absences")
    @ApiResponses(value = {@ApiResponse(code=200,message="Successfully completed the search for absences")})
    public ResponseEntity<AbsencesResponse> findAbsence (@RequestParam("company") final String company,
                                                         @RequestParam(value = "username", required=false) final String username,
                                                         @RequestParam("startDate") final String startDate,
                                                         @RequestParam("endDate") final String endDate,
                                                         @RequestParam(value="category", required=false) final String category,
                                                         @RequestParam(value = "onlyCount", defaultValue="false", required=false) final boolean onlyCount,
                                                         @RequestParam("token") final String token) {
        //Verify that user is logged in.
        if ( token == null || !userService.checkAuthToken(token) ) {
            return ResponseEntity.status(403).build();
        }
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
            //Now try and find absences. Convert the absences to a list of absence responses.
            List<AbsenceResponse> absenceResponses = AbsenceUtils.convertAbsencesToAbsenceResponses(absenceService.findAbsences(company, username, startLocalDate, endLocalDate));
            absencesResponse.setCount((long) absenceResponses.size());
            absencesResponse.setAbsenceResponseList(absenceResponses);
            absencesResponse = AbsenceUtils.calculateAbsencesResponseStatistics(absencesResponse);
        }
        //Return 200 and results.
        return ResponseEntity.ok(absencesResponse);
    }

    /**
     * Remove absences from the system based on the supplied criteria.
     * @param company a <code>String</code> containing the name of the company.
     * @param username a <code>String</code> containing the username which may be null (is optional).
     * @param startDate a <code>String</code> containing the start of the absence.
     * @param endDate a <code>String</code> containing the end of the absence.
     * @param token a <code>String</code> to verify if the user is logged in.
     * @return a <code>ResponseEntity</code> containing the result of the action.
     */
    @ApiOperation(value = "Delete absences", notes="Delete absences in the system according to the specified criteria.")
    @DeleteMapping(value="/absences")
    @ApiResponses(value = {@ApiResponse(code=200,message="Successfully deleted absences")})
    public ResponseEntity<Void> deleteAbsences (@RequestParam("company") final String company,
                                                         @RequestParam(value = "username", required=false) final String username,
                                                         @RequestParam("startDate") final String startDate,
                                                         @RequestParam("endDate") final String endDate,
                                                         @RequestParam("token") final String token) {
        //Verify that user is logged in.
        if ( token == null || !userService.checkAuthToken(token) ) {
            return ResponseEntity.status(403).build();
        }
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
     * Find all companies stored in PersonalMan.
     * @return a <code>ResponseEntity</code> containing the names of all companies stored in PersonalMan.
     */
    @ApiOperation(value = "Find all companies", notes="Find all companies stored in PersonalMan.")
    @GetMapping(value="/companies")
    @ApiResponses(value = {@ApiResponse(code=200,message="Successfully found companies"), @ApiResponse(code=204,message="Successful but no companies in database")})
    public ResponseEntity<List<String>> getCompanies () {
        //Retrieve the list of companies.
        List<String> companyNames = companyService.getAllCompanies();
        //If no companies then return 204.
        if ( companyNames.size() == 0 ) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        //Otherwise return 200.
        return ResponseEntity.ok(companyNames);
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
     * Add a user to the system.
     * @param userRequest a <code>UserRequest</code> object representing the user to add.
     * @return a <code>ResponseEntity</code> containing the result of the action.
     */
    @ApiOperation(value = "Add a user", notes="Add a user to the system.")
    @PostMapping(value="/user")
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
    @GetMapping(value="/user")
    @ApiResponses(value = {@ApiResponse(code=200,message="Successfully found user"), @ApiResponse(code=204,message="Successful but no user found")})
    public ResponseEntity<UserResponse> getUser (@RequestParam("company") final String company, @RequestParam("username") final String username,
                                                 @RequestParam("token") final String token) {
        //Verify that user is logged in.
        if ( token == null || !userService.checkAuthToken(token) ) {
            return ResponseEntity.status(403).build();
        }
        //First of all, check if the username field is empty or null, then return bad request.
        if (StringUtils.isBlank(username) || StringUtils.isBlank(company)) {
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
     * Delete a specific user from the database based on their username and company.
     * @param company a <code>String</code> containing the name of the company.
     * @param username a <code>String</code> containing the username.
     * @param token a <code>String</code> containing the token to verify that the user is logged in.
     * @return a <code>ResponseEntity</code> containing the results of the action.
     */
    @ApiOperation(value = "Delete a user", notes="Delete a user from the system.")
    @DeleteMapping(value="/user")
    @ApiResponses(value = {@ApiResponse(code=200,message="Successfully delete user"), @ApiResponse(code=204,message="Successful but no user found")})
    public ResponseEntity<Void> deleteUser (@RequestParam("company") final String company, @RequestParam("username") final String username,
                                            @RequestParam("token") final String token) {
        //Verify that user is logged in.
        if ( token == null || !userService.checkAuthToken(token) ) {
            return ResponseEntity.status(403).build();
        }
        //First of all, check if the username field is empty or null, then return bad request.
        if (StringUtils.isBlank(username) || StringUtils.isBlank(company)) {
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

    /**
     * Update or set the salary information for a particular user. If the user already has salary information
     * then these will be overwritten.
     * @param updateSalaryRequest a <code>UpdateSalaryRequest</code> object containing the information to update.
     * @return a <code>ResponseEntity</code> containing the results of the action.
     */
    @ApiOperation(value = "Update salary information", notes="Update salary information for a particular user.")
    @PutMapping(value="/user/salary")
    @ApiResponses(value = {@ApiResponse(code=200,message="Successfully updated salary information"), @ApiResponse(code=204,message="No user found")})
    public ResponseEntity<Void> updateSalaryInformation (@RequestBody UpdateSalaryRequest updateSalaryRequest) {
        //Verify that user is logged in.
        if ( updateSalaryRequest.getToken() == null || !userService.checkAuthToken(updateSalaryRequest.getToken()) ) {
            return ResponseEntity.status(403).build();
        }
        //First of all, check if the username field is empty or null, then return bad request.
        if (StringUtils.isBlank(updateSalaryRequest.getUsername()) || StringUtils.isBlank(updateSalaryRequest.getCompany())) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
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
     * @param company a <code>String</code> containing the name of the company.
     * @param username a <code>String</code> containing the username.
     * @param token a <code>String</code> containing the token to verify that the user is logged in.
     * @param trainingCourse a <code>String</code> containing the name of the training course or qualification.
     * @return a <code>ResponseEntity</code> containing the results of the action.
     */
    @ApiOperation(value = "Add a training course", notes="Add a training course for a particular user.")
    @PutMapping(value="/user/training")
    @ApiResponses(value = {@ApiResponse(code=200,message="Successfully added training course"), @ApiResponse(code=204,message="No user found")})
    public ResponseEntity<Void> addTraining (@RequestParam("company") final String company, @RequestParam("username") final String username,
                                             @RequestParam("token") final String token, @RequestParam("trainingCourse") final String trainingCourse) {
        //Verify that user is logged in.
        if ( token == null || !userService.checkAuthToken(token) ) {
            return ResponseEntity.status(403).build();
        }
        //First of all, check if the username field is empty or null, then return bad request.
        if (StringUtils.isBlank(username) || StringUtils.isBlank(company)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        //Now retrieve the user based on the username.
        User user = userService.findByCompanyAndUserName(company, username);
        //If user is null then return 204.
        if ( user == null ) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        //Now add training course.
        userService.addTrainingCourse(user, trainingCourse);
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
    @PutMapping(value="/user/timesheet")
    @ApiResponses(value = {@ApiResponse(code=200,message="Successfully added hours"), @ApiResponse(code=204,message="No user found")})
    public ResponseEntity<Void> addHoursForDate (@RequestBody AddTimeSheetHoursRequest addTimeSheetHoursRequest) {
        //Verify that user is logged in.
        if ( addTimeSheetHoursRequest.getToken() == null || !userService.checkAuthToken(addTimeSheetHoursRequest.getToken()) ) {
            return ResponseEntity.status(403).build();
        }
        //First of all, check if the username field is empty or null, then return bad request.
        if (StringUtils.isBlank(addTimeSheetHoursRequest.getUsername()) || StringUtils.isBlank(addTimeSheetHoursRequest.getCompany())) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
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
    @GetMapping(value="/user/timesheet")
    @ApiResponses(value = {@ApiResponse(code=200,message="Successfully retrieved hours"), @ApiResponse(code=204,message="No user found")})
    public ResponseEntity<Integer> getHoursForDate (@RequestParam("company") final String company, @RequestParam("username") final String username,
                                                    @RequestParam("token") final String token, @RequestParam("startDate") final String startDate,
                                                    @RequestParam("endDate") final String endDate ) {
        //Verify that user is logged in.
        if ( token == null || !userService.checkAuthToken(token) ) {
            return ResponseEntity.status(403).build();
        }
        //First of all, check if the username field is empty or null, then return bad request.
        if (StringUtils.isBlank(username) || StringUtils.isBlank(company)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
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

    /**
     * Change the password of the supplied user. Return 200 if the password was changed successfully or 404 if the user was not found
     * or the password supplied did not match the current password of the user.
     * @param changePasswordRequest a <code>ChangePasswordRequest</code> object containing the company, username, old password and new password.
     * @return a <code>ResponseEntity</code> object with status 200 if password changed or 404 if user not found.
     */
    @ApiOperation(value="changePassword", notes="Change password for a user")
    @PostMapping(value="/changePassword")
    @ApiResponses(@ApiResponse(code=200,message="Successfully processed change password request"))
    public ResponseEntity<Void> changePassword (@RequestBody final ChangePasswordRequest changePasswordRequest) {
        //Verify that user is logged in.
        if ( changePasswordRequest.getToken() == null || !userService.checkAuthToken(changePasswordRequest.getToken()) ) {
            return ResponseEntity.status(403).build();
        }
        boolean result = userService.changePassword(changePasswordRequest.getCompany(), changePasswordRequest.getUsername(),
                changePasswordRequest.getCurrentPassword(), changePasswordRequest.getNewPassword());
        //If result is true, then return 200 otherwise return 404 to indicate user not found.
        return result ? ResponseEntity.status(200).build() : ResponseEntity.status(404).build();
    }

}
