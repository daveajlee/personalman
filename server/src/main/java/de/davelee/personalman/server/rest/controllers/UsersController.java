package de.davelee.personalman.server.rest.controllers;

import de.davelee.personalman.api.PaidUserRequest;
import de.davelee.personalman.api.PayUsersResponse;
import de.davelee.personalman.api.UserResponse;
import de.davelee.personalman.api.UsersResponse;
import de.davelee.personalman.server.model.User;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * This class defines the endpoints for the REST API which manipulate users and delegates the actions to the UserService class.
 * @author Dave Lee
 */
@RestController
@Tag(name="/api/users")
@RequestMapping(value="/api/users")
public class UsersController {

    @Autowired
    private UserService userService;

    /**
     * Find all users for a specific company.
     * @param company a <code>String</code> containing the name of the company.
     * @param token a <code>String</code> containing the token to verify that the user is logged in.
     * @return a <code>ResponseEntity</code> containing the users for this company.
     */
    @Operation(summary = "Find all users for a company", description="Find all users for a company to the system.")
    @GetMapping(value="/")
    @ApiResponses(value = {@ApiResponse(responseCode="200",description="Successfully found user(s)"), @ApiResponse(responseCode = "204",description = "Successful but no users found")})
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
     * Pay all users for a specific company who have worked within a supplied date range.
     * @param company a <code>String</code> containing the name of the company.
     * @param token a <code>String</code> containing the token to verify that the user is logged in.
     * @param startDate a <code>String</code> containing the start date (inclusive) of the date range in format dd-MM-yyyy.
     * @param endDate a <code>String</code> containing the end date (inclusive) of the date range in format dd-MM-yyyy.
     * @return a <code>ResponseEntity</code> containing the users for this company.
     */
    @Operation(summary = "Pay all users for a company", description="Pay all users for a company within a specific date range.")
    @GetMapping(value="/pay")
    @ApiResponses(value = {@ApiResponse(responseCode="200",description="Successfully found user(s) and their pay"), @ApiResponse(responseCode = "204",description="Successful but no users found")})
    public ResponseEntity<PayUsersResponse> payUsers (@RequestParam("company") final String company,
                                                      @RequestParam("token") final String token,
                                                      @RequestParam("startDate") final String startDate,
                                                      @RequestParam("endDate") final String endDate) {
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
        //Now go through each user if they have worked during the date range and then calculate pay.
        BigDecimal totalSum = new BigDecimal(0); Map<String, Double> employeePayTable = new HashMap<>();
        for ( User user : users ) {
            BigDecimal sumToBePaid = new BigDecimal(0);
            LocalDate startLocalDate = DateUtils.convertDateToLocalDate(startDate);
            LocalDate endLocalDate = DateUtils.convertDateToLocalDate(endDate);
            while ( startLocalDate.isBefore(endLocalDate) || startLocalDate.isEqual(endLocalDate) ) {
                sumToBePaid = sumToBePaid.add(new BigDecimal(userService.getHoursForDate(user, startLocalDate)).multiply(user.getHourlyWage()));
                startLocalDate = startLocalDate.plusDays(1);
            }
            employeePayTable.put(user.getUserName(), sumToBePaid.doubleValue());
            totalSum = totalSum.add(sumToBePaid);
        }
        //Return response.
        return ResponseEntity.ok(PayUsersResponse.builder()
                .employeePayTable(employeePayTable)
                .totalSum(totalSum.doubleValue())
                .build());
    }

    /**
     * Mark all supplied users for a specific company as paid within a particular date range.
     * @param paidUserRequest a <code>PaidUserRequest</code> object containing the information to save.
     * @return a <code>ResponseEntity</code> confirming operation was successful.
     */
    @Operation(summary = "Mark users as paid for a company", description="Mark users as paid for a company within a specific date range.")
    @GetMapping(value="/paid")
    @ApiResponses(value = {@ApiResponse(responseCode = "200",description="Successfully found user(s) and their pay"), @ApiResponse(responseCode = "204",description="Successful but no users found")})
    public ResponseEntity<Void> paidUsers (@RequestBody final PaidUserRequest paidUserRequest) {
        //Verify that user is logged in.
        if ( paidUserRequest.getToken() == null || !userService.checkAuthToken(paidUserRequest.getToken()) ) {
            return ResponseEntity.status(403).build();
        }
        //First of all, check if the compny field is empty or null, then return bad request.
        if ( StringUtils.isBlank(paidUserRequest.getCompany())) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        //For each user in the supplied map.
        Set<String> usernameSet = paidUserRequest.getEmployeePayTable().keySet();
        for ( String username : usernameSet ) {
            //Find the relevant user.
            User user = userService.findByCompanyAndUserName(paidUserRequest.getCompany(), username);
            if ( !userService.addUserHistoryEntry(user, LocalDate.now(), UserHistoryReason.PAID,
                    "Paid " + paidUserRequest.getEmployeePayTable().get(username) + " for date range " +
                    paidUserRequest.getStartDate() + " - " + paidUserRequest.getEndDate())) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        }
        //Return empty ok response if no exceptions.
        return ResponseEntity.ok().build();
    }

}
