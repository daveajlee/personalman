package de.davelee.personalman.server.utils;

import de.davelee.personalman.api.UserRequest;
import de.davelee.personalman.api.UserResponse;
import de.davelee.personalman.server.model.User;

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
        User user = new User();
        user.setFirstName(userRequest.getFirstName());
        user.setLastName(userRequest.getSurname());
        user.setLeaveEntitlementPerYear(userRequest.getLeaveEntitlementPerYear());
        user.setPosition(userRequest.getPosition());
        user.setStartDate(startDate);
        user.setUserName(userRequest.getUsername());
        user.setCompany(userRequest.getCompany());
        user.setWorkingDays(convertToDayOfWeek(userRequest.getWorkingDays()));
        return user;
    }

    /**
     * This method converts a User object into a UserResponse object for the RestAPI.
     * @param user a <code>User</code> object to convert
     * @return a converted <code>UserResponse</code> object.
     */
    public static UserResponse convertUserToUserResponse (final User user ) {
        UserResponse userResponse = new UserResponse();
        userResponse.setFirstName(user.getFirstName());
        userResponse.setLeaveEntitlementPerYear(user.getLeaveEntitlementPerYear());
        userResponse.setPosition(user.getPosition());
        userResponse.setStartDate(DateUtils.convertLocalDateToDate(user.getStartDate()));
        userResponse.setSurname(user.getLastName());
        userResponse.setUsername(user.getUserName());
        userResponse.setCompany(user.getCompany());
        userResponse.setWorkingDays(convertFromDayOfWeek(user.getWorkingDays()));
        return userResponse;
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
        String days = "";
        for ( DayOfWeek dayOfWeek : daysOfWeekList) {
            switch ( dayOfWeek ) {
                case MONDAY:
                    days += "Monday,";
                    break;
                case TUESDAY:
                    days += "Tuesday,";
                    break;
                case WEDNESDAY:
                    days += "Wednesday,";
                    break;
                case THURSDAY:
                    days += "Thursday,";
                    break;
                case FRIDAY:
                    days += "Friday";
                    break;
                case SATURDAY:
                    days += "Saturday";
                    break;
                case SUNDAY:
                    days += "Sunday";
                    break;
            }
        }
        return days;
    }


}
