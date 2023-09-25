//
// Created by kiber_god on 21.09.2023.
//

#include "DigitalOwner.h"

DigitalOwner::DigitalOwner() {
    dayTriggering = DEFAULT_DAY_LAST_VISIT;
    monthTriggering = DEFAULT_MONTH_LAST_VISIT;
    yearTriggering = DEFAULT_YEAR_LAST_VISIT;
    mode = DEFAULT_MODE;
}

DigitalOwner::DigitalOwner(const int dayTriggering, const int monthTriggering,
                           const int yearTriggering, const int mode) {
    this->dayTriggering = dayTriggering;
    this->monthTriggering = monthTriggering;
    this->yearTriggering = yearTriggering;
    this->mode = mode;
}

const int DigitalOwner::getDayTriggering() const {
    return dayTriggering;
}

const int DigitalOwner::getMonthTriggering() const {
    return monthTriggering;
}

const int DigitalOwner::getYearTriggering() const {
    return yearTriggering;
}

const int DigitalOwner::getMode() const {
    return mode;
}