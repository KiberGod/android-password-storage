//
// Created by kiber_god on 30.07.2023.
//
#include <string>
#include "crypto_core.h"


// Кодування даних (приведених до бінарного виду) об`єктів класів моделей методом XOR
void encryptData(char* data, std::size_t size) {
    for (std::size_t i = 0; i < size; ++i) {
        data[i] = data[i] ^ SECRET_KEY;
    }
}

// Розшифровування даних (отриманих у бінарному виді з bin-файлу) об`єктів класів моделей методом XOR
void decryptData(char* data, std::size_t size) {
    for (std::size_t i = 0; i < size; ++i) {
        data[i] = data[i] ^ SECRET_KEY;
    }
}