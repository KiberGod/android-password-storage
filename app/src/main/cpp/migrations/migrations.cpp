//
// Created by kiber_god on 30.07.2023.
//
#include <string>
#include "migrations.h"
#include "create_categories.h"
#include "create_test_records.h"

// Перезавантаження міграцій
void refreshMigrations() {
    create_categories::refreshMigrations();
    create_test_records::refreshMigrations();
}