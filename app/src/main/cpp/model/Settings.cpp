//
// Created by kiber_god on 06.09.2023.
//
#include <string>
#include "Settings.h"

const std::string Settings::DEFAULT_PASSWORD = "7-.93";

Settings::Settings() {
    activityProtection = DEFAULT_ACTIVITY_PROTECTION;
    inputCalcClearing = DEFAULT_INPUT_CALC_CLEARING;

    strncpy(password, Settings::DEFAULT_PASSWORD.c_str(), MAX_PASSWORD_LENGTH);
    password[MAX_PASSWORD_LENGTH - 1] = '\0';
}

Settings::Settings(const bool activityProtection, const bool inputCalcClearing, const char* password) {
    this->activityProtection = activityProtection;
    this->inputCalcClearing = inputCalcClearing;

    strncpy(this->password, password, MAX_PASSWORD_LENGTH - 1);
    this->password[MAX_PASSWORD_LENGTH - 1] = '\0';
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