#include <jni.h>
#include <string>

#include <android/log.h>

std::string FILES_PATH;

// Функція зберігає значення дефолтного ключа для входу у сховище
extern "C" JNIEXPORT jstring JNICALL
Java_com_example_passwordstorage_MainActivity_getKey(
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

// Ініт-функція, що містить всі стартові виклики
extern "C" JNIEXPORT void JNICALL
Java_com_example_passwordstorage_MainActivity_initSecurityCore(
        JNIEnv* env,
        jobject /* this */, jobject context) {

    setFilesPath(env, context);
}