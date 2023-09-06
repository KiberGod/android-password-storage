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
     * За замовчуванням рекомендується тримати цю опцію увімкненою
     */
    static const bool DEFAULT_ACTIVITY_PROTECTION = true;

public:
    Settings();
    Settings(bool activityProtection);
    const bool getActivityProtection() const;
};


#endif //PASSWORD_STORAGE_SETTINGS_H
