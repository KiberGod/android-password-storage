//
// Created by kiber_god on 09.08.2023.
//

#include <string>
#include "create_categories.h"
#include "../model/Category.h"
#include "../file_utils/BinFileIO.h"

void create_categories::insert(const int id, char* name) {

    Category category(id, name);
    writeToBinFile(getCategoriesFilePath(),
                   reinterpret_cast<char*>(&category),
                   sizeof(category),
                   sizeof(Category)
    );
}

void create_categories::runMigrations() {
    insert(-4, "Google");
    insert(-3,"sites");
    insert(-2,"social media");
    insert(-1,"messengeres");

    // insert("");
}

void create_categories::dropMigrations() {
    dropFile(getCategoriesFilePath());
}

void create_categories::refreshMigrations() {
    dropMigrations();
    runMigrations();
}