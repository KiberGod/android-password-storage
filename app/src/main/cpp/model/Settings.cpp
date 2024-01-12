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

    fontSizeMain = DEFAULT_FONT_SIZE_MAIN;
    fontSizeInput = DEFAULT_FONT_SIZE_INPUT;
    fontSizeButtons = DEFAULT_FONT_SIZE_BUTTONS;
    fontSizeLargeButtons = DEFAULT_FONT_SIZE_LARGE_BUTTONS;
    fontSizeFieldCaptions = DEFAULT_FONT_SIZE_FIELD_CAPTIONS;
    fontSizeOther = DEFAULT_FONT_SIZE_OTHER;
    fontSizeRssMain = DEFAULT_FONT_SIZE_RSS_MAIN;
    fontSizeRssSecondary = DEFAULT_FONT_SIZE_RSS_SECONDARY;
}

Settings::Settings(const bool activityProtection, const bool inputCalcClearing, const char* password,
                   const bool digitalOwner, const bool filtersSortMode, const int filtersSortParam,
                   const int fontSizeMain, const int fontSizeInput, const int fontSizeButtons, const int fontSizeLargeButtons,
                   const int fontSizeFieldCaptions, const int fontSizeOther, const int fontSizeRssMain, const int fontSizeRssSecondary) {
    this->activityProtection = activityProtection;
    this->inputCalcClearing = inputCalcClearing;
    this->digitalOwner = digitalOwner;

    strncpy(this->password, password, MAX_PASSWORD_LENGTH - 1);
    this->password[MAX_PASSWORD_LENGTH - 1] = '\0';

    this->filtersSortMode = filtersSortMode;
    this->filtersSortParam = filtersSortParam;

    this->fontSizeMain = fontSizeMain;
    this->fontSizeInput = fontSizeInput;
    this->fontSizeButtons = fontSizeButtons;
    this->fontSizeLargeButtons = fontSizeLargeButtons;
    this->fontSizeFieldCaptions = fontSizeFieldCaptions;
    this->fontSizeOther = fontSizeOther;
    this->fontSizeRssMain = fontSizeRssMain;
    this->fontSizeRssSecondary = fontSizeRssSecondary;
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

const int Settings::getFontSizeMain() const {
    return fontSizeMain;
}

const int Settings::getFontSizeInput() const {
    return fontSizeInput;
}

const int Settings::getFontSizeButtons() const {
    return fontSizeButtons;
}

const int Settings::getFontSizeLargeButtons() const {
    return fontSizeLargeButtons;
}

const int Settings::getFontSizeFieldCaptions() const {
    return fontSizeFieldCaptions;
}

const int Settings::getFontSizeOther() const {
    return fontSizeOther;
}

const int Settings::getFontSizeRssMain() const {
    return fontSizeRssMain;
}

const int Settings::getFontSizeRssSecondary() const {
    return fontSizeRssSecondary;
}