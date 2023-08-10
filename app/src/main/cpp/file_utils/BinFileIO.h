//
// Created by kiber_god on 10.08.2023.
//

/*
 *  Даний модуль містить функції та дані для роботи з бінарними файлами внутрішнього
 *  файлового сховища програми
 */

#ifndef PASSWORD_STORAGE_BINFILEIO_H
#define PASSWORD_STORAGE_BINFILEIO_H


// Шлях до внутрішнього файлового сховища програми
static std::string FILES_PATH;


// Функція встановлює шлях до файлів програми
void setFilesPath(JNIEnv* env, jobject context);

// Повертає шлях до файлів програми
std::string getFilesPath();

// Завантаження записів з бінарного файла (усіх)
void loadRecordsFromBinFile();

// Завантаження категорій з бінарного файла
void loadCategoriesFromBinFile();

//Функція передає вектор records-об`єктів з данного С++ модуля у Java-код
extern "C" JNIEXPORT jobject JNICALL
Java_com_example_passwordstorage_NativeController_getRecords(JNIEnv *env, jclass);

#endif //PASSWORD_STORAGE_BINFILEIO_H
