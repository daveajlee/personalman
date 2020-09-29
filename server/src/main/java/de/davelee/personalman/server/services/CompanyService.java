package de.davelee.personalman.server.services;

import de.davelee.personalman.server.model.Company;
import de.davelee.personalman.server.repository.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Class to provide service operations for companies in the PersonalMan program.
 * @author Dave Lee
 */
@Service
public class CompanyService {

    @Autowired
    private CompanyRepository companyRepository;

    /**
     * Save the specified company object in the database.
     * @param company a <code>Company</code> object to save in the database.
     * @return a <code>boolean</code> which is true iff the company has been validated and saved successfully.
     */
    public boolean save ( final Company company ) {
        companyRepository.save(company);
        return true;
    }

    /**
     * List all companies which are stored in the database.
     * @return a <code>List</code> of <code>String</code> with the names of all companies stored in the database.
     */
    public List<String> getAllCompanies ( ) {
        List<String> companyNames = new ArrayList<>();
        for (Company company : companyRepository.findAll()) {
            companyNames.add(company.getName());
        }
        return companyNames;
    }

    /**
     * Delete the specified company according to the supplied company name.
     * @param name a <code>String</code> with the company name to delete.
     * @return a <code>boolean</code> which is true iff the company was deleted successfully.
     *
     */
    public boolean delete ( final String name ) {
        Company company = companyRepository.findByName(name);
        if ( company != null ) {
            companyRepository.delete(company);
            return true;
        }
        return false;
    }

}
