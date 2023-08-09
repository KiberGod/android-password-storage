//
// Created by kiber_god on 09.08.2023.
//
#include <string>
#include "Category.h"

// Конструктор, що використовується програмою під час парсингу бінарного файлу даних
Category::Category() {
    name[0] = '\0';
}

// Конструктор, що використовується для створення тестових записів
Category::Category(const char* name) {
    strncpy(this->name, name, MAX_NAME_LENGTH - 1);
    this->name[MAX_NAME_LENGTH - 1] = '\0';
}

const char* Category::getName() const {
    return name;
}