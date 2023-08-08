#include <jni.h>
#include <string>
#include <fstream>
#include <android/log.h>
#include "Record.h"
#include "crypto_core.h"
#include "migrations.h"
#include <vector>

// Шлях до внутрішнього файлового сховища програми
std::string FILES_PATH;

// Вектор, який постійно зберігає в собі множину записів
std::vector<Record> RECORDS;


// Функція зберігає значення дефолтного ключа для входу у сховище
extern "C" JNIEXPORT jstring JNICALL
Java_com_example_passwordstorage_NativeController_getKey(
        JNIEnv* env,
        jclass clazz) {
    std::string hello = "7-.93";
    return env->NewStringUTF(hello.c_str());
}

// Функція встановлює шлях до файлів програми
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

// Завантаження записів з бінарного файла (усіх)
void loadRecordsFromBinFile() {
    std::ifstream file;
    file.open(FILES_PATH + "/example2.bin");

    if(!file.is_open()){
        __android_log_print(ANDROID_LOG_DEBUG, "cpp_debug", "ERROR READ2 BIN-FILE");
    } else {
        __android_log_print(ANDROID_LOG_DEBUG, "cpp_debug", "SUCCESSFUL READ2 BIN-FILE");

        Record record;
        while (file.read((char*)&record, sizeof(Record))){
            //record.printLog();
            decryptData(reinterpret_cast<char*>(&record), sizeof(Record));
            //record.printLog();
            RECORDS.push_back(record);
        }
    };
}

// Ініт-функція, що містить всі стартові виклики
extern "C" JNIEXPORT void JNICALL
Java_com_example_passwordstorage_NativeController_initSecurityCore(
        JNIEnv* env,
        jclass clazz, jobject context) {

    setFilesPath(env, context);

    // testing work with bin-file
    //
    // runMigrations(FILES_PATH);
    loadRecordsFromBinFile();
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
        jclass recordClass = env->FindClass("com/example/passwordstorage/Record");
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