//
// Created by kiber_god on 09.08.2023.
//
#include <string>
#include <android/log.h>
#include "Category.h"


// Конструктор, що використовується для створення тестових записів
Category::Category(const int id, const char* name, const int icon_id) {
    strncpy(this->name, name, MAX_NAME_LENGTH - 1);
    this->name[MAX_NAME_LENGTH - 1] = '\0';
    this->id = id;
    this->icon_id = icon_id;
}

// Друк запису у лог
void Category::printLog() {
    __android_log_print(ANDROID_LOG_DEBUG, "cpp_debug", "Name: %s", name);
}

const int Category::getId() const {
    return id;
}

const char* Category::getName() const {
    return name;
}

const int Category::getIconId() const {
    return icon_id;
}