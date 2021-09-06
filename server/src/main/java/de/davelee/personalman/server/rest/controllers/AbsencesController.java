package de.davelee.personalman.server.rest.controllers;

import de.davelee.personalman.api.AbsenceRequest;
import de.davelee.personalman.api.AbsenceResponse;
import de.davelee.personalman.api.AbsencesResponse;
import de.davelee.personalman.server.model.AbsenceCategory;
import de.davelee.personalman.server.services.AbsenceService;
import de.davelee.personalman.server.services.UserService;
import de.davelee.personalman.server.utils.AbsenceUtils;
import de.davelee.personalman.server.utils.DateUtils;
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
import java.util.HashMap;
import java.util.List;

/**
 * This class defines the endpoints for the REST API which manipulate absences and delegates the actions to the AbsenceService class.
 * @author Dave Lee
 */
@RestController
@Api(value="/personalman/absences")
@RequestMapping(value="/personalman/absences")
public class AbsencesController {

    @Autowired
    private AbsenceService absenceService;

    @Autowired
    private UserService userService;

    /**
     * Add an absence to the database based on the supplied absence request.
     * @param absenceRequest a <code>AbsenceRequest</code> object representing the absence to add.
     * @return a <code>ResponseEntity</code> containing the result of the action.
     */
    @ApiOperation(value = "Add an absence", notes="Add an absence to the system.")
    @PostMapping(value="/")
    @ApiResponses(value = {@ApiResponse(code=201,message="Successfully created absence")})
    public ResponseEntity<Void> addAbsence (@RequestBody final AbsenceRequest absenceRequest ) {
        //Verify request was valid and authenticated.
        HttpStatus status = validateAndAuthenticateRequest(absenceRequest.getStartDate(), absenceRequest.getEndDate(), absenceRequest.getToken());
        if ( status != null ) {
            return ResponseEntity.status(status).build();
        }
        //First of all, check if any of the fields are empty or null, then return bad request.
        if (StringUtils.isBlank(absenceRequest.getCategory()) || StringUtils.isBlank(absenceRequest.getCompany())
                || StringUtils.isBlank(absenceRequest.getEndDate()) || StringUtils.isBlank(absenceRequest.getStartDate())
                || StringUtils.isBlank(absenceRequest.getUsername()) ) {
            return ResponseEntity.badRequest().build();
        }
        //Now convert to absence object.
        absenceService.save(AbsenceUtils.convertAbsenceRequestToAbsence(absenceRequest,
                DateUtils.convertDateToLocalDate(absenceRequest.getStartDate()), DateUtils.convertDateToLocalDate(absenceRequest.getEndDate())));
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
    @GetMapping(value="/")
    @ApiResponses(value = {@ApiResponse(code=200,message="Successfully completed the search for absences")})
    public ResponseEntity<AbsencesResponse> findAbsence (@RequestParam("company") final String company,
                                                         @RequestParam(value = "username", required=false) final String username,
                                                         @RequestParam("startDate") final String startDate,
                                                         @RequestParam("endDate") final String endDate,
                                                         @RequestParam(value="category", required=false) final String category,
                                                         @RequestParam(value = "onlyCount", defaultValue="false", required=false) final boolean onlyCount,
                                                         @RequestParam("token") final String token) {
        //Verify request was valid and authenticated.
        HttpStatus status = validateAndAuthenticateRequest(startDate, endDate, token);
        if ( status != null ) {
            return ResponseEntity.status(status).build();
        }
        //Prepare response object.
        AbsencesResponse absencesResponse = prepareAbsencesResponse();
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
            Long count = absenceService.countAbsences(company, username, DateUtils.convertDateToLocalDate(startDate),
                    DateUtils.convertDateToLocalDate(endDate), absenceCategory);
            //Set count.
            absencesResponse.setCount(count);
        } else {
            //Now try and find absences. Convert the absences to a list of absence responses.
            List<AbsenceResponse> absenceResponses = AbsenceUtils.convertAbsencesToAbsenceResponses(absenceService.findAbsences(company, username,  DateUtils.convertDateToLocalDate(startDate),
                    DateUtils.convertDateToLocalDate(endDate)));
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
    @DeleteMapping(value="/")
    @ApiResponses(value = {@ApiResponse(code=200,message="Successfully deleted absences")})
    public ResponseEntity<Void> deleteAbsences (@RequestParam("company") final String company,
                                                @RequestParam(value = "username", required=false) final String username,
                                                @RequestParam("startDate") final String startDate,
                                                @RequestParam("endDate") final String endDate,
                                                @RequestParam("token") final String token) {
        //Verify request was valid and authenticated.
        HttpStatus status = validateAndAuthenticateRequest(startDate, endDate, token);
        if ( status != null ) {
            return ResponseEntity.status(status).build();
        }
        //Now try and delete absences.
        absenceService.delete(company, username, DateUtils.convertDateToLocalDate(startDate), DateUtils.convertDateToLocalDate(endDate));
        //Return 200 if deleted successfully or nothing to delete.
        return ResponseEntity.status(200).build();
    }

    /**
     * Private helper method to verify that token is supplied and valid and that the start and end dates are valid.
     * @param startDate a <code>String</code> containing the name of the company.
     * @param endDate a <code>String</code> containing the username.
     * @param token a <code>String</code> containing the token to verify that the user is logged in.
     * @return a <code>HttpStatus</code> which is either filled if it was not authenticated or null if authenticated and valid.
     */
    private HttpStatus validateAndAuthenticateRequest ( final String startDate, final String endDate, final String token ) {
        //First of all, check if the start and end date fields are valid. If not, then return bad request.
        if ( StringUtils.isBlank(startDate) || StringUtils.isBlank(endDate) || StringUtils.isBlank(token) ) {
            return HttpStatus.BAD_REQUEST;
        }
        LocalDate startLocalDate = DateUtils.convertDateToLocalDate(startDate);
        LocalDate endLocalDate = DateUtils.convertDateToLocalDate(endDate);
        if ( startLocalDate == null || endLocalDate == null || endLocalDate.isBefore(startLocalDate) ) {
            return HttpStatus.BAD_REQUEST;
        }
        //Verify that user is logged in.
        if ( token == null || !userService.checkAuthToken(token) ) {
            return HttpStatus.FORBIDDEN;
        }
        //If everything was ok then return null.
        return null;
    }

    /**
     * Private helper method to prepare AbsencesResponse.
     * @return a <code>AbsencesResponse</code> object containing the basic statistics map to.
     */
    private AbsencesResponse prepareAbsencesResponse ( ) {
        HashMap<String, Integer> statisticsMap = new HashMap<>();
        AbsenceCategory[] absenceCategories = AbsenceCategory.values();
        for ( AbsenceCategory absenceCategory : absenceCategories ) {
            statisticsMap.put(absenceCategory.toString(), 0);
        }
        return AbsencesResponse.builder()
                .statisticsMap(statisticsMap)
                .build();
    }

}
