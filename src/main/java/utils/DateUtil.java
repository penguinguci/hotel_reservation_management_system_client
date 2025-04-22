package utils;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DateUtil {
    /**
     * Parse a string into a Date object, supporting various date formats including milliseconds.
     *
     * @param dateStr The date string to parse.
     * @return The parsed Date object, or null if parsing fails.
     */
    public static Date parseDateFlexible(String dateStr) {
        if (dateStr == null || dateStr.isEmpty()) {
            throw new IllegalArgumentException("Date string cannot be null or empty");
        }

        List<String> formats = new ArrayList<>();
        formats.add("dd/MM/yyyy");               // Day, month, year
        formats.add("dd-MM-yyyy");               // Day, month, year with hyphen
        formats.add("yyyy-MM-dd");               // ISO-like format
        formats.add("dd/MM/yyyy HH:mm:ss");      // Day, month, year, time
        formats.add("dd-MM-yyyy HH:mm:ss");      // Day, month, year with time (hyphen)
        formats.add("yyyy-MM-dd HH:mm:ss");      // Full ISO-like format with time
        formats.add("yyyy-MM-dd HH:mm:ss.SSS");  // Full ISO-like format with milliseconds
        formats.add("MM/dd/yyyy HH:mm:ss");      // US-style month/day/year with time
        formats.add("yyyy/MM/dd HH:mm:ss");      // Alternative ISO style
        formats.add("dd/MM/yyyy HH:mm");         // Without seconds
        formats.add("yyyy-MM-dd'T'HH:mm:ss");    // ISO 8601
        formats.add("yyyy-MM-dd'T'HH:mm:ss.SSS");// ISO 8601 with milliseconds

        for (String format : formats) {
            try {
                SimpleDateFormat dateFormat = new SimpleDateFormat(format);
                dateFormat.setLenient(false);
                return dateFormat.parse(dateStr);
            } catch (ParseException ignored) {

            }
        }

        System.err.println("Error: No matching date format for input: " + dateStr);
        return null;
    }

    public static Date combineDateAndTime(Date date, Date time) {
        if (date == null || time == null) return null;

        Calendar dateCal = Calendar.getInstance();
        dateCal.setTime(date);

        Calendar timeCal = Calendar.getInstance();
        timeCal.setTime(time);

        dateCal.set(Calendar.HOUR_OF_DAY, timeCal.get(Calendar.HOUR_OF_DAY));
        dateCal.set(Calendar.MINUTE, timeCal.get(Calendar.MINUTE));
        dateCal.set(Calendar.SECOND, 0);
        dateCal.set(Calendar.MILLISECOND, 0);

        return dateCal.getTime();
    }

    public static Date combineDateTime(Date date, Time time) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        Calendar timeCal = Calendar.getInstance();
        timeCal.setTime(time);

        cal.set(Calendar.HOUR_OF_DAY, timeCal.get(Calendar.HOUR_OF_DAY));
        cal.set(Calendar.MINUTE, timeCal.get(Calendar.MINUTE));

        return cal.getTime();
    }

    public static Date parseDateTime(String dateTimeStr) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        return format.parse(dateTimeStr);
    }

    public static String formatDate(Date date) {
        return date != null ? new SimpleDateFormat("dd-MM-yyyy").format(date) : "";
    }

    public static String formatDateTime(Date date) {
        return date != null ? new SimpleDateFormat("dd-MM-yyyy HH:mm").format(date) : "";
    }
}
