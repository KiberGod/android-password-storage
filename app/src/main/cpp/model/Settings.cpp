//
// Created by kiber_god on 06.09.2023.
//

#include "Settings.h"

Settings::Settings() {
    activityProtection = DEFAULT_ACTIVITY_PROTECTION;
    inputPassClearing = DEFAULT_INPUT_PASS_CLEARING;
}

Settings::Settings(bool activityProtection, bool inputPassClearing) {
    this->activityProtection = activityProtection;
    this->inputPassClearing = inputPassClearing;
}

const bool Settings::getActivityProtection() const {
    return activityProtection;
}

const bool Settings::getInputPassClearing() const {
    return inputPassClearing;
}
