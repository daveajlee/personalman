package de.davelee.personalman.server.utils;

import de.davelee.personalman.api.UserHistoryResponse;
import de.davelee.personalman.api.UserRequest;
import de.davelee.personalman.api.UserResponse;
import de.davelee.personalman.server.model.User;
import de.davelee.personalman.server.model.UserAccountStatus;
import de.davelee.personalman.server.model.UserHistoryEntry;
import org.bson.types.ObjectId;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * This class provides utility methods for processing related to /user endpoints in the PersonalManRestController.
 * @author Dave Lee
 */
public class UserUtils {

    /**
     * This method converts a UserRequest object into a User object which can be saved in the database.
     * @param userRequest a <code>UserRequest</code> object to convert
     * @param startDate a <code>LocalDate</code> with the already converted start date.
     * @return a <code>User</code> object.
     */
    public static User convertUserRequestToUser (final UserRequest userRequest, final LocalDate startDate ) {
        return User.builder()
                .id(new ObjectId())
                .firstName(userRequest.getFirstName())
                .lastName(userRequest.getSurname())
                .leaveEntitlementPerYear(userRequest.getLeaveEntitlementPerYear())
                .position(userRequest.getPosition())
                .startDate(startDate)
                .userName(userRequest.getUsername())
                .password(userRequest.getPassword())
                .company(userRequest.getCompany())
                .workingDays(convertToDayOfWeek(userRequest.getWorkingDays()))
                .role(userRequest.getRole())
                .dateOfBirth(DateUtils.convertDateToLocalDate(userRequest.getDateOfBirth()))
                .accountStatus(UserAccountStatus.ACTIVE)
                .build();
    }

    /**
     * This method converts a User object into a UserResponse object for the RestAPI.
     * @param user a <code>User</code> object to convert
     * @return a converted <code>UserResponse</code> object.
     */
    public static UserResponse convertUserToUserResponse (final User user ) {
        return UserResponse.builder()
                .firstName(user.getFirstName())
                .surname(user.getLastName())
                .company(user.getCompany())
                .leaveEntitlementPerYear(user.getLeaveEntitlementPerYear())
                .position(user.getPosition())
                .startDate(DateUtils.convertLocalDateToDate(user.getStartDate()))
                .endDate(DateUtils.convertLocalDateToDate(user.getEndDate()))
                .username(user.getUserName())
                .workingDays(convertFromDayOfWeek(user.getWorkingDays()))
                .role(user.getRole())
                .dateOfBirth(DateUtils.convertLocalDateToDate(user.getDateOfBirth()))
                .hourlyWage(user.getHourlyWage().doubleValue())
                .contractedHoursPerWeek(user.getContractedHoursPerWeek())
                .trainings(user.getTrainingsList())
                .userHistory(convertToUserHistoryResponse(user.getUserHistoryEntryList()))
                .build();
    }

    /**
     * Convert the supplied list of <code>UserHistoryEntry</code> objects into Response objects which can be returned by the API
     * @param userHistoryEntryList a code>UserHistoryEntry</code> list of objects to convert
     * @return a <code>UserHistoryResponse</code> list of converted objects
     */
    private static List<UserHistoryResponse> convertToUserHistoryResponse (final List<UserHistoryEntry> userHistoryEntryList ) {
        List<UserHistoryResponse> userHistoryResponses = new ArrayList<>();
        if ( userHistoryEntryList == null ) {
            return userHistoryResponses;
        }
        for ( UserHistoryEntry userHistoryEntry : userHistoryEntryList ) {
            userHistoryResponses.add(UserHistoryResponse.builder()
                    .date(DateUtils.convertLocalDateToDate(userHistoryEntry.getDate()))
                    .comment(userHistoryEntry.getComment())
                    .userHistoryReason(userHistoryEntry.getUserHistoryReason().getText())
                    .build());
        }
        return userHistoryResponses;
    }

    /**
     * This private helper method converts a <code>String</code> array to a <code>DayOfWeek</code> array.
     * The Days of the week must be supplied in English.
     * @param days a comma separated <code>String</code> of days of the week to be converted.
     * @return a <code>DayOfWeek</code> array containing the converted days of week.
     */
    private static List<DayOfWeek> convertToDayOfWeek (final String days ) {
        String[] daysArray = days.split(",");
        List<DayOfWeek> daysOfWeekList = new ArrayList<>(daysArray.length);
        for ( String day : daysArray ) {
            switch ( day ) {
                case "Monday":
                    daysOfWeekList.add(DayOfWeek.MONDAY);
                    break;
                case "Tuesday":
                    daysOfWeekList.add(DayOfWeek.TUESDAY);
                    break;
                case "Wednesday":
                    daysOfWeekList.add(DayOfWeek.WEDNESDAY);
                    break;
                case "Thursday":
                    daysOfWeekList.add(DayOfWeek.THURSDAY);
                    break;
                case "Friday":
                    daysOfWeekList.add(DayOfWeek.FRIDAY);
                    break;
                case "Saturday":
                    daysOfWeekList.add(DayOfWeek.SATURDAY);
                    break;
                case "Sunday":
                    daysOfWeekList.add(DayOfWeek.SUNDAY);
                    break;
                default:
                    break;
            }
        }
        return daysOfWeekList;
    }

    /**
     * This private helper method converts a <code>DayOfWeek</code> array to a comma-separated <code>String</code>.
     * The Days of the week will be returned in English.
     * @param daysOfWeekList a <code>DayOfWeek</code> list of days of the week to be converted.
     * @return a comma-separated <code>String</code> containing the converted days of week.
     */
    private static String convertFromDayOfWeek ( final List<DayOfWeek> daysOfWeekList ) {
        StringBuilder daysBuilder = new StringBuilder();
        for ( DayOfWeek dayOfWeek : daysOfWeekList) {
            switch ( dayOfWeek ) {
                case MONDAY:
                    daysBuilder.append("Monday,");
                    break;
                case TUESDAY:
                    daysBuilder.append("Tuesday,");
                    break;
                case WEDNESDAY:
                    daysBuilder.append("Wednesday,");
                    break;
                case THURSDAY:
                    daysBuilder.append("Thursday,");
                    break;
                case FRIDAY:
                    daysBuilder.append("Friday");
                    break;
                case SATURDAY:
                    daysBuilder.append("Saturday");
                    break;
                case SUNDAY:
                    daysBuilder.append("Sunday");
                    break;
            }
        }
        return daysBuilder.toString();
    }


}
