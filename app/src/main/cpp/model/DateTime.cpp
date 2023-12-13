//
// Created by kiber_god on 13.12.2023.
//

#include "DateTime.h"

DateTime::DateTime() {
    this->year = 0;
    this->month = 0;
    this->day = 0;
    this->hours = 0;
    this->minutes = 0;
}

DateTime::DateTime(const int year, const int month, const int day, const int hours,
                   const int minutes) {
    this->year = year;
    this->month = month;
    this->day = day;
    this->hours = hours;
    this->minutes = minutes;
}

const int DateTime::getYear() const {
    return year;
}

const int DateTime::getMonth() const {
    return month;
}

const int DateTime::getDay() const {
    return day;
}

const int DateTime::getHours() const {
    return hours;
}

const int DateTime::getMinutes() const {
    return minutes;
}