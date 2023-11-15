package com.kibergod.passwordstorage.model;

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
     * Змінна вказує, чи необхідно очистити введені дані у калькуляторі
     *
     *      true    -   так, необхідно очистити
     *      false   -   ні, необхідно залишити
     */
    private boolean inputCalcClearing;

    /*
     * Пароль для входу у програму
     */
    private String password;

    /*
     * Змінна вказує, у якому стані знаходиться "Цифровий власник"
     *
     *      true    -   увімкнений
     *      false   -   вимкнутий
     */
    private boolean digitalOwner;

    /*
     * За замовчуванням рекомендується тримати ці опції у таких станах:
     */
    private static  final boolean DEFAULT_ACTIVITY_PROTECTION = true;
    private static final boolean DEFAULT_INPUT_CALC_CLEARING = false;
    private static final boolean DEFAULT_DIGITAL_OWNER = false;


    public Settings(String password) {
        activityProtection = DEFAULT_ACTIVITY_PROTECTION;
        inputCalcClearing = DEFAULT_INPUT_CALC_CLEARING;
        this.password = password;
        digitalOwner = DEFAULT_DIGITAL_OWNER;
    }

    public Settings(boolean activityProtection, boolean inputPassClearing, String password, boolean digitalOwner) {
        this.activityProtection = activityProtection;
        this.inputCalcClearing = inputPassClearing;
        this.password = password;
        this.digitalOwner = digitalOwner;
    }

    public boolean getActivityProtection() { return activityProtection; }
    public boolean getInputCalcClearing() { return inputCalcClearing; }
    public String getPassword() { return password; }
    public boolean getDigitalOwner() { return digitalOwner; }

    public void resetActivityProtection() {
        activityProtection = !activityProtection;
    }
    public void resetInputCalcClearing() { inputCalcClearing = !inputCalcClearing; }
    public void resetDigitalOwner() { digitalOwner = !digitalOwner; }

    public void setPassword(String password) {
        this.password = password;
    }
}
