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
    /*
     * Змінна контролює, яким саме чином повинна поводитися програма при її прихованні.
     *
     *      true    -   активна сесія буде скинута, користувача буде перенаправлено на сторінку з калькулятором
     *      false   -   після повернення до програми, користувач продовжить працювати у активній сесії без втрати прогресу
     */
    bool activityProtection;

    /*
     * Змінна вказує, чи необхідно очистити "сміття", що утворюється під час введення паролю у калькуляторі
     *
     *      true    -   так, необхідно очистити
     *      false   -   ні, необхідно залишити
     */
    bool inputPassClearing;

    /*
     * За замовчуванням рекомендується тримати ці опції у таких станах:
     */
    static const bool DEFAULT_ACTIVITY_PROTECTION = true;
    static const bool DEFAULT_INPUT_PASS_CLEARING = false;

public:
    Settings();
    Settings(bool activityProtection, bool inputPassClearing);
    const bool getActivityProtection() const;
    const bool getInputPassClearing() const;
};


#endif //PASSWORD_STORAGE_SETTINGS_H
