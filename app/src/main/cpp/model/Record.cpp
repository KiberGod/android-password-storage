//
// Created by kiber_god on 29.07.2023.
//
#include <android/log.h>
#include <string>
#include "Record.h"

Record::Field::Field() {
    name[0] = '\0';
    value[0] = '\0';
    valueVisibility = DEFAULT_VALUE_VISIBILITY;
}

Record::Field::Field(const char *name, const char *value, const bool valueVisibility) {
    strncpy(this->name, name, MAX_NAME_LENGTH - 1);
    this->name[MAX_NAME_LENGTH - 1] = '\0';

    strncpy(this->value, value, MAX_VALUE_LENGTH - 1);
    this->value[MAX_VALUE_LENGTH - 1] = '\0';

    this->valueVisibility = valueVisibility;
}

const char* Record::Field::getName() const {
    return name;
}

const char* Record::Field::getValue() const {
    return value;
}

const bool Record::Field::getValueVisibility() const {
    return valueVisibility;
}

// Конструктор, що використовується програмою під час парсингу бінарного файлу даних
Record::Record() {
    id = 0;
    title[0] = '\0';
    text[0] = '\0';
    category_id = NULL_CATEGORY_VALUE;
    bookmark = NULL_BOOKMARK_VALUE;
    icon_id[0] = '\0';

    for (int i=0; i<MAX_FIELDS_LENGTH; i++) {
        fields[i] = Field();
    }

    created_at = DateTime();
    updated_at = DateTime();
    viewed_at = DateTime();
    totalValueVisibility = DEFAULT_TOTAL_VALUE_VISIBILITY;
    deleted_at = DateTime(0,0,0,0,0);
    hidden = DEFAULT_HIDDEN;
}

// Конструктор, що використовується для створення тестових записів
Record::Record(const int id, const char* title, const char* text, const int category_id, const bool bookmark, const char* icon_id, const Field* fields, const DateTime created_at,
               const DateTime updated_at, const DateTime viewed_at, bool totalValueVisibility, const DateTime deleted_at, bool hidden) {

    this->id = id;

    strncpy(this->title, title, MAX_TITLE_LENGTH - 1);
    this->title[MAX_TITLE_LENGTH - 1] = '\0';

    strncpy(this->text, text, MAX_TEXT_LENGTH - 1);
    this->text[MAX_TEXT_LENGTH - 1] = '\0';

    this->category_id = category_id;
    this->bookmark = bookmark;

    strncpy(this->icon_id, icon_id, MAX_ICON_ID_LENGTH - 1);
    this->icon_id[MAX_ICON_ID_LENGTH - 1] = '\0';

    for (int i = 0; i < MAX_FIELDS_LENGTH; i++) {
        this->fields[i] = fields[i];
    }

    this->created_at = created_at;
    this->updated_at = updated_at;
    this->viewed_at = viewed_at;
    this->totalValueVisibility = totalValueVisibility;
    this->deleted_at = deleted_at;
    this->hidden = hidden;
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

const int Record::getId() const {
    return id;
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

const char* Record::getIconId() const {
    return icon_id;
}
const DateTime Record::getCreated_at() const {
    return created_at;
}

const DateTime Record::getUpdated_at() const {
    return updated_at;
}

const DateTime Record::getViewed_at() const {
    return viewed_at;
}

const bool Record::getTotalValueVisibility() const {
    return totalValueVisibility;
}

const DateTime Record::getDeleted_at() const {
    return deleted_at;
}

const bool Record::getHidden() const {
    return hidden;
}

const int Record::getMaxFields() {
    return MAX_FIELDS_LENGTH;
}
