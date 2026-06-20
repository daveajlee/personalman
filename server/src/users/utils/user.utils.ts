import { User } from "../models/user.model";
import { UserAccountStatus } from "../models/useraccountstatus.enum";
import { UserRequest } from "../requests/user.request";
import { UserResponse } from "../responses/user.response";
import { UserHistoryResponse } from "../responses/userhistory.response";
import { UserHistoryEntry } from "../models/userhistory.entry";

export class UserUtils {

    /**
     * This method converts a UserRequest object into a User object which can be saved in the database.
     * @param userRequest a <code>UserRequest</code> object to convert
     * @param startDate a <code>LocalDate</code> with the already converted start date.
     * @return a <code>User</code> object.
     */
    static convertUserRequestToUser (userRequest: UserRequest, startDate: Date ): User {
        return new User(userRequest.getFirstName(), userRequest.getSurname(), userRequest.getLeaveEntitlementPerYear(),
            userRequest.getPosition(), startDate, userRequest.getUsername(), userRequest.getPassword(), userRequest.getCompany(),
            userRequest.getWorkingDays(), userRequest.getRole(), userRequest.getDateOfBirth(), UserAccountStatus.ACTIVE);
    }

    /**
     * This method converts a User object into a UserResponse object for the RestAPI.
     * @param user a <code>User</code> object to convert
     * @return a converted <code>UserResponse</code> object.
     */
    static convertUserToUserResponse (user: User ): UserResponse {
        return new UserResponse(user.getFirstName(), user.getLastName(), user.getUsername(), user.getCompany(), user.getLeaveEntitlementPerYear(),
            user.getWorkingDays().join(","), user.getPosition(), user.getStartDate().toDateString(), user.getEndDate().toDateString(), 
            user.getRole(), user.getDateOfBirth().toDateString(), user.getHourlyWage(),
            user.getContractedHoursPerWeek(), user.getTrainingsList(), this.convertToUserHistoryResponse(user.getUserHistoryEntryList()));
    }

    /**
     * Convert the supplied list of <code>UserHistoryEntry</code> objects into Response objects which can be returned by the API
     * @param userHistoryEntryList a code>UserHistoryEntry</code> list of objects to convert
     * @return a <code>UserHistoryResponse</code> list of converted objects
     */
    static convertToUserHistoryResponse (userHistoryEntryList: UserHistoryEntry[] ): UserHistoryResponse[] {
        let userHistoryResponses: UserHistoryResponse[] = [];
        if ( userHistoryEntryList == null ) {
            return userHistoryResponses;
        }
        userHistoryEntryList.forEach(userHistoryEntry => {
            userHistoryResponses.push(new UserHistoryResponse(userHistoryEntry.getDate().toDateString(), userHistoryEntry.getComment(),
                userHistoryEntry.getUserHistoryReason().getText()));
        });
        return userHistoryResponses;
    }

}