//
// Created by kiber_god on 10.08.2023.
//

/*
 *  Даний модуль містить функції та дані для роботи з бінарними файлами внутрішнього
 *  файлового сховища програми
 */

#ifndef PASSWORD_STORAGE_BINFILEIO_H
#define PASSWORD_STORAGE_BINFILEIO_H

#include <jni.h>

// Шлях до внутрішнього файлового сховища програми
static std::string FILES_PATH;

// Тестовий файл записів
static const std::string TEST_RECORDS_FILE = "/example2.bin";

// Файл категорій
static const std::string CATEGORIES_FILE = "/categories.bin";

// Повертає і`мя файла тестових записів
std::string getTestRecordsFilePath();

// Повертає і`мя файла категорій
std::string getCategoriesFilePath();

// Функція встановлює шлях до файлів програми
void setFilesPath(JNIEnv* env, jobject context);

// Повертає шлях до файлів програми
std::string getFilesPath();

// Дана поліморфна функція завантажує дані з файлів, працючи з такими типами даних, як Record та Category
template <typename T>
std::vector<T> loadDataFromBinFile(const std::string& filename, std::vector<T>& data);

// Функція передає вектор records-об`єктів з данного С++ модуля у Java-код
extern "C" JNIEXPORT jobject JNICALL
Java_com_example_passwordstorage_NativeController_getRecords(JNIEnv *env, jclass);

// Функція передає вектор categories-об`єктів з данного С++ модуля у Java-код
extern "C" JNIEXPORT jobject JNICALL
Java_com_example_passwordstorage_NativeController_getCategories(JNIEnv *env, jclass);

// Функція запису змін до бінарного файла
void writeToBinFile(std::string file_path, char* data, std::size_t dataSize, std::size_t classSize);

// Видалення бінарного файла
void dropFile(std::string file_path);

// Отримання нових даних категорій з java, які необхідно внести у файл
extern "C" JNIEXPORT void JNICALL
Java_com_example_passwordstorage_NativeController_saveCategories(JNIEnv* env, jclass, jobject categoriesList);

// Отримання нових даних записів з java, які необхідно внести у файл
extern "C" JNIEXPORT void JNICALL
Java_com_example_passwordstorage_NativeController_saveRecords(JNIEnv* env, jclass, jobject recordsList);

#endif //PASSWORD_STORAGE_BINFILEIO_H