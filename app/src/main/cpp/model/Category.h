//
// Created by kiber_god on 09.08.2023.
//

#ifndef PASSWORD_STORAGE_CATEGORY_H
#define PASSWORD_STORAGE_CATEGORY_H

#include "DateTime.h"

/*
 * Клас, що зберігає структуру категорій записів сховища на стороні С++
 */

class Category {
private:
    static const int MAX_NAME_LENGTH = 20;
    static const int MAX_ICON_ID_LENGTH = 35;

    int id;
    char name[MAX_NAME_LENGTH];
    char icon_id[MAX_ICON_ID_LENGTH];
    DateTime created_at;

public:
    static const int NULL_ICON_ID_VALUE = -1;

    Category(const int id, const char* name, const char* icon_id, const DateTime created_at);
    void printLog();
    const int getId() const;
    const char* getName() const;
    const char* getIconId() const;
    const DateTime getCreated_at() const;
};


#endif //PASSWORD_STORAGE_CATEGORY_H
