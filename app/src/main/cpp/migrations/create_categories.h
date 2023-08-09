//
// Created by kiber_god on 09.08.2023.
//

#ifndef PASSWORD_STORAGE_CREATE_CATEGORIES_H
#define PASSWORD_STORAGE_CREATE_CATEGORIES_H

/*
    Класс міграцій категорій
 */

class create_categories {
private:
    // Вставка даних
    static void insert(char* name);

    // Запуск міграцій
    static void runMigrations();

    // Видалення міграцій
    static void dropMigrations();

public:
    // Оновлення міграцій
    static void refreshMigrations(std::string files_path);
};

// Шлях до тестового bin-файла
static std::string pathToCategoriesBinFile;

//Назва тестового bin-файла
static const std::string CATEGORIES_BIN_FILE = "/categories.bin";

#endif //PASSWORD_STORAGE_CREATE_CATEGORIES_H
