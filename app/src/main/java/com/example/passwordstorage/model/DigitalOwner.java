package com.example.passwordstorage.model;

import java.util.Calendar;

/*
 *  Клас "Цифрового власника"
 */
public class DigitalOwner {

    public static final int PASSIVE_MODE = 0;          // Режим бездії
    public static final int HIDE_MODE = 1;             // Режим приховування даних
    public static final int PROTECTED_MODE = 2;        // Режим захисту входу
    public static final int DATA_DELETION_MODE = 3;    // Режим знищення даних

    private int dayLastVisit;
    private int monthLastVisit;
    private int yearLastVisit;

    private int numberDaysBeforeTriggering;

    private int mode;

    public DigitalOwner(int dayLastVisit, int monthLastVisit, int yearLastVisit, int numberDaysBeforeTriggering, int mode) {
        this.dayLastVisit = dayLastVisit;
        this.monthLastVisit = monthLastVisit;
        this.yearLastVisit = yearLastVisit;
        this.numberDaysBeforeTriggering = numberDaysBeforeTriggering;
        this.mode = mode;
    };

    public int getDayLastVisit() { return dayLastVisit; }
    public int getMonthLastVisit() { return monthLastVisit; }
    public int getYearLastVisit() { return yearLastVisit; }
    public int getNumberDaysBeforeTriggering() { return numberDaysBeforeTriggering; }
    public int getMode() { return mode; }

    public void setNumberDaysBeforeTriggering(int numberDaysBeforeTriggering) {
        this.numberDaysBeforeTriggering = numberDaysBeforeTriggering;
    }

    public void setMode(int mode) {
        this.mode = mode;
        setDate();
    }

    public void setDate() {
        Calendar calendar = Calendar.getInstance();
        this.dayLastVisit = calendar.get(Calendar.DAY_OF_MONTH);
        this.monthLastVisit = calendar.get(Calendar.MONTH) + 1;
        this.yearLastVisit = calendar.get(Calendar.YEAR);
    }

}
