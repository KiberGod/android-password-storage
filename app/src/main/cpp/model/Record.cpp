//
// Created by kiber_god on 29.07.2023.
//
#include <android/log.h>
#include <string>
#include "Record.h"

// Конструктор, що використовується програмою під час парсингу бінарного файлу даних
Record::Record() {
    title[0] = '\0';
    text[0] = '\0';
    category_id = NULL;
}

// Конструктор, що використовується для створення тестових записів
Record::Record(const char* title, const char* text, const int category_id) {

    strncpy(this->title, title, MAX_TITLE_LENGTH - 1);
    this->title[MAX_TITLE_LENGTH - 1] = '\0';

    strncpy(this->text, text, MAX_TEXT_LENGTH - 1);
    this->text[MAX_TEXT_LENGTH - 1] = '\0';

    this->category_id = category_id;
}

// Друк запису у лог
void Record::printLog() {
    __android_log_print(ANDROID_LOG_DEBUG,
                        "cpp_debug",
                        "Title: %.20s | Text: %.100s | Category: %.20s",
                        title, text, category_id);
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