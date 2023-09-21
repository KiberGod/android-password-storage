package com.example.passwordstorage.model;

/*
 *  Клас "Цифрового власника"
 */
public class DigitalOwner {

    private static final int PASSIVE_MODE = 0;          // Режим бездії
    private static final int HIDE_MODE = 1;             // Режим приховування даних
    private static final int PROTECTED_MODE = 2;        // Режим захисту входу
    private static final int DATA_DELETION_MODE = 3;    // Режим знищення даних

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

    public void setDateLastVisit(int dayLastVisit, int monthLastVisit, int yearLastVisit) {
        this.dayLastVisit = dayLastVisit;
        this.monthLastVisit = monthLastVisit;
        this.yearLastVisit = yearLastVisit;
    }

    public void setNumberDaysBeforeTriggering(int numberDaysBeforeTriggering) {
        this.numberDaysBeforeTriggering = numberDaysBeforeTriggering;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

}
