//
// Created by kiber_god on 09.08.2023.
//

#ifndef PASSWORD_STORAGE_CATEGORY_H
#define PASSWORD_STORAGE_CATEGORY_H

/*
 * Клас, що зберігає структуру категорій записів сховища на стороні С++
 */

class Category {
private:
    static const int MAX_NAME_LENGTH = 20;

    int id;
    char name[MAX_NAME_LENGTH];
    int icon_id;

public:
    static const int NULL_ICON_ID_VALUE = -1;

    Category(const int id, const char* name, const int icon_id);
    void printLog();
    const int getId() const;
    const char* getName() const;
    const int getIconId() const;
};


#endif //PASSWORD_STORAGE_CATEGORY_H
