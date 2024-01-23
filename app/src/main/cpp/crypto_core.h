//
// Created by kiber_god on 30.07.2023.
//

#ifndef PASSWORD_STORAGE_CRYPTO_CORE_H
#define PASSWORD_STORAGE_CRYPTO_CORE_H

// Тимчасовий секретний ключ для XOR-шифрування
const char SECRET_KEY = 'kyh78T789MY7UGTN76lOlio8iku7uGYM';

// Кодування даних (приведених до бінарного виду) об`єктів класів моделей методом XOR
void encryptData(char* data, std::size_t size);

// Розшифровування даних (отриманих у бінарному виді з bin-файлу) об`єктів класів моделей методом XOR
void decryptData(char* data, std::size_t size);

#endif //PASSWORD_STORAGE_CRYPTO_CORE_H