//
// Created by kiber_god on 09.08.2023.
//

#include <fstream>
#include <android/log.h>
#include <string>
#include "MigrationUtils.h"
#include "../../crypto_core.h"

void writeToBinFile(std::string file_path, char* data, std::size_t dataSize, std::size_t classSize) {
    std::ofstream file;
    file.open(file_path, std::ofstream::app);

    if (!file.is_open()) {
        __android_log_print(ANDROID_LOG_DEBUG, "cpp_debug", "ERROR WRITE2 BIN-FILE");
    } else {
        // XOR-шифрування
        encryptData(data, dataSize);

        file.write(reinterpret_cast<char*>(data), classSize);
        __android_log_print(ANDROID_LOG_DEBUG, "cpp_debug", "SUCCESSFUL WRITE2 BIN-FILE");
    }
}

void dropFile(std::string file_path) {
    std::ifstream file(file_path);
    if (file.good()) {
        file.close();
        remove(file_path.c_str());
    }
}