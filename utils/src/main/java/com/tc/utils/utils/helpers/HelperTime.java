package com.tc.utils.utils.helpers;

import tk.jamun.ui.snacks.L;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import static com.tc.utils.utils.helpers.HelperTime.Constants.HYPHEN;
import static com.tc.utils.utils.helpers.HelperTime.Constants.SLASH;
import static com.tc.utils.utils.helpers.HelperTime.Constants.TIMESTAMP_DATE_END;
import static com.tc.utils.utils.helpers.HelperTime.Constants.TIMESTAMP_MONTH_START;
import static com.tc.utils.utils.helpers.HelperTime.Constants.TIMESTAMP_SEC_END;
import static com.tc.utils.utils.helpers.HelperTime.Constants.TIMESTAMP_YEAR_END;
import static com.tc.utils.utils.helpers.HelperTime.Constants.TIMESTAMP_YEAR_START;
import static com.tc.utils.utils.helpers.HelperTime.Constants.months;
import static com.tc.utils.utils.helpers.HelperTime.Constants.weekDaysSmall;

public class HelperTime {

    public interface Constants {
        int TIMESTAMP_MONTH_START = 5;
        int TIMESTAMP_MONTH_END = 7;
        int TIMESTAMP_DATE_START = 8;
        int TIMESTAMP_DATE_END = 10;
        int TIMESTAMP_YEAR_START = 0;
        int TIMESTAMP_YEAR_END = 4;
        int TIMESTAMP_HOUR_START = 11;
        int TIMESTAMP_HOUR_END = 13;
        int TIMESTAMP_MINUTE_START = 14;
        int TIMESTAMP_MINUTE_END = 16;
        int TIMESTAMP_SEC_START = 17;
        int TIMESTAMP_SEC_END = 19;
        String SLASH = "/";
        String COLON = ":";
        String TIMESTAMP = "yyyy-MM-dd HH:mm:ss";
        String[] weekDaysSmall = {"", "Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
        String HYPHEN = "-";
        String[] months = {"", "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
    }

    private static HelperTime helperTime;
    private Calendar calendar;
    private SimpleDateFormat simpleDateFormat, simpleDateFormatUTC;

    public static HelperTime get() {
        if (helperTime == null)
            helperTime = new HelperTime();
        return helperTime;
    }

    private HelperTime() {
        simpleDateFormat = new SimpleDateFormat(Constants.TIMESTAMP, Locale.getDefault());
        simpleDateFormatUTC = new SimpleDateFormat(Constants.TIMESTAMP, Locale.getDefault());
        simpleDateFormatUTC.setTimeZone(TimeZone.getTimeZone("UTC"));
        calendar = Calendar.getInstance();
    }

    public String getTimeStamp(Calendar calendar) {
        return calendar.get(Calendar.YEAR) + Constants.HYPHEN + getIntoTwo(calendar.get(Calendar.MONTH) + 1)
                + Constants.HYPHEN + getIntoTwo(calendar.get(Calendar.DAY_OF_MONTH)) + " " + getIntoTwo(calendar.get(Calendar.HOUR_OF_DAY)) +
                ":" + getIntoTwo(calendar.get(Calendar.MINUTE)) + ":" + getIntoTwo(calendar.get(Calendar.SECOND));
    }

    public String getTimeStamp() {
        return simpleDateFormat.format(new Date());
    }

    public String getTimeStampUTC() {
        return simpleDateFormatUTC.format(new Date());
    }


    public  String parseDateFromString(String entryTime) {
        int month = Integer.parseInt(entryTime.substring(TIMESTAMP_MONTH_START, Constants.TIMESTAMP_MONTH_END));
        if (month > 12) {
            month = 12;
        } else if (month < 1) {
            month = 1;
        }
        if (Integer.parseInt(entryTime.substring(Constants.TIMESTAMP_YEAR_START, Constants.TIMESTAMP_YEAR_END)) == getCurrentYear()) {
            return entryTime.substring(Constants.TIMESTAMP_DATE_START, Constants.TIMESTAMP_DATE_END) + " "
                    + months[month].substring(0, 3) + ",";
        } else
            return entryTime.substring(Constants.TIMESTAMP_DATE_START, Constants.TIMESTAMP_DATE_END) + " "
                    + months[month].substring(0, 3) + ", " + entryTime.substring(Constants.TIMESTAMP_YEAR_START, Constants.TIMESTAMP_YEAR_END);
    }



    public int getCurrentYear() {
        return calendar.get(Calendar.YEAR);
    }

    public Calendar getDateCalendar(String timeStamp) {
        Calendar calendarNew = Calendar.getInstance();
        try {
            calendarNew.setTime(simpleDateFormat.parse(timeStamp));
            return calendarNew;
        } catch (ParseException e) {
            L.logI(e.getMessage());
        }
        return calendarNew;
    }


    public String getDateInFormat(int date, int month, int year) {
        if (month > 11) {
            month = 11;
        } else if (month < 0) {
            month = 0;
        }
        return getIntoTwo(date) + " " + months[month].substring(0, 3) + ", " + year;
    }

    public String getDateInFormatToSave(int date, int month, int year) {
        return year + HYPHEN + getIntoTwo(month)
                + Constants.HYPHEN + getIntoTwo(date);
    }


    public String getDateInFormatToPost(String date) {
        if (date.length() == 10) {
            return date.substring(Constants.TIMESTAMP_DATE_START, Constants.TIMESTAMP_DATE_END)
                    + Constants.SLASH + getIntoTwo((Integer.parseInt(date.substring(TIMESTAMP_MONTH_START, Constants.TIMESTAMP_MONTH_END))))
                    + Constants.SLASH + date.substring(Constants.TIMESTAMP_YEAR_START, Constants.TIMESTAMP_YEAR_END) + " 00:00:00";
        } else {
            return date.substring(Constants.TIMESTAMP_DATE_START, Constants.TIMESTAMP_DATE_END)
                    + SLASH + getIntoTwo((Integer.parseInt(date.substring(TIMESTAMP_MONTH_START, Constants.TIMESTAMP_MONTH_END))))
                    + Constants.SLASH + date.substring(Constants.TIMESTAMP_YEAR_START, Constants.TIMESTAMP_YEAR_END) + date.substring(Constants.TIMESTAMP_DATE_END, Constants.TIMESTAMP_SEC_END);
        }
    }

    public String getDateInFormatFromPost(String date) {
        if (date.length() == 10) {
            return date.substring(6) + Constants.HYPHEN + getIntoTwo((Integer.parseInt(date.substring(3, 5))))
                    + Constants.HYPHEN + date.substring(0, 2) + " 00:00:00";
        } else {
            return date.substring(6) + Constants.HYPHEN + getIntoTwo((Integer.parseInt(date.substring(3, 5))))
                    + Constants.HYPHEN + date.substring(0, 2) + " " + date.substring(Constants.TIMESTAMP_DATE_END, Constants.TIMESTAMP_SEC_END);
        }
    }

    public String getIntoTwo(int month) {
        if (month > 9) {
            return "" + month;
        } else
            return 0 + "" + month;
    }

    public String getWithAmPm(String timestamp) {
        if (timestamp.length() < TIMESTAMP_SEC_END) {
            timestamp = "0000/00/00 " + timestamp;
        }
        int i = Integer.parseInt(timestamp.substring(Constants.TIMESTAMP_HOUR_START, Constants.TIMESTAMP_HOUR_END));
        if (i == 12) {
            return "00:" + timestamp.substring(Constants.TIMESTAMP_MINUTE_START, Constants.TIMESTAMP_MINUTE_END) + " PM";
        } else if (i >= 12) {
            int j = i - 12;
            String s;
            if (j < 10) {
                s = "0" + j;
            } else {
                s = String.valueOf(j);
            }
            return s + ":" + timestamp.substring(Constants.TIMESTAMP_MINUTE_START, Constants.TIMESTAMP_MINUTE_END) + " PM";
        } else {
            return timestamp.substring(Constants.TIMESTAMP_HOUR_START, Constants.TIMESTAMP_MINUTE_END) + " AM";
        }
    }

    public int getDateDifference(int type, Calendar latestDate, Calendar oldDate) {
        if (latestDate.compareTo(oldDate) > 0) {
            long different = latestDate.getTime().getTime() - oldDate.getTime().getTime();
            long secondsInMilli = 1000;
            if (type == Calendar.HOUR_OF_DAY) {
                long minutesInMilli = secondsInMilli * 60;
                long hoursInMilli = minutesInMilli * 60;
                return (int) (different / hoursInMilli);
            } else if (type == Calendar.SECOND) {
                return (int) (different / secondsInMilli);
            } else if (type == Calendar.MINUTE) {
                long minutesInMilli = secondsInMilli * 60;
                return (int) (different / minutesInMilli);
            } else if (type == Calendar.MONTH) {
                int diffYear = latestDate.get(Calendar.YEAR) - oldDate.get(Calendar.YEAR);
                return diffYear * 12 + latestDate.get(Calendar.MONTH) - oldDate.get(Calendar.MONTH);
            } else if (type == Calendar.YEAR) {
                return latestDate.get(Calendar.YEAR) - oldDate.get(Calendar.YEAR);
            } else {
                long minutesInMilli = secondsInMilli * 60;
                long hoursInMilli = minutesInMilli * 60;
                long daysInMilli = hoursInMilli * 24;
                return (int) (different / daysInMilli);
            }
        }
        return 0;
    }

    public String getAge(String dob) {
        if (dob.length() == 10) {
            dob = dob + " 00:00:00";
        }
        return getDateDifference(Calendar.YEAR, calendar, getDateCalendar(dob.replaceAll("/", "-"))) + " Year Old";
    }

    public String getDateFormatWithToday(String date) {
        try {
            Calendar calendarNew = Calendar.getInstance();
            date = date.substring(TIMESTAMP_YEAR_START, TIMESTAMP_SEC_END).replace("T", " ");
            calendarNew.setTime(simpleDateFormatUTC.parse(date));
            calendarNew.setTimeZone(TimeZone.getDefault());
            date = getTimeStamp(calendarNew);
            if (date.substring(TIMESTAMP_YEAR_START, TIMESTAMP_DATE_END).equals(getTimeStamp().substring(TIMESTAMP_YEAR_START, TIMESTAMP_DATE_END))) {
                return getWithAmPm(date);
            } else {
                return parseDateFromString(date) + " " + weekDaysSmall[calendarNew.get(Calendar.DAY_OF_WEEK)] + " " + getWithAmPm(date);
            }
        } catch (ParseException e) {
            return date;
        }
    }

    public String getDateFormatForChat(String date) {
        try {
            Calendar calendarNew = Calendar.getInstance();
            date = date.substring(TIMESTAMP_YEAR_START, TIMESTAMP_SEC_END).replace("T", " ");
            calendarNew.setTime(simpleDateFormatUTC.parse(date));
            calendarNew.setTimeZone(TimeZone.getDefault());
            date = getTimeStamp(calendarNew);
            if (date.substring(TIMESTAMP_YEAR_START, TIMESTAMP_DATE_END).equals(getTimeStamp().substring(TIMESTAMP_YEAR_START, TIMESTAMP_DATE_END))) {
                return getWithAmPm(date);
            } else {
                return parseDateFromString(date) + " " + getWithAmPm(date);
            }
        } catch (ParseException e) {
            return date;
        }
    }

}
