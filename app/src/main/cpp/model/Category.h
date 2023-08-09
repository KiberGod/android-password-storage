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

    char name[MAX_NAME_LENGTH];

public:
    Category();
    Category(const char* name);
    void printLog();
    const char* getName() const;
};


#endif //PASSWORD_STORAGE_CATEGORY_H
