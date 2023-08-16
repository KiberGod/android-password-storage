//
// Created by kiber_god on 09.08.2023.
//

#include <string>
#include "create_test_records.h"
#include "../model/Record.h"
#include "../file_utils/BinFileIO.h"

void create_test_records::insert(char* title, char* text, int category_id) {

    Record record(title, text, category_id);
    writeToBinFile(getTestRecordsFilePath(),
                   reinterpret_cast<char*>(&record),
                   sizeof(record),
                   sizeof(Record)
    );
}

void create_test_records::runMigrations() {
    insert("My pass note1", "main text YHINUYH78yjhi7", 1);
    insert("My pass note2", "F9fg9dfe76u", 2);
    insert("Якась назва", "якийсь пароль", 1);
    insert("12234", "йцукен123", Record::NULL_CATEGORY_VALUE);
    insert("запис", "текст", 1);
    insert("MAIN PASS", "MY MAIN PASS: 1111", 2);
    insert("site", "loooooooooooooooooooooooooooooong teeeeeeeeeeeeext", 0);

    insert("privat","123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890",Record::NULL_CATEGORY_VALUE);
    insert("uiyuy", "rgehyrfjuylkoiu;io'piluykiulouipwfx4e5g64hf56h   ;io",3);
    insert("olx", "pppp qqqqq wwwww eeeee rrrrr ttttt yyyyy", 1);
    insert("mail", "--", 3);
    insert("www", "LINK LINK LINK", 0);
    insert("mono", "code 123142453456", 1);
    insert("протон", "пароль, логін і т.д.", 3);
    insert("стім", "код підтвердження: 1111999222", Record::NULL_CATEGORY_VALUE);
    insert("insta", "ololo", 0);

    // insert("", "", Record::NULL_CATEGORY_VALUE);
}

void create_test_records::dropMigrations() {
    dropFile(getTestRecordsFilePath());
}

void create_test_records::refreshMigrations() {
    dropMigrations();
    runMigrations();
}