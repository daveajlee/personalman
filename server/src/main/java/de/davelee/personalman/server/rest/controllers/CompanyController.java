package de.davelee.personalman.server.rest.controllers;

import de.davelee.personalman.api.CompanyResponse;
import de.davelee.personalman.api.RegisterCompanyRequest;
import de.davelee.personalman.server.model.Company;
import de.davelee.personalman.server.services.CompanyService;
import de.davelee.personalman.server.services.UserService;
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

/**
 * This class defines the endpoints for the REST API which manipulate a single company and delegates the actions to the CompanyService class.
 * @author Dave Lee
 */
@RestController
@Api(value="/personalman/company")
@RequestMapping(value="/personalman/company")
public class CompanyController {

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
    @PostMapping(value="/")
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
    @GetMapping(value="/")
    @ApiResponses(value = {@ApiResponse(code=200,message="Successfully retrieved company"), @ApiResponse(code=404,message="Company not found")})
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
    @ApiOperation(value = "Delete a company", notes="Delete a company from the system.")
    @DeleteMapping(value="/")
    @ApiResponses(value = {@ApiResponse(code=200,message="Successfully delete company"), @ApiResponse(code=404,message="Company not found")})
    public ResponseEntity<Void> deleteCompany (@RequestParam("name") final String name, @RequestParam("token") final String token ) {
        //Check valid request including authentication
        HttpStatus status = validateAndAuthenticateRequest(name, token);
        //If the status is not null then produce response and return.
        if ( status != null ) {
            return new ResponseEntity<>(status);
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
