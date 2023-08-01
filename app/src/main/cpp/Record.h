//
// Created by kiber_god on 29.07.2023.
//

#ifndef PASSWORD_STORAGE_RECORD_H
#define PASSWORD_STORAGE_RECORD_H

/*
 * Клас, що зберігає структуру записів сховища на стороні С++
 */

class Record {
private:
    static const int MAX_TITLE_LENGTH = 20;
    static const int MAX_TEXT_LENGTH = 100;
    static const int MAX_CATEGORY_LENGTH = 20;

    char title[MAX_TITLE_LENGTH];
    char text[MAX_TEXT_LENGTH];
    char category[MAX_CATEGORY_LENGTH];

public:
    Record();
    Record(const char* title, const char* text, const char* category);
    void printLog();
    const char* getTitle() const;
    const char* getText() const;
    const char* getCategory() const;
};


#endif //PASSWORD_STORAGE_RECORD_H
