//
// Created by kiber_god on 09.08.2023.
//

#ifndef PASSWORD_STORAGE_MIGRATIONUTILS_H
#define PASSWORD_STORAGE_MIGRATIONUTILS_H

/*
 * Файл містить загальні функції роботи з міграціями та використовується
 * різними класами міграцій
 */

void writeToBinFile(std::string file_path, char* data, std::size_t dataSize, std::size_t classSize);

void dropFile(std::string file_path);

#endif //PASSWORD_STORAGE_MIGRATIONUTILS_H
