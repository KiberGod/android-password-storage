//
// Created by kiber_god on 30.07.2023.
//

#ifndef PASSWORD_STORAGE_MIGRATIONS_H
#define PASSWORD_STORAGE_MIGRATIONS_H

// Тестовий запис даних до бінарного файла (дописування об`єкта класу Запису)
void testWriteToBinFile(char* title, char* text, char* category);

// Запуск міграцій
void runMigrations();

// Видалення міграцій
void dropMigrations();

// Перезавантаження міграцій
void refreshMigrations(std::string files_path);

#endif //PASSWORD_STORAGE_MIGRATIONS_H
