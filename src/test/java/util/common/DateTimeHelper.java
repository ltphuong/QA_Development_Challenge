package util.common;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class DateTimeHelper {

    public static String convertDateFromUnixTime(Long second, String timezone, String dateFormat){
        Date date = new Date(second * 1000L);
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        sdf.setTimeZone(TimeZone.getTimeZone(timezone));
        return sdf.format(date);
    }
}
