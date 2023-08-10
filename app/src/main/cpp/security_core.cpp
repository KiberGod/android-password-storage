#include <jni.h>
#include <string>
#include <android/log.h>
#include "migrations/migrations.h"
#include "file_utils/BinFileIO.h"


// Функція зберігає значення дефолтного ключа для входу у сховище
extern "C" JNIEXPORT jstring JNICALL
Java_com_example_passwordstorage_NativeController_getKey(
        JNIEnv* env,
        jclass clazz) {
    std::string hello = "7-.93";
    return env->NewStringUTF(hello.c_str());
}

// Ініт-функція, що містить всі стартові виклики
extern "C" JNIEXPORT void JNICALL
Java_com_example_passwordstorage_NativeController_initSecurityCore(
        JNIEnv* env,
        jclass clazz, jobject context) {

    setFilesPath(env, context);
    refreshMigrations(getFilesPath());
    loadCategoriesFromBinFile();
    loadRecordsFromBinFile();
}
