import { Injectable } from '@nestjs/common';
import { Company } from './models/company.model';

@Injectable()
export class CompanyService {

    /**
     * Save the specified company object in the database.
     * @param company a <code>Company</code> object to save in the database.
     * @return a <code>boolean</code> which is true iff the company has been validated and saved successfully.
     */
    public save ( company: Company ) : boolean {
        const createdCompany = new this.companyModel(company);
        return createdCompany.save() != null;
    }

    /**
     * List all companies which are stored in the database.
     * @return a <code>List</code> of <code>String</code> with the names of all companies stored in the database.
     */
    public getAllCompanies(): string[] {
        var companyNames: string[] = [];
        companyModel.find().exec().forEach(company => companyNames.push(company.getName()));
        return companyNames;
    }

    /**
     * Get the information of a particular company based on its name.
     * @param name a <code>String</code> containing the name of the company to retrieve.
     * @return a <code>Company</code> object containing the information for the company or null if the company was not found.
     */
    public getCompany ( name: string ) : Company {
        return companyModel.find({name: name});
    }

    /**
     * Delete the specified company according to the supplied company name.
     * @param name a <code>String</code> with the company name to delete.
     * @return a <code>boolean</code> which is true iff the company was deleted successfully.
     *
     */
    public delete ( name: string ) : boolean {
        var company: Company = this.getCompany(name);
        if ( company != null ) {
            companyModel.deleteOne(company);
            return true;
        }
        return false;
    }

}
