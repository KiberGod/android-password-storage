package com.kibergod.passwordstorage.model;


/*
 *  Клас "Цифрового власника"
 */
public class DigitalOwner {

    public static final int PASSIVE_MODE = 0;          // Режим бездії
    public static final int HIDE_MODE = 1;             // Режим приховування даних
    public static final int PROTECTED_MODE = 2;        // Режим захисту входу
    public static final int DATA_DELETION_MODE = 3;    // Режим знищення даних

    public static final int DEFAULT_DAY_TRIGGERING = 0;
    public static final int DEFAULT_MONTH_TRIGGERING = 0;
    public static final int DEFAULT_YEAR_TRIGGERING = 0;

    public static boolean retrieveRecords = false;

    private int dayTriggering;
    private int monthTriggering;
    private int yearTriggering;

    private int mode;

    public DigitalOwner(int dayTriggering, int monthTriggering, int yearTriggering, int mode) {
        this.dayTriggering = dayTriggering;
        this.monthTriggering = monthTriggering;
        this.yearTriggering = yearTriggering;
        this.mode = mode;
    };

    public int getDayTriggering() { return dayTriggering; }
    public int getMonthTriggering() { return monthTriggering; }
    public int getYearTriggering() { return yearTriggering; }

    public int getMode() { return mode; }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public void setDate(int day, int month, int year) {
        this.dayTriggering = day;
        this.monthTriggering = month;
        this.yearTriggering = year;
    }

    public void resetSettings() {
        this.dayTriggering = DEFAULT_DAY_TRIGGERING;
        this.monthTriggering = DEFAULT_MONTH_TRIGGERING;
        this.yearTriggering = DEFAULT_YEAR_TRIGGERING;
        this.mode = PASSIVE_MODE;
    }
}