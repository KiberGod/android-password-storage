#include <jni.h>
#include <string>
#include <fstream>
#include <android/log.h>
#include "Record.h"
#include "crypto_core.h"
#include "migrations.h"

// Шлях до внутрішнього файлового сховища програми
std::string FILES_PATH;


// Функція зберігає значення дефолтного ключа для входу у сховище
extern "C" JNIEXPORT jstring JNICALL
Java_com_example_passwordstorage_NativeController_getKey(
        JNIEnv* env,
        jobject /* this */) {
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


// Тестове зчитування записів з бінарного файла (усіх)
void testReadToBinFile(){
    std::ifstream file;
    file.open(FILES_PATH + "/example2.bin");

    if(!file.is_open()){
        __android_log_print(ANDROID_LOG_DEBUG, "cpp_debug", "ERROR READ2 BIN-FILE");
    } else {
        __android_log_print(ANDROID_LOG_DEBUG, "cpp_debug", "SUCCESSFUL READ2 BIN-FILE");

        Record record;
        while (file.read((char*)&record, sizeof(Record))){
            record.printLog();
            decryptData(reinterpret_cast<char*>(&record), sizeof(Record));
            record.printLog();
        }
    };
}


// Ініт-функція, що містить всі стартові виклики
extern "C" JNIEXPORT void JNICALL
Java_com_example_passwordstorage_NativeController_initSecurityCore(
        JNIEnv* env,
        jobject /* this */, jobject context) {

    setFilesPath(env, context);

    // testing work with bin-file
    //runMigrations(FILES_PATH);
    testReadToBinFile();
}