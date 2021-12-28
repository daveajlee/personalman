package de.davelee.personalman.server.rest.controllers;

import de.davelee.personalman.server.services.CompanyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * This class defines the endpoints for the REST API which manipulate companies and delegates the actions to the CompanyService class.
 * @author Dave Lee
 */
@RestController
@Tag(name="/api/companies")
@RequestMapping(value="/api/companies")
public class CompaniesController {

    @Autowired
    private CompanyService companyService;

    /**
     * Find all companies stored in PersonalMan.
     * @return a <code>ResponseEntity</code> containing the names of all companies stored in PersonalMan.
     */
    @Operation(summary = "Find all companies", description="Find all companies stored in PersonalMan.")
    @GetMapping(value="/")
    @ApiResponses(value = {@ApiResponse(responseCode="200",description="Successfully found companies"), @ApiResponse(responseCode="204",description="Successful but no companies in database")})
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

}
