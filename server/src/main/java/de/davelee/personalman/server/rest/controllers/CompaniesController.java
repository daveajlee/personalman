package de.davelee.personalman.server.rest.controllers;

import de.davelee.personalman.server.services.CompanyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
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
@Api(value="/personalman/companies")
@RequestMapping(value="/personalman/companies")
public class CompaniesController {

    @Autowired
    private CompanyService companyService;

    /**
     * Find all companies stored in PersonalMan.
     * @return a <code>ResponseEntity</code> containing the names of all companies stored in PersonalMan.
     */
    @ApiOperation(value = "Find all companies", notes="Find all companies stored in PersonalMan.")
    @GetMapping(value="/")
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

}
