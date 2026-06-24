import { Injectable } from '@nestjs/common';
import { Company } from './models/company.model';
import { InjectModel } from '@nestjs/mongoose';
import { Model } from 'mongoose';

@Injectable()
export class CompanyService {

    constructor(@InjectModel(Company.name) private companyModel: Model<Company>) {}

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
    public async getAllCompanies(): Promise<string[]> {
        var companyNames: string[] = [];
        let companies: Company[] = await this.companyModel.find().exec();
        companies.forEach(company => companyNames.push(company.getName()));
        return companyNames;
    }

    /**
     * Get the information of a particular company based on its name.
     * @param name a <code>String</code> containing the name of the company to retrieve.
     * @return a <code>Company</code> object containing the information for the company or null if the company was not found.
     */
    public async getCompany ( name: string ) : Promise<Company> {
        let companies: Company[] = await this.companyModel.find({name: name}).exec();
        return companies[0];
    }

    /**
     * Delete the specified company according to the supplied company name.
     * @param name a <code>String</code> with the company name to delete.
     * @return a <code>boolean</code> which is true iff the company was deleted successfully.
     *
     */
    public async delete ( name: string ) : Promise<boolean> {
        var company: Company = await this.getCompany(name);
        if ( company != null ) {
            this.companyModel.deleteOne(company);
            return true;
        }
        return false;
    }

}
