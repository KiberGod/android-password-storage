//
// Created by kiber_god on 30.07.2023.
//
#include <fstream>
#include <android/log.h>
#include "migrations.h"
#include "model/Record.h"
#include "crypto_core.h"

// Шлях до bin-файла
std::string pathToBinFile;

//Назва bin-файла
const std::string BIN_FILE = "/example2.bin";

// Тестовий запис даних до бінарного файла (дописування об`єкта класу Запису)
void testWriteToBinFile(char* title, char* text, char* category) {

    Record record(title, text, category);

    std::ofstream file;
    file.open(pathToBinFile,std::ofstream::app);

    if(!file.is_open()) {
        __android_log_print(ANDROID_LOG_DEBUG, "cpp_debug", "ERROR WRITE2 BIN-FILE");
    } else {

        // Отримання показника на дані об`єкта record
        char* data = reinterpret_cast<char*>(&record);

        std::size_t dataSize = sizeof(record);

        // XOR-шифрування
        encryptData(data, dataSize);

        file.write((char *)&record, sizeof(Record));
        __android_log_print(ANDROID_LOG_DEBUG, "cpp_debug", "SUCCESSFUL WRITE2 BIN-FILE");
    };
}

// Запуск міграцій
void runMigrations() {
    testWriteToBinFile("My pass note1", "main text YHINUYH78yjhi7", "google");
    testWriteToBinFile("My pass note2", "F9fg9dfe76u", "google");
    testWriteToBinFile("Якась назва", "якийсь пароль", "категорія");
    testWriteToBinFile("12234", "йцукен123", "пріват 24");
    testWriteToBinFile("запис", "текст", "6563");
    testWriteToBinFile("MAIN PASS", "MY MAIN PASS: 1111", "google");
    testWriteToBinFile("site", "loooooooooooooooooooooooooooooong teeeeeeeeeeeeext", "sites");

    testWriteToBinFile("privat", "123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890", "baaaaaaaaank");
    testWriteToBinFile("uiyuy", "rgehyrfjuylkoiu;io'piluykiulouipwfx4e5g64hf56h   ;io", "5645654");
    testWriteToBinFile("olx", "pppp qqqqq wwwww eeeee rrrrr ttttt yyyyy", "th rrgheff fefe");
    testWriteToBinFile("mail", "--", "");
    testWriteToBinFile("www", "LINK LINK LINK", "site");
    testWriteToBinFile("mono", "code 123142453456", "bank");
    testWriteToBinFile("протон", "пароль, логін і т.д.", "пошта");
    testWriteToBinFile("стім", "код підтвердження: 1111999222", "грульки");
    testWriteToBinFile("insta", "ololo", "");

    // testWriteToBinFile("", "", "");
}

// Видалення міграцій
void dropMigrations() {
    std::ifstream file(pathToBinFile);
    if (file.good()) {
        file.close();
        remove(pathToBinFile.c_str());
    }
}

// Перезавантаження міграцій
void refreshMigrations(std::string files_path) {
    pathToBinFile = files_path + BIN_FILE;

    dropMigrations();
    runMigrations();
}