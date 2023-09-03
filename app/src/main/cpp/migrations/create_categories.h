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
    static void insert(int id, char* name, int icon_id);

    // Запуск міграцій
    static void runMigrations();

public:
    // Видалення міграцій
    static void dropMigrations();

    // Оновлення міграцій
    static void refreshMigrations();
};


#endif //PASSWORD_STORAGE_CREATE_CATEGORIES_H
