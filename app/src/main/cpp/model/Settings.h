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
     * За замовчуванням рекомендується тримати ці опції у таких станах:
     */
    static const bool DEFAULT_ACTIVITY_PROTECTION = true;
    static const bool DEFAULT_INPUT_CALC_CLEARING = false;
    static const std::string DEFAULT_PASSWORD;
    static const bool DEFAULT_DIGITAL_OWNER = false;


public:
    Settings();
    Settings(const bool activityProtection, const bool inputCalcClearing, const char* password, const bool digitalOwner);
    const bool getActivityProtection() const;
    const bool getInputCalcClearing() const;
    const char* getPassword() const;
    const bool getDigitalOwner() const;
};


#endif //PASSWORD_STORAGE_SETTINGS_H
