//
// Created by kiber_god on 10.08.2023.
//

#include <string>
#include <fstream>
#include <vector>
#include <android/log.h>
#include "BinFileIO.h"
#include "../crypto_core.h"
#include "../model/Settings.h"
#include "../model/Calculator.h"
#include "../model/Category.h"
#include "../model/Record.h"


std::string getRecordsFilePath() { return FILES_PATH + TEST_RECORDS_FILE; }

std::string getCategoriesFilePath() { return FILES_PATH + CATEGORIES_FILE; }

std::string getSettingsFilePath() { return FILES_PATH + SETTINGS_FILE; }

std::string getCalculatorFilePath() { return FILES_PATH + CALCULATOR_FILE; }

void setFilesPath(JNIEnv* env, jobject context) {
    jclass contextClass = env->GetObjectClass(context);
    jmethodID getFilesDirMethod = env->GetMethodID(contextClass, "getFilesDir", "()Ljava/io/File;");
    jobject filesDir = env->CallObjectMethod(context, getFilesDirMethod);

    jclass fileClass = env->FindClass("java/io/File");
    jmethodID getAbsolutePathMethod = env->GetMethodID(fileClass, "getAbsolutePath", "()Ljava/lang/String;");
    jstring absolutePath = (jstring)env->CallObjectMethod(filesDir, getAbsolutePathMethod);

    FILES_PATH = env->GetStringUTFChars(absolutePath, nullptr);

    //__android_log_print(ANDROID_LOG_DEBUG, "cpp_debug", "FILES PATH: %s", FILES_PATH.c_str());
}

std::string getFilesPath() {
    return FILES_PATH;
}

/*
 * Дана поліморфна функція завантажує дані з файлів, працючи з такими типами даних, як Record, Category та Settings.
 *
 * Слід зазначити, що вона актуальна лише доти, доки файли матимуть спільний алгоритм кодування.
 * У іншому випадку слід модифікувати рядок з декодуванням:
 *                                                          decryptData(buffer, sizeof(buffer));
 */
template <typename T>
std::vector<T> loadDataFromBinFile(const std::string& filename, std::vector<T>& data) {
    std::ifstream file;
    file.open(filename, std::ios::binary);

    if (!file.is_open()) {
        //__android_log_print(ANDROID_LOG_DEBUG, "cpp_debug", "ERROR READING BIN-FILE: %s", filename.c_str());
    } else {
        //__android_log_print(ANDROID_LOG_DEBUG, "cpp_debug", "SUCCESSFULLY READ BIN-FILE: %s", filename.c_str());

        char buffer[sizeof(T)];
        while (file.read(buffer, sizeof(buffer))) {
            decryptData(buffer, sizeof(buffer));
            data.push_back(*reinterpret_cast<T*>(buffer));
        }
    }
    return data;
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

    jclass recordClass = env->FindClass("com/example/passwordstorage/model/Record");
    jclass fieldClass = env->FindClass("com/example/passwordstorage/model/Record$Field");
    jmethodID recordConstructor = env->GetMethodID(recordClass, "<init>", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Integer;Ljava/lang/Boolean;I)V");

    std::vector<Record> records;

    // Упакування Record у ArrayList
    for (const auto& record : loadDataFromBinFile(getFilesPath() + TEST_RECORDS_FILE, records)) {
        jstring jTitle = env->NewStringUTF(record.getTitle());
        jstring jText = env->NewStringUTF(record.getText());

        // Створення об`єкта Integer
        jclass integerClass = env->FindClass("java/lang/Integer");
        jmethodID integerConstructor = env->GetMethodID(integerClass, "<init>", "(I)V");
        jobject jCategory = env->NewObject(integerClass, integerConstructor, record.getCategoryId());

        jclass booleanClass = env->FindClass("java/lang/Boolean");
        jmethodID booleanConstructor = env->GetMethodID(booleanClass, "<init>", "(Z)V");
        jobject jBookmark = env->NewObject(booleanClass, booleanConstructor, record.getBookmark());

        // Створення об`єкта Record в Java
        jobject recordObject = env->NewObject(recordClass, recordConstructor, jTitle, jText, jCategory, jBookmark, record.getIconId());

        jobjectArray jFields = env->NewObjectArray(Record::getMaxFields(), fieldClass, nullptr);
        // Заповнюємо массив об`єктів Field в Java
        for (int i = 0; i < Record::getMaxFields(); i++) {
            //jobject jField = nullptr;

            const Record::Field& cppField = record.getFields()[i];
            jstring jName = env->NewStringUTF(cppField.getName());
            jstring jValue = env->NewStringUTF(cppField.getValue());
            jboolean jValueVisibility = cppField.getValueVisibility();

            jobject valueVisibility = env->NewObject(
                    env->FindClass("java/lang/Boolean"),
                    env->GetMethodID(env->FindClass("java/lang/Boolean"), "<init>", "(Z)V"),
                    jValueVisibility
            );

            jmethodID createFieldMethod = env->GetMethodID(recordClass, "createField", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Boolean;)Lcom/example/passwordstorage/model/Record$Field;");
            jobject jField = env->CallObjectMethod(recordObject, createFieldMethod, jName, jValue, valueVisibility);

            env->DeleteLocalRef(jName);
            env->DeleteLocalRef(jValue);

            env->SetObjectArrayElement(jFields, i, jField);
        }

        jmethodID setFieldsMethod = env->GetMethodID(recordClass, "setFields", "([Lcom/example/passwordstorage/model/Record$Field;)V");
        env->CallVoidMethod(recordObject, setFieldsMethod, jFields);

        // Додавання об`єкта Record в ArrayList
        env->CallBooleanMethod(arrayList, arrayListAddMethod, recordObject);

        env->DeleteLocalRef(jTitle);
        env->DeleteLocalRef(jText);
        env->DeleteLocalRef(jCategory);
        env->DeleteLocalRef(jBookmark);
        env->DeleteLocalRef(recordObject);
    }

    return arrayList;
}


/*
 * Функція передає вектор categories-об`єктів з данного С++ модуля у Java-код
 *
 * return vector (C++) --> ArrayList (Java)
 */
extern "C" JNIEXPORT jobject JNICALL
Java_com_example_passwordstorage_NativeController_getCategories(JNIEnv *env, jclass) {
    // Створення класу ArrayList в Java
    jclass arrayListClass = env->FindClass("java/util/ArrayList");
    jmethodID arrayListConstructor = env->GetMethodID(arrayListClass, "<init>", "()V");
    jmethodID arrayListAddMethod = env->GetMethodID(arrayListClass, "add", "(Ljava/lang/Object;)Z");
    jobject arrayList = env->NewObject(arrayListClass, arrayListConstructor);

    jclass categoryClass = env->FindClass("com/example/passwordstorage/model/Category");
    jmethodID categoryConstructor = env->GetMethodID(categoryClass, "<init>", "(Ljava/lang/Integer;Ljava/lang/String;I)V");

    std::vector<Category> categories;

    // Упакування Category у ArrayList
    for (const auto& category : loadDataFromBinFile(getFilesPath() + CATEGORIES_FILE, categories)) {

        // Створення об`єкта Integer
        jclass integerClass = env->FindClass("java/lang/Integer");
        jmethodID integerConstructor = env->GetMethodID(integerClass, "<init>", "(I)V");
        jobject jId = env->NewObject(integerClass, integerConstructor, category.getId());

        jstring jName = env->NewStringUTF(category.getName());

        // Створення об`єкта Category в Java
        jobject categoryObject = env->NewObject(categoryClass, categoryConstructor, jId, jName, category.getIconId());

        // Додавання об`єкта Record в ArrayList
        env->CallBooleanMethod(arrayList, arrayListAddMethod, categoryObject);

        env->DeleteLocalRef(jName);
        env->DeleteLocalRef(categoryObject);
    }

    return arrayList;
}

/*
 * Функція передає об`єкт Settings з данного С++ модуля у Java-код
 *
 * return Settings obj (C++) --> Settings obj (Java)
 */
extern "C" JNIEXPORT jobject JNICALL
Java_com_example_passwordstorage_NativeController_getSettings(JNIEnv *env, jclass) {
    jclass settingsClass = env->FindClass("com/example/passwordstorage/model/Settings");
    jmethodID settingsConstructor = env->GetMethodID(settingsClass, "<init>", "(ZZLjava/lang/String;)V");

    std::vector<Settings> settings;

    if (loadDataFromBinFile(getFilesPath() + SETTINGS_FILE, settings).size() == 0) {
        Settings defaultSettings;
        settings.push_back(defaultSettings);
    }

    bool activityProtection = settings[0].getActivityProtection();
    bool inputCalcClearing = settings[0].getInputCalcClearing();
    const char* password = settings[0].getPassword();

    jstring jPassword = env->NewStringUTF(password);

    jobject settingsObject = env->NewObject(settingsClass, settingsConstructor, activityProtection, inputCalcClearing, jPassword);

    return settingsObject;
}

/*
 * Функція передає об`єкт Calculator з данного С++ модуля у Java-код
 *
 * return Calculator obj (C++) --> Calculator obj (Java)
 */
extern "C" JNIEXPORT jobject JNICALL
Java_com_example_passwordstorage_NativeController_getCalculator(JNIEnv *env, jclass) {
    jclass calculatorClass = env->FindClass("com/example/passwordstorage/model/Calculator");
    jmethodID calculatorConstructor = env->GetMethodID(calculatorClass, "<init>", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Character;)V");

    std::vector<Calculator> calculator;

    if (loadDataFromBinFile(getFilesPath() + CALCULATOR_FILE, calculator).size() == 0) {
        Calculator newCalculator;
        calculator.push_back(newCalculator);
    }

    const char* number1 = calculator[0].getNumber1();
    const char* number2 = calculator[0].getNumber2();
    char operation = calculator[0].getOperation();

    jstring jNumber1 = env->NewStringUTF(number1);
    jstring jNumber2 = env->NewStringUTF(number2);

    jclass characterClass = env->FindClass("java/lang/Character");
    jmethodID valueOfMethod = env->GetStaticMethodID(characterClass, "valueOf", "(C)Ljava/lang/Character;");
    jobject jOperation = operation == '\0' ? nullptr : env->CallStaticObjectMethod(characterClass, valueOfMethod, operation);

    jobject calculatorObject = env->NewObject(calculatorClass, calculatorConstructor, jNumber1, jNumber2, jOperation);

    return calculatorObject;
}

void writeToBinFile(std::string file_path, char* data, std::size_t dataSize, std::size_t classSize) {
    std::ofstream file;
    file.open(file_path, std::ofstream::app);

    if (!file.is_open()) {
        //__android_log_print(ANDROID_LOG_DEBUG, "cpp_debug", "ERROR WRITE2 BIN-FILE");
    } else {
        // XOR-шифрування
        encryptData(data, dataSize);

        file.write(reinterpret_cast<char*>(data), classSize);
        //__android_log_print(ANDROID_LOG_DEBUG, "cpp_debug", "SUCCESSFUL WRITE2 BIN-FILE");
    }
}

void dropFile(std::string file_path) {
    std::ifstream file(file_path);
    if (file.good()) {
        file.close();
        remove(file_path.c_str());
    }
}

extern "C" JNIEXPORT void JNICALL
Java_com_example_passwordstorage_NativeController_saveCategories(JNIEnv* env, jclass, jobject categoriesList) {
    jclass arrayListClass = env->GetObjectClass(categoriesList);
    jmethodID getMethod = env->GetMethodID(arrayListClass, "get", "(I)Ljava/lang/Object;");
    jmethodID sizeMethod = env->GetMethodID(arrayListClass, "size", "()I");

    jint size = env->CallIntMethod(categoriesList, sizeMethod);

    dropFile(getCategoriesFilePath());

    for (int i = 0; i < size; ++i) {
        jobject categoryObj = env->CallObjectMethod(categoriesList, getMethod, i);
        jclass categoryClass = env->GetObjectClass(categoryObj);

        jfieldID idField = env->GetFieldID(categoryClass, "id", "Ljava/lang/Integer;");
        jfieldID nameField = env->GetFieldID(categoryClass, "name", "Ljava/lang/String;");
        jfieldID iconIdField = env->GetFieldID(categoryClass, "icon_id", "I");

        jobject idObj = env->GetObjectField(categoryObj, idField);
        jstring name = static_cast<jstring>(env->GetObjectField(categoryObj, nameField));
        jint icon_id = env->GetIntField(categoryObj, iconIdField);

        const char* nameStr = env->GetStringUTFChars(name, nullptr);

        jint id = 0;
        if (idObj != nullptr) {
            jclass integerClass = env->GetObjectClass(idObj);
            jmethodID intValueMethod = env->GetMethodID(integerClass, "intValue", "()I");
            id = env->CallIntMethod(idObj, intValueMethod);
        }

        Category category{id, nameStr, icon_id};

        writeToBinFile(getCategoriesFilePath(),
                       reinterpret_cast<char*>(&category),
                       sizeof(category),
                       sizeof(Category)
        );

        env->ReleaseStringUTFChars(name, nameStr);
        env->DeleteLocalRef(categoryObj);
    }

}


extern "C" JNIEXPORT void JNICALL
Java_com_example_passwordstorage_NativeController_saveRecords(JNIEnv* env, jclass, jobject recordsList) {
    jclass arrayListClass = env->GetObjectClass(recordsList);
    jmethodID getMethod = env->GetMethodID(arrayListClass, "get", "(I)Ljava/lang/Object;");
    jmethodID sizeMethod = env->GetMethodID(arrayListClass, "size", "()I");

    jint size = env->CallIntMethod(recordsList, sizeMethod);

    dropFile(getRecordsFilePath());

    for (int i = 0; i < size; ++i) {
        jobject recordObj = env->CallObjectMethod(recordsList, getMethod, i);
        jclass recordClass = env->GetObjectClass(recordObj);

        jfieldID titleField = env->GetFieldID(recordClass, "title", "Ljava/lang/String;");
        jfieldID textField = env->GetFieldID(recordClass, "text", "Ljava/lang/String;");
        jfieldID categoryIdField = env->GetFieldID(recordClass, "category_id", "Ljava/lang/Integer;");
        jfieldID bookmarkField = env->GetFieldID(recordClass, "bookmark", "Ljava/lang/Boolean;");
        jfieldID iconIdField = env->GetFieldID(recordClass, "icon_id", "I");

        jstring title = static_cast<jstring>(env->GetObjectField(recordObj, titleField));
        jstring text = static_cast<jstring>(env->GetObjectField(recordObj, textField));
        jobject category_idObj = env->GetObjectField(recordObj, categoryIdField);
        jobject bookmarkObj = env->GetObjectField(recordObj, bookmarkField);
        jint icon_id = env->GetIntField(recordObj, iconIdField);

        const char* titleStr = env->GetStringUTFChars(title, nullptr);
        const char* textStr = env->GetStringUTFChars(text, nullptr);

        jint category_id = Record::NULL_CATEGORY_VALUE;
        if (category_idObj != nullptr) {
            jclass integerClass = env->GetObjectClass(category_idObj);
            jmethodID intValueMethod = env->GetMethodID(integerClass, "intValue", "()I");
            category_id = env->CallIntMethod(category_idObj, intValueMethod);
        }

        bool bookmark = Record::NULL_BOOKMARK_VALUE;
        if (bookmarkObj != nullptr) {
            jclass booleanClass = env->GetObjectClass(bookmarkObj);
            jmethodID booleanValueMethod = env->GetMethodID(booleanClass, "booleanValue", "()Z");
            bookmark = env->CallBooleanMethod(bookmarkObj, booleanValueMethod);
        }

        jfieldID fieldsField = env->GetFieldID(recordClass, "fields", "[Lcom/example/passwordstorage/model/Record$Field;");
        jobjectArray fieldsArray = reinterpret_cast<jobjectArray>(env->GetObjectField(recordObj, fieldsField));

        int fieldsCount = env->GetArrayLength(fieldsArray);
        std::vector<Record::Field> cppFields;

        for (int j = 0; j < fieldsCount; ++j) {
            jobject fieldObj = env->GetObjectArrayElement(fieldsArray, j);

            jclass fieldClass = env->FindClass("com/example/passwordstorage/model/Record$Field");
            jfieldID nameField = env->GetFieldID(fieldClass, "name", "Ljava/lang/String;");
            jfieldID valueField = env->GetFieldID(fieldClass, "value", "Ljava/lang/String;");
            jfieldID valueVisibilityField = env->GetFieldID(fieldClass, "valueVisibility", "Z");

            jstring jName = static_cast<jstring>(env->GetObjectField(fieldObj, nameField));
            jstring jValue = static_cast<jstring>(env->GetObjectField(fieldObj, valueField));
            jboolean jValueVisibility = env->GetBooleanField(fieldObj, valueVisibilityField);

            const char* nameStr = env->GetStringUTFChars(jName, nullptr);
            const char* valueStr = env->GetStringUTFChars(jValue, nullptr);
            bool valueVisibility = static_cast<bool>(jValueVisibility);

            cppFields.push_back(Record::Field{nameStr, valueStr, valueVisibility});

            env->ReleaseStringUTFChars(jName, nameStr);
            env->ReleaseStringUTFChars(jValue, valueStr);
        }

        Record record{titleStr, textStr, category_id, bookmark, icon_id, cppFields.data()};

        writeToBinFile(getRecordsFilePath(),
                       reinterpret_cast<char*>(&record),
                       sizeof(record),
                       sizeof(Record)
        );

        env->ReleaseStringUTFChars(title, titleStr);
        env->ReleaseStringUTFChars(text, textStr);
        env->DeleteLocalRef(recordObj);
    }
}

extern "C" JNIEXPORT void JNICALL
Java_com_example_passwordstorage_NativeController_saveSettings(JNIEnv* env, jclass, jobject settingsObject) {
    dropFile(getSettingsFilePath());

    jclass settingsClass = env->GetObjectClass(settingsObject);

    jfieldID activityProtectionField = env->GetFieldID(settingsClass, "activityProtection", "Z");
    jfieldID inputCalcClearingField = env->GetFieldID(settingsClass, "inputCalcClearing", "Z");
    jfieldID passwordField = env->GetFieldID(settingsClass, "password", "Ljava/lang/String;");

    jboolean activityProtection = env->GetBooleanField(settingsObject, activityProtectionField);
    jboolean inputCalcClearing = env->GetBooleanField(settingsObject, inputCalcClearingField);
    jstring jPassword = (jstring)env->GetObjectField(settingsObject, passwordField);

    const char* password = env->GetStringUTFChars(jPassword, nullptr);

    Settings settings(activityProtection, inputCalcClearing, password);

    writeToBinFile(getSettingsFilePath(),
                   reinterpret_cast<char*>(&settings),
                   sizeof(settings),
                   sizeof(Settings)
    );

    env->ReleaseStringUTFChars(jPassword, password);
    env->DeleteLocalRef(settingsObject);
}

extern "C" JNIEXPORT void JNICALL
Java_com_example_passwordstorage_NativeController_saveCalculator(JNIEnv* env, jclass, jobject calculatorObject) {
    dropFile(getCalculatorFilePath());

    jclass calculatorClass = env->GetObjectClass(calculatorObject);

    jfieldID jNumber1Field = env->GetFieldID(calculatorClass, "number1", "Ljava/lang/String;");
    jfieldID jNumber2Field = env->GetFieldID(calculatorClass, "number2", "Ljava/lang/String;");
    jfieldID jOperationField = env->GetFieldID(calculatorClass, "operation", "Ljava/lang/Character;");

    jstring jNumber1 = (jstring)env->GetObjectField(calculatorObject, jNumber1Field);
    jstring jNumber2 = (jstring)env->GetObjectField(calculatorObject, jNumber2Field);
    jobject jOperation = env->GetObjectField(calculatorObject, jOperationField);

    jclass characterClass = env->GetObjectClass(jOperation);
    jmethodID charValueMethod = env->GetMethodID(characterClass, "charValue", "()C");
    char operation = env->CallCharMethod(jOperation, charValueMethod);

    const char* number1 = env->GetStringUTFChars(jNumber1, nullptr);
    const char* number2 = env->GetStringUTFChars(jNumber2, nullptr);

    Calculator calculator(number1, number2, operation);

    writeToBinFile(getCalculatorFilePath(),
                   reinterpret_cast<char*>(&calculator),
                   sizeof(calculator),
                   sizeof(Calculator)
    );

    env->ReleaseStringUTFChars(jNumber1, number1);
    env->ReleaseStringUTFChars(jNumber2, number2);
    env->DeleteLocalRef(calculatorObject);
}