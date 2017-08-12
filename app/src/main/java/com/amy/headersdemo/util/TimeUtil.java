package com.amy.headersdemo.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TimeUtil {

    public static final long MILLIS2DAY = 86400000;

    /**
     * @return format like "MM月dd日   HH:mm";
     * @see #getDateFormat(String, long)
     */
    public static String getDateFormat(long currMillis) {
        final String format = "MM月dd日   HH:mm";
        return getDateFormat(format, currMillis);
    }

    /**
     * Input your format
     *
     * @param format
     * @return
     */
    public static String getDateFormat(final String format, long millis) {
        DateFormat formatter = new SimpleDateFormat(format);
        Date curDate = new Date(millis);
        return formatter.format(curDate);
    }

    public static TimeStruct getCurTimeStruct() {
        return getTimeStruct(System.currentTimeMillis());
    }

    public static TimeStruct getTimeStruct(long time) {
        final Calendar calendar = Calendar.getInstance();

        calendar.setTimeInMillis(time);

        return new TimeStruct(
                time,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH),
                calendar.get(Calendar.HOUR),
                calendar.get(Calendar.MINUTE)
        );
    }

    public static final class TimeStruct {

        public final long millis;
        public final int year;
        public final int month;
        public final int day;
        public final int hour;
        public final int min;

        public TimeStruct(long millis, int year, int month, int day, int hour, int min) {
            this.millis = millis;
            this.year = year;
            this.month = month;
            this.day = day;
            this.hour = hour;
            this.min = min;
        }

        /**
         * @param timeStruct
         * @return day difference.
         * <br/>
         * <bold>param - this</bold>
         */
        public int differenceDay(TimeStruct timeStruct) {
            long resultMillis = this.millis - timeStruct.millis;
            return (int) (resultMillis / MILLIS2DAY);
        }
    }

}
