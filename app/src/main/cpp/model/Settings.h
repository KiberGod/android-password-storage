//
// Created by kiber_god on 06.09.2023.
//

#ifndef PASSWORD_STORAGE_SETTINGS_H
#define PASSWORD_STORAGE_SETTINGS_H

/*
 *   Даний клас містить налаштування та не повинен
 *   існувати більш ніж в одному екземплярі
 */
class Settings {
private:
    static const int MAX_PASSWORD_LENGTH = 10;
    static const int MAX_CALC_EXPRESSION = 200;

    /*
     * Змінна контролює, яким саме чином повинна поводитися програма при її прихованні.
     *
     *      true    -   активна сесія буде скинута, користувача буде перенаправлено на сторінку з калькулятором
     *      false   -   після повернення до програми, користувач продовжить працювати у активній сесії без втрати прогресу
     */
    bool activityProtection;

    /*
     * Змінна вказує, чи необхідно очистити введені дані у калькуляторі
     *
     *      true    -   так, необхідно очистити
     *      false   -   ні, необхідно залишити
     */
    bool inputCalcClearing;

    /*
     * Пароль для входу у програму
     */
    char password[MAX_PASSWORD_LENGTH];

    /*
     * Змінна вказує, у якому стані знаходиться "Цифровий власник"
     *
     *      true    -   увімкнений
     *      false   -   вимкнутий
     */
    bool digitalOwner;

    /*
     * Змінна зберігає стан фільтру порядку сортування
     *
     *      true    -   від найстаріших
     *      false   -   від найновіших
     */
    bool filtersSortMode;

    /*
     * Змінна зберігає стан фільтру критерію сортування
     *
     *      1 - за датою редагування
     *      2 - за датою перегляду
     *      3 - за датою створення
     */
    int filtersSortParam;

    /*
     *  Шрифти (у dp)
     */
    int fontSizeMain;
    int fontSizeInput;
    int fontSizeButtons;
    int fontSizeLargeButtons;
    int fontSizeFieldCaptions;
    int fontSizeOther;
    int fontSizeRssMain;
    int fontSizeRssSecondary;

    // Рядок калькулятора
    char calcExpression[MAX_CALC_EXPRESSION];

    /*
     * За замовчуванням рекомендується тримати ці опції у таких станах:
     */
    static const bool DEFAULT_ACTIVITY_PROTECTION = true;
    static const bool DEFAULT_INPUT_CALC_CLEARING = false;
    static const std::string DEFAULT_PASSWORD;
    static const bool DEFAULT_DIGITAL_OWNER = false;
    static const bool DEFAULT_FILTERS_SORT_MODE = false;
    static const int DEFAULT_FILTERS_SORT_PARAM = 3;
    static const int DEFAULT_FONT_SIZE_MAIN = 18;
    static const int DEFAULT_FONT_SIZE_INPUT = 18;
    static const int DEFAULT_FONT_SIZE_BUTTONS = 18;
    static const int DEFAULT_FONT_SIZE_LARGE_BUTTONS = 14;
    static const int DEFAULT_FONT_SIZE_FIELD_CAPTIONS = 14;
    static const int DEFAULT_FONT_SIZE_OTHER = 16;
    static const int DEFAULT_FONT_SIZE_RSS_MAIN = 16;
    static const int DEFAULT_FONT_SIZE_RSS_SECONDARY = 12;
    static const std::string DEFAULT_CALC_EXPRESSION;

public:
    Settings();
    Settings(const bool activityProtection, const bool inputCalcClearing, const char* password,
             const bool digitalOwner, const bool filtersSortMode, const int filtersSortParam,
             const int fontSizeMain, const int fontSizeInput, const int fontSizeButtons, const int fontSizeLargeButtons,
             const int fontSizeFieldCaptions, const int fontSizeOther, const int fontSizeRssMain, const int fontSizeRssSecondary, const char* calcExpression);
    const bool getActivityProtection() const;
    const bool getInputCalcClearing() const;
    const char* getPassword() const;
    const bool getDigitalOwner() const;
    const bool getFiltersSortMode() const;
    const int getFiltersSortParam() const;
    const int getFontSizeMain() const;
    const int getFontSizeInput() const;
    const int getFontSizeButtons() const;
    const int getFontSizeLargeButtons() const;
    const int getFontSizeFieldCaptions() const;
    const int getFontSizeOther() const;
    const int getFontSizeRssMain() const;
    const int getFontSizeRssSecondary() const;
    const char* geCalcExpression() const;
};


#endif //PASSWORD_STORAGE_SETTINGS_H