#include <jni.h>
#include <string>
#include <fstream>

#include <android/log.h>


// Шлях до внутрішнього файлового сховища програми
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


class Record
{
private:
    static const int MAX_TITLE_LENGTH = 20;
    static const int MAX_TEXT_LENGTH = 100;
    static const int MAX_CATEGORY_LENGTH = 20;

    char title[MAX_TITLE_LENGTH];
    char text[MAX_TEXT_LENGTH];
    char category[MAX_CATEGORY_LENGTH];

public:
    // Конструктор, що використовується програмою під час парсингу бінарного файлу даних
    Record() {
        title[0] = '\0';
        text[0] = '\0';
        category[0] = '\0';
    }

    // Конструктор, що використовується для створення тестових записів
    Record(const char* title, const char* text, const char* category) {

        strncpy(this->title, title, MAX_TITLE_LENGTH - 1);
        this->title[MAX_TITLE_LENGTH - 1] = '\0';

        strncpy(this->text, text, MAX_TEXT_LENGTH - 1);
        this->text[MAX_TEXT_LENGTH - 1] = '\0';

        strncpy(this->category, category, MAX_CATEGORY_LENGTH - 1);
        this->category[MAX_CATEGORY_LENGTH - 1] = '\0';
    }

    // Друк запису у лог
    void printLog() {
        __android_log_print(ANDROID_LOG_DEBUG,
                            "cpp_debug",
                            "Title: %.20s | Text: %.100s | Category: %.20s",
                            title, text, category);
    }
};

// Тимчасовий секретний ключ для XOR-шифрування
const char SECRET_KEY = 'kyh78T789MY7UGTN76lOlio8iku7uGYM';

// Кодування даних (приведених до бінарного виду) об`єкта класу Запису методом XOR
void encryptData(char* data, std::size_t size) {
    for (std::size_t i = 0; i < size; ++i) {
        data[i] = data[i] ^ SECRET_KEY;
    }
}

// Розшифровування даних (отриманих у бінарному виді з bin-файлу) об`єкта класу Запису методом XOR
void decryptData(char* data, std::size_t size) {
    for (std::size_t i = 0; i < size; ++i) {
        data[i] = data[i] ^ SECRET_KEY;
    }
}


// Тестовий запис даних до бінарного файла (дописування об`єкта класу Запису)
void testWriteToBinFile() {
    //Record record("My pass note1", "main text YHINUYH78yjhi7", "google");
    Record record("My pass note2", "F9fg9dfe76u", "google");

    std::ofstream file;
    file.open(FILES_PATH + "/example2.bin",std::ofstream::app);

    if(!file.is_open()) {
        __android_log_print(ANDROID_LOG_DEBUG, "cpp_debug", "ERROR WRITE2 BIN-FILE");
    } else {

        // Отримання показника на дані об`єкта record
        char* data = reinterpret_cast<char*>(&record);

        std::size_t dataSize = sizeof(record);

        // XOR-шифрування
        encryptData(data, dataSize);

        file.write((char *)&record, sizeof(Record));
        __android_log_print(ANDROID_LOG_DEBUG, "cpp_debug", "SUCCESSFUL WRITE2 BIN-FILE");
    };
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
Java_com_example_passwordstorage_MainActivity_initSecurityCore(
        JNIEnv* env,
        jobject /* this */, jobject context) {

    setFilesPath(env, context);

    // testing work with bin-file
    //testWriteToBinFile();
    testReadToBinFile();
}