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
     * Змінна зберігає стан фільтру порядку сортування
     *
     *      true    -   від найстаріших
     *      false   -   від найновіших
     */
    private boolean filtersSortMode;

    /*
     * Змінна зберігає стан фільтру критерію сортування
     *
     *      1 - за датою редагування
     *      2 - за датою перегляду
     *      3 - за датою створення
     */
    private int filtersSortParam;

    /*
     * За замовчуванням рекомендується тримати ці опції у таких станах:
     */
    private static  final boolean DEFAULT_ACTIVITY_PROTECTION = true;
    private static final boolean DEFAULT_INPUT_CALC_CLEARING = false;
    private static final boolean DEFAULT_DIGITAL_OWNER = false;


    public Settings(String password, boolean filtersSortMode, int filtersSortParam) {
        activityProtection = DEFAULT_ACTIVITY_PROTECTION;
        inputCalcClearing = DEFAULT_INPUT_CALC_CLEARING;
        this.password = password;
        digitalOwner = DEFAULT_DIGITAL_OWNER;
        this.filtersSortMode = filtersSortMode;
        this.filtersSortParam = filtersSortParam;
    }

    public Settings(boolean activityProtection, boolean inputPassClearing, String password, boolean digitalOwner, boolean filtersSortMode, int filtersSortParam) {
        this.activityProtection = activityProtection;
        this.inputCalcClearing = inputPassClearing;
        this.password = password;
        this.digitalOwner = digitalOwner;
        this.filtersSortMode = filtersSortMode;
        this.filtersSortParam = filtersSortParam;
    }

    public boolean getActivityProtection() { return activityProtection; }
    public boolean getInputCalcClearing() { return inputCalcClearing; }
    public String getPassword() { return password; }
    public boolean getDigitalOwner() { return digitalOwner; }
    public boolean getFiltersSortMode() { return  filtersSortMode; }
    public int getFiltersSortParam() { return filtersSortParam; }

    public void resetActivityProtection() {
        activityProtection = !activityProtection;
    }
    public void resetInputCalcClearing() { inputCalcClearing = !inputCalcClearing; }
    public void resetDigitalOwner() { digitalOwner = !digitalOwner; }

    public void setPassword(String password) {
        this.password = password;
    }
    public void setFiltersSortMode(boolean filtersSortMode) { this.filtersSortMode = filtersSortMode; }
    public void setFiltersSortParam(int filtersSortParam) { this.filtersSortParam = filtersSortParam; }
}
