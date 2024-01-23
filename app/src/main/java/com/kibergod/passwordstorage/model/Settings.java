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
     *  Шрифти (у dp)
     */
    private int fontSizeMain;
    private int fontSizeInput;
    private int fontSizeButtons;
    private int fontSizeLargeButtons;
    private int fontSizeFieldCaptions;
    private int fontSizeOther;
    private int fontSizeRssMain;
    private int fontSizeRssSecondary;

    // Рядок калькулятора
    private String calcExpression;

    /*
     * За замовчуванням рекомендується тримати ці опції у таких станах:
     */
    private static  final boolean DEFAULT_ACTIVITY_PROTECTION = true;
    private static final boolean DEFAULT_INPUT_CALC_CLEARING = false;
    private static final boolean DEFAULT_DIGITAL_OWNER = false;
    private static final int DEFAULT_FONT_SIZE_MAIN = 18;
    private static final int DEFAULT_FONT_SIZE_INPUT = 18;
    private static final int DEFAULT_FONT_SIZE_BUTTONS = 18;
    private static final int DEFAULT_FONT_SIZE_LARGE_BUTTONS = 14;
    private static final int DEFAULT_FONT_SIZE_FIELD_CAPTIONS = 14;
    private static final int DEFAULT_FONT_SIZE_OTHER = 16;
    private static final int DEFAULT_FONT_SIZE_RSS_MAIN = 16;
    private static final int DEFAULT_FONT_SIZE_RSS_SECONDARY = 12;
    public static final int MIN_FONT_SIZE = 10;
    public static final int MAX_FONT_SIZE = 30;
    public static final int MAX_CALC_EXPRESSION = 200;

    public Settings(String password, boolean filtersSortMode, int filtersSortParam) {
        activityProtection = DEFAULT_ACTIVITY_PROTECTION;
        inputCalcClearing = DEFAULT_INPUT_CALC_CLEARING;
        this.password = password;
        digitalOwner = DEFAULT_DIGITAL_OWNER;
        this.filtersSortMode = filtersSortMode;
        this.filtersSortParam = filtersSortParam;
        fontSizeMain = DEFAULT_FONT_SIZE_MAIN;
        fontSizeInput = DEFAULT_FONT_SIZE_INPUT;
        fontSizeButtons = DEFAULT_FONT_SIZE_BUTTONS;
        fontSizeLargeButtons = DEFAULT_FONT_SIZE_LARGE_BUTTONS;
        fontSizeFieldCaptions = DEFAULT_FONT_SIZE_FIELD_CAPTIONS;
        fontSizeOther = DEFAULT_FONT_SIZE_OTHER;
        fontSizeRssMain = DEFAULT_FONT_SIZE_RSS_MAIN;
        fontSizeRssSecondary = DEFAULT_FONT_SIZE_RSS_SECONDARY;
        calcExpression = "";
    }

    public Settings(boolean activityProtection, boolean inputPassClearing, String password,
                    boolean digitalOwner, boolean filtersSortMode, int filtersSortParam,
                    int fontSizeMain, int fontSizeInput, int fontSizeButtons, int fontSizeLargeButtons,
                    int fontSizeFieldCaptions, int fontSizeOther, int fontSizeRssMain, int fontSizeRssSecondary, String calcExpression) {
        this.activityProtection = activityProtection;
        this.inputCalcClearing = inputPassClearing;
        this.password = password;
        this.digitalOwner = digitalOwner;
        this.filtersSortMode = filtersSortMode;
        this.filtersSortParam = filtersSortParam;
        this.fontSizeMain = fontSizeMain;
        this.fontSizeInput = fontSizeInput;
        this.fontSizeButtons = fontSizeButtons;
        this.fontSizeLargeButtons = fontSizeLargeButtons;
        this.fontSizeFieldCaptions = fontSizeFieldCaptions;
        this.fontSizeOther = fontSizeOther;
        this.fontSizeRssMain = fontSizeRssMain;
        this.fontSizeRssSecondary = fontSizeRssSecondary;
        this.calcExpression = calcExpression;
    }

    public boolean getActivityProtection() { return activityProtection; }
    public boolean getInputCalcClearing() { return inputCalcClearing; }
    public String getPassword() { return password; }
    public boolean getDigitalOwner() { return digitalOwner; }
    public boolean getFiltersSortMode() { return  filtersSortMode; }
    public int getFiltersSortParam() { return filtersSortParam; }

    public int getFontSizeMain() { return fontSizeMain; }
    public int getFontSizeInput() {return fontSizeInput; }
    public int getFontSizeButtons() { return fontSizeButtons; }
    public int getFontSizeLargeButtons() { return fontSizeLargeButtons; }
    public int getFontSizeFieldCaptions() { return fontSizeFieldCaptions; }
    public int getFontSizeOther() { return fontSizeOther; }
    public int getFontSizeRssMain() { return fontSizeRssMain; }
    public int getFontSizeRssSecondary() { return fontSizeRssSecondary; }
    public String getCalcExpression() { return calcExpression; }

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
    public void setFontSizeMain(int fontSizeMain) { this.fontSizeMain = fontSizeMain; }
    public void setFontSizeInput(int fontSizeInput) { this.fontSizeInput = fontSizeInput; }
    public void setFontSizeButtons(int fontSizeButtons) { this.fontSizeButtons = fontSizeButtons; }
    public void setFontSizeLargeButtons(int fontSizeLargeButtons) { this.fontSizeLargeButtons = fontSizeLargeButtons; }
    public void setFontSizeFieldCaptions(int fontSizeFieldCaptions) { this.fontSizeFieldCaptions = fontSizeFieldCaptions; }
    public void setFontSizeOther(int fontSizeOther) { this.fontSizeOther = fontSizeOther; }
    public void setFontSizeRssMain(int fontSizeRssMain) { this.fontSizeRssMain = fontSizeRssMain; }
    public void setFontSizeRssSecondary(int fontSizeRssSecondary) { this.fontSizeRssSecondary = fontSizeRssSecondary; }
    public void setCalcExpression(String calcExpression) {
        if (calcExpression.length() < MAX_CALC_EXPRESSION) {
            this.calcExpression = calcExpression;
        } else {
            this.calcExpression = "";
        }
    }
}