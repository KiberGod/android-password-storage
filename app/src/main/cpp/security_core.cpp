#include <jni.h>
#include <string>
#include "file_utils/BinFileIO.h"


// Ініт-функція, що містить всі стартові виклики
extern "C" JNIEXPORT void JNICALL
Java_com_kibergod_passwordstorage_NativeController_initSecurityCore(
        JNIEnv* env,
        jclass clazz, jobject context) {

    setFilesPath(env, context);
}
