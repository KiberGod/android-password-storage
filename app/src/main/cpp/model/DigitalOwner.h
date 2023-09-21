//
// Created by kiber_god on 21.09.2023.
//

#ifndef PASSWORD_STORAGE_DIGITALOWNER_H
#define PASSWORD_STORAGE_DIGITALOWNER_H


class DigitalOwner {
private:
    static const int DEFAULT_DAY_LAST_VISIT = 0;
    static const int DEFAULT_MONTH_LAST_VISIT = 0;
    static const int DEFAULT_YEAR_LAST_VISIT = 0;
    static const int DEFAULT_NUMBER_DAYS_BEFORE_TRIGGERING = 0;
    static const int DEFAULT_MODE = 0;

    int dayLastVisit;
    int monthLastVisit;
    int yearLastVisit;
    int numberDaysBeforeTriggering;
    int mode;

public:
    DigitalOwner();
    DigitalOwner(const int dayLastVisit, const int monthLastVisit, const int yearLastVisit, const int numberDaysBeforeTriggering, const int mode);
    const int getDayLastVisit() const;
    const int getMonthLastVisit() const;
    const int getYearLastVisit() const;
    const int getNumberDaysBeforeTriggering() const;
    const int getMode() const;
};


#endif //PASSWORD_STORAGE_DIGITALOWNER_H
