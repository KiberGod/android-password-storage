//
// Created by kiber_god on 09.08.2023.
//

#include <string>
#include "create_categories.h"
#include "../model/Category.h"
#include "../file_utils/BinFileIO.h"

void create_categories::insert(const int id, char* name, const int icon_id) {

    Category category(id, name, icon_id);
    writeToBinFile(getCategoriesFilePath(),
                   reinterpret_cast<char*>(&category),
                   sizeof(category),
                   sizeof(Category)
    );
}

void create_categories::runMigrations() {
    insert(-4, "Google",Category::NULL_ICON_ID_VALUE);
    insert(-3,"sites",Category::NULL_ICON_ID_VALUE);
    insert(-2,"social media",Category::NULL_ICON_ID_VALUE);
    insert(-1,"messengeres",Category::NULL_ICON_ID_VALUE);

    // insert("");
}

void create_categories::dropMigrations() {
    dropFile(getCategoriesFilePath());
}

void create_categories::refreshMigrations() {
    dropMigrations();
    runMigrations();
}