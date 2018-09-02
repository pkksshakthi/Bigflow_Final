package models;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
/**
 * @author sakthivel
 */
public class Common {
    public static Date convertDate(String inputString, String inputStringFormat) {
        SimpleDateFormat format = new SimpleDateFormat(inputStringFormat);
        try {
            Date date = format.parse(inputString);
            return date;
        } catch (ParseException pr) {
            return null;
        }

    }

    public static String convertDateString(Date date, String dateFormat) {
        SimpleDateFormat format = new SimpleDateFormat(dateFormat);
        String str = format.format(date);
        return str;
    }
}
