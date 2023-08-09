//
// Created by kiber_god on 09.08.2023.
//

#include <string>
#include "create_categories.h"
#include "utils/MigrationUtils.h"
#include "../model/Category.h"


void create_categories::insert(char* name) {

    Category category(name);
    writeToBinFile(pathToCategoriesBinFile,
                   reinterpret_cast<char*>(&category),
                   sizeof(category),
                   sizeof(Category)
    );
}

void create_categories::runMigrations() {
    insert("Google");
    insert("sites");
    insert("social media");
    insert("messengeres");

    // insert("");
}

void create_categories::dropMigrations() {
    dropFile(pathToCategoriesBinFile);
}

void create_categories::refreshMigrations(std::string files_path) {
    pathToCategoriesBinFile = files_path + CATEGORIES_BIN_FILE;

    dropMigrations();
    runMigrations();
}