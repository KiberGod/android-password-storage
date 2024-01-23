//
// Created by kiber_god on 09.08.2023.
//
#include <string>
#include "Category.h"


// Конструктор, що використовується для створення тестових записів
Category::Category(const int id, const char* name, const char* icon_id, const DateTime created_at, const DateTime updated_at, const DateTime viewed_at) {
    strncpy(this->name, name, MAX_NAME_LENGTH - 1);
    this->name[MAX_NAME_LENGTH - 1] = '\0';

    this->id = id;

    strncpy(this->icon_id, icon_id, MAX_ICON_ID_LENGTH - 1);
    this->icon_id[MAX_ICON_ID_LENGTH - 1] = '\0';

    this->created_at = created_at;
    this->updated_at = updated_at;
    this->viewed_at = viewed_at;
}

const int Category::getId() const {
    return id;
}

const char* Category::getName() const {
    return name;
}

const char* Category::getIconId() const {
    return icon_id;
}

const DateTime Category::getCreated_at() const {
    return created_at;
}

const DateTime Category::getUpdated_at() const {
    return updated_at;
}

const DateTime Category::getViewed_at() const {
    return viewed_at;
}