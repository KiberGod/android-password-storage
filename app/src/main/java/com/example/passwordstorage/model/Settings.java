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
     * Змінна вказує, чи необхідно очистити "сміття", що утворюється під час введення паролю у калькуляторі
     *
     *      true    -   так, необхідно очистити
     *      false   -   ні, необхідно залишити
     */
    private boolean inputPassClearing;

    /*
     * За замовчуванням рекомендується тримати ці опції у таких станах:
     */
    private static  final boolean DEFAULT_ACTIVITY_PROTECTION = true;
    private static final boolean DEFAULT_INPUT_PASS_CLEARING = false;

    public Settings(boolean activityProtection, boolean inputPassClearing) {
        this.activityProtection = activityProtection;
        this.inputPassClearing = inputPassClearing;
    }

    public boolean getActivityProtection() { return activityProtection; }
    public boolean getInputPassClearing() { return inputPassClearing; }

    public void resetActivityProtection() {
        activityProtection = !activityProtection;
    }
    public void resetInputPassClearing() { inputPassClearing = !inputPassClearing; }
}
