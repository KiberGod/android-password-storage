package com.example.passwordstorage.model;

/*
 *   Даний клас містить налаштування та не повинен
 *   існувати більш ніж в одному екземплярі
 */
public class Settings {

    /*
     * Змінна контролює, яким саме чином повинна поводитися програма при її прихованні.
     *
     *      true    -   активна сесія буде скинута, користувача буде перенаправлено на сторінку з калькулятором
     *      false   -   після повернення до програми, користувач продовжить працювати у активній сесії без втрати прогресу
     */
    private boolean activityProtection;

    /*
     * За замовчуванням рекомендується тримати цю опцію увімкненою
     */
    private static  final boolean DEFAULT_ACTIVITY_PROTECTION = true;

    public Settings(boolean activityProtection) {
        this.activityProtection = activityProtection;
    }

    public boolean getActivityProtection() { return activityProtection; }
}
