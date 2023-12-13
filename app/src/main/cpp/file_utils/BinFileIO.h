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
#include "../model/Category.h"

// Шлях до внутрішнього файлового сховища програми
static std::string FILES_PATH;

// Тестовий файл записів
static const std::string RECORDS_FILE = "/records_v4.bin";

// Прихована буферна копія файла записів
static const std::string HIDDEN_RECORDS_FILE = "/hidden_records_v4.bin";

// Файл категорій
static const std::string CATEGORIES_FILE = "/categories_v4.bin";

// Прихована буферна копія файла категорій
static const std::string HIDDEN_CATEGORIES_FILE = "/hidden_categories_v4.bin";

// Файл налаштуваннь
static const std::string SETTINGS_FILE = "/settings.bin";

// Файл даних калькулятора
static const std::string CALCULATOR_FILE = "/calcData.bin";

// Файл даних "Цифрового власника"
static const std::string DIGITAL_OWNER_FILE = "/digitalOwner.bin";


// Повертає і`мя файла тестових записів
std::string getRecordsFilePath();

// Повертає і`мя прихованої копії файла записів
std::string getHiddenRecordsFilePath();

// Повертає і`мя файла категорій
std::string getCategoriesFilePath();

// Повертає і`мя прихованої копії файла категорій
std::string getHiddenCategoriesFilePath();

// Повертає і`мя файла налаштуваннь
std::string getSettingsFilePath();

// Повертає і`мя файла даних калькулятора
std::string getCalculatorFilePath();

// Повертає і`мя файла даних "Цифрового власника"
std::string getDigitalOwnerFilePath();

// Функція встановлює шлях до файлів програми
void setFilesPath(JNIEnv* env, jobject context);

// Повертає шлях до файлів програми
std::string getFilesPath();

// Дана поліморфна функція завантажує дані з файлів, працючи з такими типами даних, як Record та Category
template <typename T>
std::vector<T> loadDataFromBinFile(const std::string& filename, std::vector<T>& data);

// Функція передає вектор records-об`єктів з данного С++ модуля у Java-код
extern "C" JNIEXPORT jobject JNICALL
Java_com_kibergod_passwordstorage_NativeController_getRecords(JNIEnv *env, jclass);

// Функція передає вектор categories-об`єктів з данного С++ модуля у Java-код
extern "C" JNIEXPORT jobject JNICALL
Java_com_kibergod_passwordstorage_NativeController_getCategories(JNIEnv *env, jclass);

// Функція передає об`єкт Settings з данного С++ модуля у Java-код
extern "C" JNIEXPORT jobject JNICALL
Java_com_kibergod_passwordstorage_NativeController_getSettings(JNIEnv *env, jclass);

// Функція передає об`єкт Calculator з данного С++ модуля у Java-код
extern "C" JNIEXPORT jobject JNICALL
Java_com_kibergod_passwordstorage_NativeController_getCalculator(JNIEnv *env, jclass);

// Функція передає об`єкт DivitalOwner з данного С++ модуля у Java-код
extern "C" JNIEXPORT jobject JNICALL
Java_com_kibergod_passwordstorage_NativeController_getDigitalOwner(JNIEnv *env, jclass);

// Функція запису змін до бінарного файла
void writeToBinFile(std::string file_path, char* data, std::size_t dataSize, std::size_t classSize);

// Видалення бінарного файла
void dropFile(std::string file_path);

// Створення копії файла
void copyFile(const std::string& mainFilePath, const std::string& copyFile);

// Отримання нових даних категорій з java, які необхідно внести у файл
extern "C" JNIEXPORT void JNICALL
Java_com_kibergod_passwordstorage_NativeController_saveCategories(JNIEnv* env, jclass, jobject categoriesList);

// Отримання нових даних записів з java, які необхідно внести у файл
extern "C" JNIEXPORT void JNICALL
Java_com_kibergod_passwordstorage_NativeController_saveRecords(JNIEnv* env, jclass, jobject recordsList);

// Отримання нових даних налаштувань з java, які необхідно внести у файл
extern "C" JNIEXPORT void JNICALL
Java_com_kibergod_passwordstorage_NativeController_saveSettings(JNIEnv* env, jclass, jobject settingsObject);

// Отримання нових даних калькулятора з java, які необхідно внести у файл
extern "C" JNIEXPORT void JNICALL
Java_com_kibergod_passwordstorage_NativeController_saveCalculator(JNIEnv* env, jclass, jobject calculatorObject);

// Отримання нових даних "Цифрового власника" з java, які необхідно внести у файл
extern "C" JNIEXPORT void JNICALL
Java_com_kibergod_passwordstorage_NativeController_saveDigitalOwner(JNIEnv* env, jclass, jobject digitalOwnerObject);

// Знищує дані користувача (файли записів та категорій)
extern "C" JNIEXPORT void JNICALL
Java_com_kibergod_passwordstorage_NativeController_destroyUserData(JNIEnv* env, jclass);

// Приховує дані користувача (файли записів та категорій) шляхом створення копій та видалення основних файлів
extern "C" JNIEXPORT void JNICALL
Java_com_kibergod_passwordstorage_NativeController_hideUserData(JNIEnv* env, jclass);

// Відновлення прихованих записів
extern "C" JNIEXPORT void JNICALL
Java_com_kibergod_passwordstorage_NativeController_retrieveHiddenRecords(JNIEnv* env, jclass);

// Відновлення прихованих категорій
extern "C" JNIEXPORT void JNICALL
Java_com_kibergod_passwordstorage_NativeController_retrieveHiddenCategories(JNIEnv* env, jclass);

// Повертає об`єкт DataTime на основі данних Rcord або Category
jobject getDateTimeObj(JNIEnv* env, const DateTime& dateTime);

// Повертає об`єкт DataTime
DateTime getDateTimeObj(JNIEnv* env, jclass dataClass, jobject dataObj, const char *name);

#endif //PASSWORD_STORAGE_BINFILEIO_H