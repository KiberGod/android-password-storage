//
// Created by kiber_god on 06.09.2023.
//
#include <string>
#include "Settings.h"

const std::string Settings::DEFAULT_PASSWORD = "7-.93";

Settings::Settings() {
    activityProtection = DEFAULT_ACTIVITY_PROTECTION;
    inputPassClearing = DEFAULT_INPUT_PASS_CLEARING;

    strncpy(password, Settings::DEFAULT_PASSWORD.c_str(), MAX_PASSWORD_LENGTH);
    password[MAX_PASSWORD_LENGTH - 1] = '\0';
}

Settings::Settings(const bool activityProtection, const bool inputPassClearing, const char* password) {
    this->activityProtection = activityProtection;
    this->inputPassClearing = inputPassClearing;

    strncpy(this->password, password, MAX_PASSWORD_LENGTH - 1);
    this->password[MAX_PASSWORD_LENGTH - 1] = '\0';
}

const bool Settings::getActivityProtection() const {
    return activityProtection;
}

const bool Settings::getInputPassClearing() const {
    return inputPassClearing;
}

const char* Settings::getPassword() const {
    return password;
}