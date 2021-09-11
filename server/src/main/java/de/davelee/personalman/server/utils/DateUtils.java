package de.davelee.personalman.server.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * This class provides utility methods for processing related to dates in the PersonalManRestController.
 * @author Dave Lee
 */
public class DateUtils {

    private static final Logger LOG = LoggerFactory.getLogger(DateUtils.class);

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    /**
     * This method converts a string date in the format dd-mm-yyyy to a LocalDate object. If the conversion is not
     * successful then return null.
     * @param date a <code>String</code> in the form dd-mm-yyyy
     * @return a <code>LocaLDate</code> with the converted date or null if no conversion is possible.
     */
    public static LocalDate convertDateToLocalDate ( final String date ) {
        try {
            return LocalDate.parse(date, DATE_TIME_FORMATTER);
        } catch ( DateTimeParseException dateTimeParseException ) {
            LOG.error("Could not convert date: " + date);
            return null;
        }
    }

    /**
     * This method converts a LocalDate object to a string date in the format dd-mm-yyyy. If the conversion is not
     * successful then return null.
     * @param date a <code>LocalDate</code> with the date to convert
     * @return a <code>String</code> with the converted String.
     */
    public static String convertLocalDateToDate ( final LocalDate date ) {
        if ( date == null ) {
            return null;
        }
        return date.format(DATE_TIME_FORMATTER);
    }


}
