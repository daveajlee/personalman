import { User } from "../models/user.model";
import { UserAccountStatus } from "../models/useraccountstatus.enum";
import { UserRequest } from "../requests/user.request";
import { UserResponse } from "../responses/user.response";
import { UserHistoryResponse } from "../responses/userhistory.response";
import { UserHistoryEntry } from "../models/userhistory.entry";
import { UserHistoryReason } from "../models/userhistoryreason.enum";

export class UserUtils {

    /**
     * This method converts a UserRequest object into a User object which can be saved in the database.
     * @param userRequest a <code>UserRequest</code> object to convert
     * @param startDate a <code>Date</code> with the already converted start date.
     * @param dateOfBirth a <code>Date</code> with the already converted date of birth.
     * @return a <code>User</code> object.
     */
    static convertUserRequestToUser (userRequest: UserRequest, startDate: Date, dateOfBirth: Date ): User {
        return new User(userRequest.getFirstName(), userRequest.getSurname(), userRequest.getLeaveEntitlementPerYear(),
            userRequest.getPosition(), startDate, userRequest.getUsername(), userRequest.getPassword(), userRequest.getCompany(),
            userRequest.getWorkingDays(), userRequest.getRole(), dateOfBirth, UserAccountStatus.ACTIVE);
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
            user.getContractedHoursPerWeek(), user.getTrainingsList(), UserUtils.convertToUserHistoryResponse(user.getUserHistoryEntryList()));
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
                userHistoryEntry.getUserHistoryReason()));
        });
        return userHistoryResponses;
    }

    static userHistoryReasonFromString(userHistoryReason: string) {
            switch ( userHistoryReason ) {
              case "Joined": 
                return UserHistoryReason.JOINED;
              case "Paid":
                return UserHistoryReason.PAID;
              case "Evaluated":
                return UserHistoryReason.EVALUATED;
              case "Warned":
                return UserHistoryReason.WARNED;
              case "Resigned":
                return UserHistoryReason.RESIGNED;
              case "Sacked":
                return UserHistoryReason.SACKED;
              default:
                return null;
            }
        }

}