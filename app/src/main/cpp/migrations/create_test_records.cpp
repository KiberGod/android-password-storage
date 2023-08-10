//
// Created by kiber_god on 09.08.2023.
//

#include <string>
#include "create_test_records.h"
#include "utils/MigrationUtils.h"
#include "../model/Record.h"
#include "../file_utils/BinFileIO.h"

void create_test_records::insert(char* title, char* text, char* category) {

    Record record(title, text, category);
    writeToBinFile(getTestRecordsFilePath(),
                   reinterpret_cast<char*>(&record),
                   sizeof(record),
                   sizeof(Record)
    );
}

void create_test_records::runMigrations() {
    insert("My pass note1", "main text YHINUYH78yjhi7", "google");
    insert("My pass note2", "F9fg9dfe76u", "google");
    insert("Якась назва", "якийсь пароль", "категорія");
    insert("12234", "йцукен123", "пріват 24");
    insert("запис", "текст", "6563");
    insert("MAIN PASS", "MY MAIN PASS: 1111", "google");
    insert("site", "loooooooooooooooooooooooooooooong teeeeeeeeeeeeext", "sites");

    insert("privat","123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890","baaaaaaaaank");
    insert("uiyuy", "rgehyrfjuylkoiu;io'piluykiulouipwfx4e5g64hf56h   ;io","5645654");
    insert("olx", "pppp qqqqq wwwww eeeee rrrrr ttttt yyyyy", "th rrgheff fefe");
    insert("mail", "--", "");
    insert("www", "LINK LINK LINK", "site");
    insert("mono", "code 123142453456", "bank");
    insert("протон", "пароль, логін і т.д.", "пошта");
    insert("стім", "код підтвердження: 1111999222", "грульки");
    insert("insta", "ololo", "");

    // insert("", "", "");
}

void create_test_records::dropMigrations() {
    dropFile(getTestRecordsFilePath());
}

void create_test_records::refreshMigrations() {
    dropMigrations();
    runMigrations();
}