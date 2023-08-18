//
// Created by kiber_god on 30.07.2023.
//

#ifndef PASSWORD_STORAGE_MIGRATIONS_H
#define PASSWORD_STORAGE_MIGRATIONS_H

/*
 * У даному файлі необхідно реєструвати класи міграцій
 */

// Перезавантаження міграцій
void refreshMigrations();

// Видалення міграцій
void dropMigrations();

#endif //PASSWORD_STORAGE_MIGRATIONS_H
