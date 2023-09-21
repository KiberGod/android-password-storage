//
// Created by kiber_god on 21.09.2023.
//

#include "DigitalOwner.h"

DigitalOwner::DigitalOwner() {
    dayLastVisit = DEFAULT_DAY_LAST_VISIT;
    monthLastVisit = DEFAULT_MONTH_LAST_VISIT;
    yearLastVisit = DEFAULT_YEAR_LAST_VISIT;
    numberDaysBeforeTriggering = DEFAULT_NUMBER_DAYS_BEFORE_TRIGGERING;
    mode = DEFAULT_MODE;
}

DigitalOwner::DigitalOwner(const int dayLastVisit, const int monthLastVisit,
                           const int yearLastVisit, const int numberDaysBeforeTriggering,
                           const int mode) {
    this->dayLastVisit = dayLastVisit;
    this->monthLastVisit = monthLastVisit;
    this->yearLastVisit = yearLastVisit;
    this->numberDaysBeforeTriggering = numberDaysBeforeTriggering;
    this->mode = mode;
}

const int DigitalOwner::getDayLastVisit() const {
    return dayLastVisit;
}

const int DigitalOwner::getMonthLastVisit() const {
    return monthLastVisit;
}

const int DigitalOwner::getYearLastVisit() const {
    return yearLastVisit;
}

const int DigitalOwner::getNumberDaysBeforeTriggering() const {
    return numberDaysBeforeTriggering;
}

const int DigitalOwner::getMode() const {
    return mode;
}