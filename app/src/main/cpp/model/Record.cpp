//
// Created by kiber_god on 29.07.2023.
//
#include <android/log.h>
#include <string>
#include "Record.h"

Record::Field::Field() {
    name[0] = '\0';
    value[0] = '\0';
}

Record::Field::Field(const char *name, const char *value) {
    strncpy(this->name, name, MAX_NAME_LENGTH - 1);
    this->name[MAX_NAME_LENGTH - 1] = '\0';

    strncpy(this->value, value, MAX_VALUE_LENGTH - 1);
    this->value[MAX_VALUE_LENGTH - 1] = '\0';
}

const char* Record::Field::getName() const {
    return name;
}

const char* Record::Field::getValue() const {
    return value;
}



// Конструктор, що використовується програмою під час парсингу бінарного файлу даних
Record::Record() {
    title[0] = '\0';
    text[0] = '\0';
    category_id = NULL_CATEGORY_VALUE;
    bookmark = NULL_BOOKMARK_VALUE;
    icon_id = NULL_ICON_ID_VALUE;

    for (int i=0; i<MAX_FIELDS_LENGTH; i++) {
        fields[i] = Field();
    }
}

// Конструктор, що використовується для створення тестових записів
Record::Record(const char* title, const char* text, const int category_id, const bool bookmark, const int icon_id, const Field* fields) {

    strncpy(this->title, title, MAX_TITLE_LENGTH - 1);
    this->title[MAX_TITLE_LENGTH - 1] = '\0';

    strncpy(this->text, text, MAX_TEXT_LENGTH - 1);
    this->text[MAX_TEXT_LENGTH - 1] = '\0';

    this->category_id = category_id;
    this->bookmark = bookmark;
    this->icon_id = icon_id;

    for (int i = 0; i < MAX_FIELDS_LENGTH; i++) {
        this->fields[i] = fields[i];
    }
}

// Друк запису у лог
void Record::printLog() {
    __android_log_print(ANDROID_LOG_DEBUG,
                        "cpp_debug",
                        "Title: %.20s | Text: %.100s | Category: %.20s",
                        title, text, category_id);
}

const Record::Field* Record::getFields() const {
    return fields;
}

const char* Record::getTitle() const {
    return title;
}

const char* Record::getText() const {
    return text;
}

const int Record::getCategoryId() const {
    return category_id;
}

const bool Record::getBookmark() const {
    return  bookmark;
}

const int Record::getIconId() const {
    return icon_id;
}

const int Record::getMaxFields() {
    return MAX_FIELDS_LENGTH;
}
