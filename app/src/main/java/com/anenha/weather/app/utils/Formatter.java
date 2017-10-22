package com.anenha.weather.app.utils;

import android.content.Context;
import com.anenha.weather.R;
import com.anenha.weather.app.model.Condition;
import com.anenha.weather.app.model.Location;
import com.anenha.weather.app.provider.TranslateCallback;
import com.anenha.weather.app.provider.TranslateService;

/**
 * Created by ajnen on 12/10/2017.
 */

public class Formatter {

    public static String location(final Location location) {
        return location.getCity() + ", " + location.getCountry();
    }

    public static String temperature(final Integer temperture, final String tempUnit) {
        switch (tempUnit) {
            case "1": return temperture + "\u00B0" + "C";
            case "2": return celsiusToFahrenheit(temperture);
            default: return temperture + "\u00B0" + "C";
        }
    }

    public static String time(final String hour, final String timeFormat) {
        switch (timeFormat) {
            case "1": return getTime(hour);
            case "2": return hour;
            default: return hour;
        }
    }

    public static String weekDay(final Context context, final String weekDay) {
        return getWeekday(context, weekDay, true);
    }

    public static String date(final Context context, final String day, final String date, final boolean monthInitials, final boolean hasYear) {
        return getDate(context, day, date, monthInitials, hasYear);
    }

    public static String conditionDescription(final Context context, final Condition condition){
        final String[] conditionsArray = context.getResources().getStringArray(R.array.condition_descriptions);
        final Integer code = Integer.parseInt(condition.getCode());
        if (code >= 0 && code <= 47) {
            return conditionsArray[code];
        } else {
            return context.getString(R.string.condition_unavailable);
        }
    }

    private static String celsiusToFahrenheit(final Integer celsusTemp){
        return (celsusTemp *9/5 + 32)+ "\u00B0" + "F";
    }

    private static String getTime(String time) {
        String[] hour = time.split(" ");

        if(hour[1].equalsIgnoreCase("AM")){
            return "0" + hour[0];
        } else {
            String[] time_PM = hour[0].split(":");
            int afternoon = Integer.parseInt(time_PM[0]) + 12;
            return afternoon + ":" + time_PM[1];
        }
    }

    private static String getDate(final Context context, final String day, final String date, final boolean monthInitials,  final boolean hasYear){
        String formatedDate = "";

        if(day != null && !day.isEmpty()){
            formatedDate = getWeekday(context, day, false) + ", ";
        }

        final String[] finalDate = date.split(" ");
        if(Integer.parseInt(finalDate[0]) < 10){ finalDate[0] = "0"+finalDate[0]; }
        finalDate[1] = getMonth(context, finalDate[1], monthInitials);
        formatedDate = formatedDate + finalDate[0] + " " + finalDate[1];

        if(hasYear){ formatedDate = formatedDate + " " + finalDate[2]; }
        return formatedDate;
    }

    private static String getWeekday(final Context context, final String weekday, final boolean initials){
        final String[] weekdays = context.getResources().getStringArray(R.array.weekdays);
        String day = "";

        switch (weekday){
            case "Sun": day = weekdays[0]; break;
            case "Mon": day = weekdays[1]; break;
            case "Tue": day = weekdays[2]; break;
            case "Wed": day = weekdays[3]; break;
            case "Thu": day = weekdays[4]; break;
            case "Fri": day = weekdays[5]; break;
            case "Sat": day = weekdays[6]; break;
        }

        if(initials){ return day.substring(0, 3); }
        return day;
    }

    private static String getMonth(final Context context, final String key, final boolean initials){
        final String[] months = context.getResources().getStringArray(R.array.months);
        String month = "";

        switch (key){
            case "Jan": month = months[0]; break;
            case "Feb": month = months[1]; break;
            case "Mar": month = months[2]; break;
            case "Apr": month = months[3]; break;
            case "May": month = months[4]; break;
            case "Jun": month = months[5]; break;
            case "Jul": month = months[6]; break;
            case "Aug": month = months[7]; break;
            case "Sep": month = months[8]; break;
            case "Oct": month = months[9]; break;
            case "Nov": month = months[10]; break;
            case "Dec": month = months[11]; break;
        }

        if(initials){ return month.substring(0, 3); }
        return month;
    }
}
