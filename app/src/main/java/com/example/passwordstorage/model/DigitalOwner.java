package com.example.passwordstorage.model;


/*
 *  Клас "Цифрового власника"
 */
public class DigitalOwner {

    public static final int PASSIVE_MODE = 0;          // Режим бездії
    public static final int HIDE_MODE = 1;             // Режим приховування даних
    public static final int PROTECTED_MODE = 2;        // Режим захисту входу
    public static final int DATA_DELETION_MODE = 3;    // Режим знищення даних

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

}
