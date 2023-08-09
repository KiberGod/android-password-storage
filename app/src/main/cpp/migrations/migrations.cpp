//
// Created by kiber_god on 30.07.2023.
//
#include <string>
#include "migrations.h"
#include "create_test_records.h"

// Перезавантаження міграцій
void refreshMigrations(std::string files_path) {
    create_test_records::refreshMigrations(files_path);
}