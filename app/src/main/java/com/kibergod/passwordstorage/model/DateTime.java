package com.kibergod.passwordstorage.model;

import java.util.Calendar;

/*
 * Даний клас зберігає в собі часову мітку (дату + час)
 */
public class DateTime {
    private int year;
    private int month;
    private int day;
    private int hours;
    private int minutes;

    public DateTime() {
        this.update();
    }

    public DateTime(int year, int month, int day, int hours, int minutes) {
        edit(year, month, day, hours, minutes);
    }

    public void update() {
        Calendar calendar = Calendar.getInstance();

        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH) + 1;
        day = calendar.get(Calendar.DAY_OF_MONTH);
        hours = calendar.get(Calendar.HOUR_OF_DAY);
        minutes = calendar.get(Calendar.MINUTE);
    }

    public void edit(int year, int month, int day, int hours, int minutes) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.hours = hours;
        this.minutes = minutes;
    }

    public static String correctedShortValue(int value) {
        if (value < 10) {
            return "0" + value;
        }
        return Integer.toString(value);
    }

    public String getTimestamp() {
        return correctedShortValue(day) + "." +
                correctedShortValue(month) + "." +
                year + " " +
                correctedShortValue(hours) + ":" +
                correctedShortValue(minutes);
    }

    public long getDateTimeInMilliseconds() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, day, hours, minutes);
        return calendar.getTimeInMillis();
    }

    public boolean isEmpty() {
        if (year+month+day+hours+minutes == 0) {
            return true;
        }
        return false;
    }

    public static String getDifferenceTimestamp(DateTime dateTime) {
        Calendar currentTime = Calendar.getInstance();
        Calendar targetTime = Calendar.getInstance();
        targetTime.set(dateTime.year, dateTime.month, dateTime.day, dateTime.hours, dateTime.minutes);

        long timeDifferenceMillis = targetTime.getTimeInMillis() - currentTime.getTimeInMillis();
        long days = timeDifferenceMillis / (24 * 60 * 60 * 1000);
        long hours = (timeDifferenceMillis % (24 * 60 * 60 * 1000)) / (60 * 60 * 1000);
        long minutes = (timeDifferenceMillis % (60 * 60 * 1000)) / (60 * 1000);

        return days + " днів " + hours + " годин " + minutes + " хвилин";
    }

    public static boolean timeToDelete(DateTime dateTime) {
        Calendar currentTime = Calendar.getInstance();
        Calendar targetTime = Calendar.getInstance();
        targetTime.set(dateTime.year, dateTime.month, dateTime.day, dateTime.hours, dateTime.minutes);

        return targetTime.getTimeInMillis() <= currentTime.getTimeInMillis();
    }
}
