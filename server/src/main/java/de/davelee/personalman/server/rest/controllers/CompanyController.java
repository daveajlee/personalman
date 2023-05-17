package de.davelee.personalman.server.rest.controllers;

import de.davelee.personalman.api.CompanyResponse;
import de.davelee.personalman.api.RegisterCompanyRequest;
import de.davelee.personalman.server.model.Company;
import de.davelee.personalman.server.services.AbsenceService;
import de.davelee.personalman.server.services.CompanyService;
import de.davelee.personalman.server.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * This class defines the endpoints for the REST API which manipulate a single company and delegates the actions to the CompanyService class.
 * @author Dave Lee
 */
@RestController
@Tag(name="/api/company")
@RequestMapping(value="/api/company")
public class CompanyController {

    @Autowired
    private CompanyService companyService;

    @Autowired
    private UserService userService;

    @Autowired
    private AbsenceService absenceService;

    /**
     * Add an absence to the database based on the supplied register company request.
     * @param registerCompanyRequest a <code>RegisterCompanyRequest</code> object representing the company to add.
     * @return a <code>ResponseEntity</code> containing the result of the action.
     */
    @Operation(summary = "Add a company", description="Add a company to the system.")
    @CrossOrigin()
    @PostMapping(value="/")
    @ApiResponses(value = {@ApiResponse(responseCode="201",description="Successfully created company")})
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
    @Operation(summary = "Retrieve a company", description="Retrieve a company from the system.")
    @GetMapping(value="/")
    @ApiResponses(value = {@ApiResponse(responseCode="200",description="Successfully retrieved company"), @ApiResponse(responseCode="404",description="Company not found")})
    @ResponseBody
    public ResponseEntity<CompanyResponse> getCompany (@RequestParam("name") final String name, @RequestParam("token") final String token ) {
        //Check valid request including authentication
        HttpStatus status = validateAndAuthenticateRequest(name, token);
        //If the status is not null then produce response and return.
        if ( status != null ) {
            return new ResponseEntity<>(status);
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
    @Operation(summary = "Delete a company", description="Delete a company from the system.")
    @DeleteMapping(value="/")
    @ApiResponses(value = {@ApiResponse(responseCode="200",description="Successfully delete company"), @ApiResponse(responseCode="404",description="Company not found")})
    public ResponseEntity<Void> deleteCompany (@RequestParam("name") final String name, @RequestParam("token") final String token ) {
        //Check valid request including authentication
        HttpStatus status = validateAndAuthenticateRequest(name, token);
        //If the status is not null then produce response and return.
        if ( status != null ) {
            return new ResponseEntity<>(status);
        }
        //First of all, delete all users and absences belonging to this company.
        absenceService.delete(name);
        userService.delete(name);
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
     * Private helper method to verify that at least name and token are supplied and valid.
     * @param name a <code>String</code> containing the name of the company.
     * @param token a <code>String</code> containing the token to verify that the user is logged in.
     * @return a <code>HttpStatus</code> which is either filled if it was not authenticated or null if authenticated and valid.
     */
    private HttpStatus validateAndAuthenticateRequest ( final String name, final String token ) {
        //First of all, check if the name field is empty or null, then return bad request.
        if (StringUtils.isBlank(name) ) {
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
