//
// Created by kiber_god on 10.08.2023.
//

#include <string>
#include <fstream>
#include <vector>
#include <android/log.h>
#include "BinFileIO.h"
#include "../crypto_core.h"
#include "../model/Category.h"
#include "../model/Record.h"



std::string getTestRecordsFilePath() { return FILES_PATH + TEST_RECORDS_FILE; }

std::string getCategoriesFilePath() { return FILES_PATH + CATEGORIES_FILE; }

void setFilesPath(JNIEnv* env, jobject context) {
    jclass contextClass = env->GetObjectClass(context);
    jmethodID getFilesDirMethod = env->GetMethodID(contextClass, "getFilesDir", "()Ljava/io/File;");
    jobject filesDir = env->CallObjectMethod(context, getFilesDirMethod);

    jclass fileClass = env->FindClass("java/io/File");
    jmethodID getAbsolutePathMethod = env->GetMethodID(fileClass, "getAbsolutePath", "()Ljava/lang/String;");
    jstring absolutePath = (jstring)env->CallObjectMethod(filesDir, getAbsolutePathMethod);

    FILES_PATH = env->GetStringUTFChars(absolutePath, nullptr);

    __android_log_print(ANDROID_LOG_DEBUG, "cpp_debug", "FILES PATH: %s", FILES_PATH.c_str());
}

std::string getFilesPath() {
    return FILES_PATH;
}

/*
 * Дана поліморфна функція завантажує дані з файлів, працючи з такими типами даних, як Record та Category.
 *
 * Слід зазначити, що вона актуальна лише доти, доки файли матимуть спільний алгоритм кодування.
 * У іншому випадку слід модифікувати рядок з декодуванням:
 *                                                          decryptData(buffer, sizeof(buffer));
 */
template <typename T>
std::vector<T> loadDataFromBinFile(const std::string& filename, std::vector<T>& data) {
    std::ifstream file;
    file.open(filename, std::ios::binary);

    if (!file.is_open()) {
        __android_log_print(ANDROID_LOG_DEBUG, "cpp_debug", "ERROR READING BIN-FILE: %s", filename.c_str());
    } else {
        __android_log_print(ANDROID_LOG_DEBUG, "cpp_debug", "SUCCESSFULLY READ BIN-FILE: %s", filename.c_str());

        char buffer[sizeof(T)];
        while (file.read(buffer, sizeof(buffer))) {
            decryptData(buffer, sizeof(buffer));
            data.push_back(*reinterpret_cast<T*>(buffer));
        }
    }
    return data;
}

/*
 * Функція передає вектор records-об`єктів з данного С++ модуля у Java-код
 *
 * В данному випадку сигнатура "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V" означає
 * виклик конструктора, що має 3 параметри типу String. При зміні коструктора класу Record (на стороні java),
 * слід відповідно змінити дану сигнатуру
 *
 * return vector (C++) --> ArrayList (Java)
 */
extern "C" JNIEXPORT jobject JNICALL
Java_com_example_passwordstorage_NativeController_getRecords(JNIEnv *env, jclass) {
    // Створення класу ArrayList в Java
    jclass arrayListClass = env->FindClass("java/util/ArrayList");
    jmethodID arrayListConstructor = env->GetMethodID(arrayListClass, "<init>", "()V");
    jmethodID arrayListAddMethod = env->GetMethodID(arrayListClass, "add", "(Ljava/lang/Object;)Z");
    jobject arrayList = env->NewObject(arrayListClass, arrayListConstructor);

    jclass recordClass = env->FindClass("com/example/passwordstorage/model/Record");
    jmethodID recordConstructor = env->GetMethodID(recordClass, "<init>", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V");

    std::vector<Record> records;

    // Упакування Record у ArrayList
    for (const auto& record : loadDataFromBinFile(getFilesPath() + TEST_RECORDS_FILE, records)) {

        jstring jTitle = env->NewStringUTF(record.getTitle());
        jstring jText = env->NewStringUTF(record.getText());
        jstring jCategory = env->NewStringUTF(record.getCategory());

        // Створення об`єкта Record в Java
        jobject recordObject = env->NewObject(recordClass, recordConstructor, jTitle, jText, jCategory);

        // Додавання об`єкта Record в ArrayList
        env->CallBooleanMethod(arrayList, arrayListAddMethod, recordObject);

        env->DeleteLocalRef(jTitle);
        env->DeleteLocalRef(jText);
        env->DeleteLocalRef(jCategory);
        env->DeleteLocalRef(recordObject);
    }

    return arrayList;
}

/*
 * Функція передає вектор categories-об`єктів з данного С++ модуля у Java-код
 *
 * return vector (C++) --> ArrayList (Java)
 */
extern "C" JNIEXPORT jobject JNICALL
Java_com_example_passwordstorage_NativeController_getCategories(JNIEnv *env, jclass) {
    // Створення класу ArrayList в Java
    jclass arrayListClass = env->FindClass("java/util/ArrayList");
    jmethodID arrayListConstructor = env->GetMethodID(arrayListClass, "<init>", "()V");
    jmethodID arrayListAddMethod = env->GetMethodID(arrayListClass, "add", "(Ljava/lang/Object;)Z");
    jobject arrayList = env->NewObject(arrayListClass, arrayListConstructor);

    jclass categoryClass = env->FindClass("com/example/passwordstorage/model/Category");
    jmethodID categoryConstructor = env->GetMethodID(categoryClass, "<init>", "(Ljava/lang/String;)V");

    std::vector<Category> categories;

    // Упакування Category у ArrayList
    for (const auto& category : loadDataFromBinFile(getFilesPath() + CATEGORIES_FILE, categories)) {

        jstring jName = env->NewStringUTF(category.getName());

        // Створення об`єкта Category в Java
        jobject categoryObject = env->NewObject(categoryClass, categoryConstructor, jName);

        // Додавання об`єкта Record в ArrayList
        env->CallBooleanMethod(arrayList, arrayListAddMethod, categoryObject);

        env->DeleteLocalRef(jName);
        env->DeleteLocalRef(categoryObject);
    }

    return arrayList;
}

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

extern "C" JNIEXPORT void JNICALL
Java_com_example_passwordstorage_NativeController_saveCategories(JNIEnv* env, jclass, jobject categoriesList) {
    jclass arrayListClass = env->GetObjectClass(categoriesList);
    jmethodID getMethod = env->GetMethodID(arrayListClass, "get", "(I)Ljava/lang/Object;");
    jmethodID sizeMethod = env->GetMethodID(arrayListClass, "size", "()I");

    jint size = env->CallIntMethod(categoriesList, sizeMethod);

    dropFile(getCategoriesFilePath());

    for (int i = 0; i < size; ++i) {
        jobject categoryObj = env->CallObjectMethod(categoriesList, getMethod, i);
        jclass categoryClass = env->GetObjectClass(categoryObj);
        jfieldID nameField = env->GetFieldID(categoryClass, "name", "Ljava/lang/String;");
        jstring name = static_cast<jstring>(env->GetObjectField(categoryObj, nameField));

        const char* nameStr = env->GetStringUTFChars(name, nullptr);
        Category category{nameStr};

        writeToBinFile(getCategoriesFilePath(),
                       reinterpret_cast<char*>(&category),
                       sizeof(category),
                       sizeof(Category)
        );

        env->ReleaseStringUTFChars(name, nameStr);
        env->DeleteLocalRef(categoryObj);
    }

}