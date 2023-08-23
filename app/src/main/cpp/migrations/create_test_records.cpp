//
// Created by kiber_god on 09.08.2023.
//

#include <string>
#include "create_test_records.h"
#include "../model/Record.h"
#include "../file_utils/BinFileIO.h"

void create_test_records::insert(char* title, char* text, int category_id, bool bookmark) {

    Record record(title, text, category_id, bookmark);
    writeToBinFile(getTestRecordsFilePath(),
                   reinterpret_cast<char*>(&record),
                   sizeof(record),
                   sizeof(Record)
    );
}

void create_test_records::runMigrations() {
    insert("My pass note1", "main text YHINUYH78yjhi7", 100, false);
    insert("My pass note2", "F9fg9dfe76u", 2, true);
    insert("Якась назва", "якийсь пароль", 1, false);
    insert("12234", "йцукен123", Record::NULL_CATEGORY_VALUE, false);
    insert("запис", "текст", 1, false);
    insert("MAIN PASS", "MY MAIN PASS: 1111", 2, false);
    insert("site", "loooooooooooooooooooooooooooooong teeeeeeeeeeeeext", 0, false);

    insert("privat","123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890",Record::NULL_CATEGORY_VALUE,
           false);
    insert("uiyuy", "rgehyrfjuylkoiu;io'piluykiulouipwfx4e5g64hf56h   ;io",3, false);
    insert("olx", "pppp qqqqq wwwww eeeee rrrrr ttttt yyyyy", 1, false);
    insert("mail", "--", 3, false);
    insert("www", "LINK LINK LINK", 0, false);
    insert("mono", "code 123142453456", 1, false);
    insert("протон", "пароль, логін і т.д.", 3, false);
    insert("стім", "код підтвердження: 1111999222", Record::NULL_CATEGORY_VALUE, true);
    insert("insta", "ololo", 0, false);

    // insert("", "", Record::NULL_CATEGORY_VALUE, false);
}

void create_test_records::dropMigrations() {
    dropFile(getTestRecordsFilePath());
}

void create_test_records::refreshMigrations() {
    dropMigrations();
    runMigrations();
}