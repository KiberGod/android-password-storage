//
// Created by kiber_god on 06.09.2023.
//
#include <string>
#include "Settings.h"

const std::string Settings::DEFAULT_PASSWORD = "7-.93";

Settings::Settings() {
    activityProtection = DEFAULT_ACTIVITY_PROTECTION;
    inputCalcClearing = DEFAULT_INPUT_CALC_CLEARING;
    digitalOwner = DEFAULT_DIGITAL_OWNER;

    strncpy(password, Settings::DEFAULT_PASSWORD.c_str(), MAX_PASSWORD_LENGTH);
    password[MAX_PASSWORD_LENGTH - 1] = '\0';

    filtersSortMode = DEFAULT_FILTERS_SORT_MODE;
    filtersSortParam = DEFAULT_FILTERS_SORT_PARAM;
}

Settings::Settings(const bool activityProtection, const bool inputCalcClearing, const char* password, const bool digitalOwner, const bool filtersSortMode, const int filtersSortParam) {
    this->activityProtection = activityProtection;
    this->inputCalcClearing = inputCalcClearing;
    this->digitalOwner = digitalOwner;

    strncpy(this->password, password, MAX_PASSWORD_LENGTH - 1);
    this->password[MAX_PASSWORD_LENGTH - 1] = '\0';

    this->filtersSortMode = filtersSortMode;
    this->filtersSortParam = filtersSortParam;
}

const bool Settings::getActivityProtection() const {
    return activityProtection;
}

const bool Settings::getInputCalcClearing() const {
    return inputCalcClearing;
}

const char* Settings::getPassword() const {
    return password;
}

const bool Settings::getDigitalOwner() const {
    return digitalOwner;
}

const bool Settings::getFiltersSortMode() const {
    return filtersSortMode;
}

const int Settings::getFiltersSortParam() const {
    return filtersSortParam;
}