//
// Created by kiber_god on 13.12.2023.
//

#ifndef PASSWORD_STORAGE_DATETIME_H
#define PASSWORD_STORAGE_DATETIME_H

/*
 * Даний клас зберігає в собі часову мітку (дату + час)
 */
class DateTime {
private:
    int year;
    int month;
    int day;
    int hours;
    int minutes;

public:
    DateTime();
    DateTime(const int year, const int month, const int day, const int hours, const int minutes);
    const int getYear() const;
    const int getMonth() const;
    const int getDay() const;
    const int getHours() const;
    const int getMinutes() const;
};


#endif //PASSWORD_STORAGE_DATETIME_H