package de.davelee.personalman.server.services;

import de.davelee.personalman.server.model.Company;
import de.davelee.personalman.server.repository.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

}
