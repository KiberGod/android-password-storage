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

    char title[MAX_TITLE_LENGTH];
    char text[MAX_TEXT_LENGTH];

    /*
     * Відсутність значення у даному полі слід позначати як NULL_CATEGORY_VALUE (-1)
     */
    int category_id;

    /*
     *  True - запис додано до закладок
     *  false - запис не додано до закладок
     */
     bool bookmark;

public:
    static const int NULL_CATEGORY_VALUE = -1;
    static const bool NULL_BOOKMARK_VALUE = false;

    Record();
    Record(const char* title, const char* text, const int category_id, const bool bookmark);
    void printLog();
    const char* getTitle() const;
    const char* getText() const;
    const int getCategoryId() const;
    const bool getBookmark() const;
};


#endif //PASSWORD_STORAGE_RECORD_H
