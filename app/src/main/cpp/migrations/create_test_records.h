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
    static void insert(char* title, char* text, int category_id, bool bookmark);

    // Запуск міграцій
    static void runMigrations();

public:
    // Видалення міграцій
    static void dropMigrations();

    // Оновлення міграцій
    static void refreshMigrations();
};

#endif //PASSWORD_STORAGE_CREATE_TEST_RECORDS_H
