//
// Created by kiber_god on 06.09.2023.
//

#include "Settings.h"

Settings::Settings() {
    activityProtection = DEFAULT_ACTIVITY_PROTECTION;
}

Settings::Settings(bool activityProtection) {
    this->activityProtection = activityProtection;
}

const bool Settings::getActivityProtection() const {
    return activityProtection;
}
