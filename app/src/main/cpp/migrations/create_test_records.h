//
// Created by kiber_god on 09.08.2023.
//

#ifndef PASSWORD_STORAGE_CREATE_TEST_RECORDS_H
#define PASSWORD_STORAGE_CREATE_TEST_RECORDS_H

/*
    Класс міграцій тестових записів
 */

class create_test_records {
private:
    // Вставка даних
    static void insert(char* title, char* text, char* category);

    // Запуск міграцій
    static void runMigrations();

    // Видалення міграцій
    static void dropMigrations();

public:
    // Оновлення міграцій
    static void refreshMigrations(std::string files_path);
};

// Шлях до тестового bin-файла
static std::string pathToTestBinFile;

//Назва тестового bin-файла
static const std::string TEST_BIN_FILE = "/example2.bin";

#endif //PASSWORD_STORAGE_CREATE_TEST_RECORDS_H
