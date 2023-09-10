package com.example.passwordstorage.model;

/*
 *   Даний клас містить налаштування та не повинен
 *   існувати більш ніж в одному екземплярі
 */
public class Settings {
    public static final Integer MAX_PASSWORD_LENGTH = 10;

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
     * Пароль для входу у програму
     */
    private String password;

    /*
     * За замовчуванням рекомендується тримати ці опції у таких станах:
     */
    private static  final boolean DEFAULT_ACTIVITY_PROTECTION = true;
    private static final boolean DEFAULT_INPUT_PASS_CLEARING = false;


    public Settings(String password) {
        activityProtection = DEFAULT_ACTIVITY_PROTECTION;
        inputPassClearing = DEFAULT_INPUT_PASS_CLEARING;
        this.password = password;
    }

    public Settings(boolean activityProtection, boolean inputPassClearing, String password) {
        this.activityProtection = activityProtection;
        this.inputPassClearing = inputPassClearing;
        this.password = password;
    }

    public boolean getActivityProtection() { return activityProtection; }
    public boolean getInputPassClearing() { return inputPassClearing; }
    public String getPassword() { return password; }

    public void resetActivityProtection() {
        activityProtection = !activityProtection;
    }
    public void resetInputPassClearing() { inputPassClearing = !inputPassClearing; }

    public void setPassword(String password) {
        this.password = password;
    }
}
