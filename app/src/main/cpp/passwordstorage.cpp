#include <jni.h>
#include <string>

extern "C" JNIEXPORT jstring JNICALL
Java_com_example_passwordstorage_MainActivity_getKey(
        JNIEnv* env,
        jobject /* this */) {
    std::string hello = "7-.93";
    return env->NewStringUTF(hello.c_str());
}