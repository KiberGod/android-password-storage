//
// Created by kiber_god on 29.07.2023.
//

#ifndef PASSWORD_STORAGE_RECORD_H
#define PASSWORD_STORAGE_RECORD_H

/*
 * Клас, що зберігає структуру записів сховища на стороні С++
 */

class Record {
public:
    /*
     * Клас поля
     */
    class Field {
    private:
        static const int MAX_NAME_LENGTH = 20;
        static const int MAX_VALUE_LENGTH = 80;
        static const bool DEFAULT_VALUE_VISIBILITY = false;

        char name[MAX_NAME_LENGTH];
        char value[MAX_VALUE_LENGTH];
        bool valueVisibility;

    public:
        Field();
        Field(const char* name, const char* value, const bool valueVisibility);
        const char* getName() const;
        const char* getValue() const;
        const bool getValueVisibility() const;
    };

private:

    static const int MAX_FIELDS_LENGTH = 10;
    static const int MAX_TITLE_LENGTH = 20;
    static const int MAX_TEXT_LENGTH = 100;
    static const int MAX_ICON_ID_LENGTH = 35;

    Field fields[MAX_FIELDS_LENGTH];
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
    char icon_id[MAX_ICON_ID_LENGTH];

public:
    static const int NULL_CATEGORY_VALUE = -1;
    static const bool NULL_BOOKMARK_VALUE = false;

    Record();
    Record(const char* title, const char* text, const int category_id, const bool bookmark, const char* icon_id, const Field* fields);
    void printLog();
    const Field* getFields() const;
    const char* getTitle() const;
    const char* getText() const;
    const int getCategoryId() const;
    const bool getBookmark() const;
    const char* getIconId() const;

    static const int getMaxFields();
};


#endif //PASSWORD_STORAGE_RECORD_H
