//
// Created by kiber_god on 21.09.2023.
//

#ifndef PASSWORD_STORAGE_DIGITALOWNER_H
#define PASSWORD_STORAGE_DIGITALOWNER_H


class DigitalOwner {
private:
    static const int DEFAULT_DAY_TRIGGERING = 0;
    static const int DEFAULT_MONTH_TRIGGERING = 0;
    static const int DEFAULT_YEAR_TRIGGERING = 0;
    static const int DEFAULT_MODE = 0;

    int dayTriggering;
    int monthTriggering;
    int yearTriggering;
    int mode;

public:
    DigitalOwner();
    DigitalOwner(const int dayTriggering, const int monthTriggering, const int yearTriggering, const int mode);
    const int getDayTriggering() const;
    const int getMonthTriggering() const;
    const int getYearTriggering() const;
    const int getMode() const;
};


#endif //PASSWORD_STORAGE_DIGITALOWNER_H