package com.example.passwordstorage.model.generator;


public class PasswordGenerator {

    /*
     * Клас сету символів. Представляє собою набір налаштувань, які впливатимуть на генерацію пароля
     */
    public class SymbolSet {


        /*
        * Є 4 типи сетів символів:
        *
        *       1) цифри 0-9
        *       2) літери a-z
        *       3) великі літери A-Z
        *       4) спец. символи
        */
        public static final int DIGIT_TYPE = 0;
        public static final int LOWERCASE_SYMBOLS_TYPE = 1;
        public static final int UPPERCASE_SYMBOLS_TYPE = 2;
        public static final int SPECIAL_SYMBOLS_TYPE = 3;

        private static final boolean DEFAULT_USAGE = true;
        private static final boolean DEFAULT_RANDOM_LENGTH = true;
        private static final int DEFAULT_LENGTH = 0;

        // Тип сету, який характеризує символи, що містить у собі
        private int type;

        /* Налаштування, що вказує, чи необхідно додавати символи даного сету до загального набору символів генерації
         *
         *      true    -   так, сет необхідно включити у загальний набір
         *      false   -   ні, символи всього сету необхідно проігнорувати
         */
        private boolean usage;

        /*
         * Налаштування, що вказує, у який саме спосіб повинно бути задано кількість символів сету, які увійдуть до згенерованого паролю
         *
         *      true    -   кількість символів сету, якій необхідно увійти до паролю, визначається випадково
         *      false   -   кількість символів сету задаєтсья користувачем через налаштування SymbolSet.length
         */
        private boolean randomLength;

        /*
         * Вказує кількість символів сету, які повинні увійти до паролю.
         * якщо randomLength = true, то буде згенеровано автоматично
         */
        private int length;

        SymbolSet(int type) {
            this(type, DEFAULT_USAGE, DEFAULT_RANDOM_LENGTH, DEFAULT_LENGTH);
        }

        SymbolSet(int type, boolean usage, boolean randomLength, int length) {
            this.type = type;
            this.usage = usage;
            this.randomLength = randomLength;
            this.length = length;
        }

        /*
         * Повертає набір символів, в залежності від типу сету
         */
        public String getSymbols() {
            return new String[]{
                    "0123456789",
                    "abcdefghijklmnopqrstuvwxyz",
                    "ABCDEFGHIJKLMNOPQRSTUVWXYZ",
                    "!@#$%^&*()-_=+[]{}|;:'\",.<>?"
            }[type];
        }

        public int getType() { return type; }
        public boolean isUsage() { return usage; }
        public boolean isRandomLength() { return randomLength; }
        public int getLength() { return length; }


        public void setType(int type) { this.type = type; }
        public void inversionUsage() { usage = !usage; }
        public void inversionRandomLength() { randomLength = !randomLength; }
        public void setLength(int type) { this.type = type; }

    }

    private static final int MAX_LENGTH = 100;
    private static final int MIN_LENGTH = 1;
    private static final int DEFAULT_LENGTH = 10;
    private static final String DEFAULT_NOT_USE_SYMBOLS = "@%*^#&";

    private static final int NUMBER_TYPES = 4;

    // Довжина паролю
    private int length;

    // Набори сетів символів, що використовуються для генерації паролю
    private SymbolSet[] symbolSets;

    // Перелік символів, що неповинні використовуватись у процесі генерації паролю
    private String notUseSymbols;

    private void init(int length, SymbolSet[] symbolSets, String notUseSymbols) {
        if (length >= MIN_LENGTH && length <= MAX_LENGTH) {
            this.length = length;
        } else {
            this.length = DEFAULT_LENGTH;
        }
        this.symbolSets = symbolSets;
        this.notUseSymbols = notUseSymbols;
    }

    PasswordGenerator() {
        init(DEFAULT_LENGTH, getDefaultSymbolSets(), DEFAULT_NOT_USE_SYMBOLS);
    }

    PasswordGenerator(int length, SymbolSet[] symbolSets, String notUseSymbols) {
        init(length, symbolSets, notUseSymbols);
    }

    // Створює набори сетів з дефолтними налаштуваннями
    private SymbolSet[] getDefaultSymbolSets() {
        SymbolSet[] symbolSets = new SymbolSet[NUMBER_TYPES];
        for (int type=0; type<NUMBER_TYPES; type++) {
            symbolSets[type] = new SymbolSet(type);
        }
        return symbolSets;
    }

    /*
     * Повертає набір символів, дозволених для генерації паролю
     * шляхом відкидання неувімкнених сетів та окремо заборонених символів
     */
    private String getAllValidSymbols() {
        String allSymbols = "";
        for (SymbolSet symbolSet: symbolSets) {
            if (symbolSet.isUsage()) {
                allSymbols += symbolSet.getSymbols();
            }
        }

        StringBuilder allValidSymbols = new StringBuilder();
        for (char ch : allSymbols.toCharArray()) {
            if (notUseSymbols.indexOf(ch) == -1) {
                allValidSymbols.append(ch);
            }
        }

        return allValidSymbols.toString();
    }

    /*
     * Функція рекурсивно перевіряє та корегує довжини сетів, щоб ті у суммі не перебільшили загальну довжину паролю
     */
    private void checkOverLength(int type) {
        int totalSetsLength = 0;
        for (SymbolSet symbolSet: symbolSets) {
            if (symbolSet.isUsage()) {
                totalSetsLength += symbolSet.getLength();
            }
        }

        if (totalSetsLength > MAX_LENGTH) {
            int setIndex = getTrimSetIndex(type);
            symbolSets[setIndex].setLength(symbolSets[setIndex].getLength()-1);
            checkOverLength(type);
        }
    }

    /*
     * Функція повертає індекс сету символів з найдовшою довжиною, для подальшого корегування довжини шляхом її підрізання
     */
    private int getTrimSetIndex(int type) {
        int maxLen = 0, setIndex = -1;
        for (SymbolSet symbolSet: symbolSets) {
            if (symbolSet.isUsage() && symbolSet.getType() != type) {
                if (symbolSet.getLength() > maxLen) {
                    maxLen = symbolSet.getLength();
                    setIndex = symbolSet.getType();
                }
            }
        }
        return setIndex;
    }

    /*
     * Генератор пароля
     */
    //public String generatePassword(){}

}
