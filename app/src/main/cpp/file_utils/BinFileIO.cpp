//
// Created by kiber_god on 10.08.2023.
//

#include <string>
#include <jni.h>
#include <fstream>
#include <vector>
#include <android/log.h>
#include "BinFileIO.h"
#include "../crypto_core.h"
#include "../model/Category.h"
#include "../model/Record.h"

// Вектор, який постійно зберігає в собі множину записів
std::vector<Record> RECORDS;


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

void loadRecordsFromBinFile() {
    std::ifstream file;
    file.open(getFilesPath() + "/example2.bin");

    if(!file.is_open()){
        __android_log_print(ANDROID_LOG_DEBUG, "cpp_debug", "ERROR READ TEST RECORDS BIN-FILE");
    } else {
        __android_log_print(ANDROID_LOG_DEBUG, "cpp_debug", "SUCCESSFUL READ TEST RECORDS BIN-FILE");

        Record record;
        while (file.read((char*)&record, sizeof(Record))){
            //record.printLog();
            decryptData(reinterpret_cast<char*>(&record), sizeof(Record));
            //record.printLog();
            RECORDS.push_back(record);
        }
    };
}

void loadCategoriesFromBinFile() {
    std::ifstream file;
    file.open(getFilesPath() + "/categories.bin");

    if(!file.is_open()){
        __android_log_print(ANDROID_LOG_DEBUG, "cpp_debug", "ERROR READ CATEGORIES BIN-FILE");
    } else {
        __android_log_print(ANDROID_LOG_DEBUG, "cpp_debug", "SUCCESSFUL READ CATEGORIES BIN-FILE");

        Category category;
        while (file.read((char*)&category, sizeof(Category))){
            category.printLog();
            decryptData(reinterpret_cast<char*>(&category), sizeof(Category));
            category.printLog();

        }
    };
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

    // Упакування Record у ArrayList
    for (const auto& record : RECORDS) {
        jclass recordClass = env->FindClass("com/example/passwordstorage/model/Record");
        jmethodID recordConstructor = env->GetMethodID(recordClass, "<init>", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V");

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