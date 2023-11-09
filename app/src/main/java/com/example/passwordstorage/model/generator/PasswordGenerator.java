package com.example.passwordstorage.model.generator;


import java.util.Random;

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
        public static final int DEFAULT_LENGTH = 0;

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
         *      false   -   кількість символів сету, якій необхідно увійти до паролю, визначається випадково
         *      true   -   кількість символів сету задаєтсья користувачем через налаштування SymbolSet.length
         */
        private boolean randomLength;

        /*
         * Вказує кількість символів сету, які повинні увійти до паролю.
         * якщо randomLength = false, то буде згенеровано автоматично
         */
        private int length;

        public SymbolSet(int type) {
            this(type, DEFAULT_USAGE, DEFAULT_RANDOM_LENGTH, DEFAULT_LENGTH);
        }

        public SymbolSet(int type, boolean usage, boolean randomLength, int length) {
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
        public void inversionRandomLength() { randomLength = !randomLength; length = DEFAULT_LENGTH;}
        public void setLength(int length) { this.length = length; }

    }

    private static final int MAX_LENGTH = 100;
    private static final int MIN_LENGTH = 1;
    private static final int DEFAULT_LENGTH = 10;
    private static final String DEFAULT_NOT_USE_SYMBOLS = "@%*^#&";
    private static final int MAX_NOT_USE_SYMBOLS = 50;

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

    public PasswordGenerator() {
        init(DEFAULT_LENGTH, getDefaultSymbolSets(), DEFAULT_NOT_USE_SYMBOLS);
    }

    public PasswordGenerator(int length, SymbolSet[] symbolSets, String notUseSymbols) {
        init(length, symbolSets, notUseSymbols);
    }

    public int getMinLength() { return MIN_LENGTH; }
    public int getMaxLength() { return MAX_LENGTH; }
    public int getLength() { return length; }
    public void setLength(int length) { this.length = length; }
    public int getMaxNotUseSymbols() { return MAX_NOT_USE_SYMBOLS; }
    public String getNotUseSymbols() { return notUseSymbols; }
    public void setNotUseSymbols(String notUseSymbols) { this.notUseSymbols = notUseSymbols; }
    public int getNumberTypes() { return NUMBER_TYPES; }
    public SymbolSet[] getSymbolSets() { return symbolSets; }

    // Створює набори сетів з дефолтними налаштуваннями
    private SymbolSet[] getDefaultSymbolSets() {
        SymbolSet[] symbolSets = new SymbolSet[NUMBER_TYPES];
        for (int type=0; type<NUMBER_TYPES; type++) {
            symbolSets[type] = new SymbolSet(type);
        }
        return symbolSets;
    }

    /*
     * Повертає сет з лише дозволеними символами для генерації паролю
     */
    private String getValidSymbolsSet(String set) {
        StringBuilder validSymbols = new StringBuilder();
        for (char ch : set.toCharArray()) {
            if (notUseSymbols.indexOf(ch) == -1) {
                validSymbols.append(ch);
            }
        }
        return validSymbols.toString();
    }

    /*
     * Функція перевіряє та корегує довжини сетів, щоб ті у суммі не перебільшили загальну довжину паролю
     */
    public void reduceSymbolsSetLength() {
        int totalSetsLen = getTotalLength();

        if (totalSetsLen < MIN_LENGTH) {
            symbolSets[0].setLength(DEFAULT_LENGTH);
            return;
        } else if (totalSetsLen > length) {
            while (true) {
                decrementMaxSymbolSetLength();
                totalSetsLen--;
                if (totalSetsLen == length) {
                    break;
                }
            }
        }
    }

    /*
     * Функція автоматично збільшує довжини сетів при збільшенні загальної довжини паролю
     */
    public void increaseSymbolsSetLength() {
        int totalSetsLen = getTotalLength();

        if (totalSetsLen < MIN_LENGTH) {
            return;
        } else if (totalSetsLen < length) {
            while (true) {
                incrementMinSymbolSetLength();
                totalSetsLen++;
                if (totalSetsLen == length) {
                    break;
                }
            }
        }
    }

    private void decrementMaxSymbolSetLength() {
        int maxLength = MIN_LENGTH-1, index = -1;
        for (SymbolSet symbolSet: symbolSets) {
            if (!symbolSet.isUsage() && !symbolSet.isRandomLength()) {
                if (maxLength < symbolSet.getLength()) {
                    maxLength = symbolSet.getLength();
                    index = symbolSet.getType();
                }
            }
        }
        symbolSets[index].setLength(symbolSets[index].getLength() -1);
    }

    private void incrementMinSymbolSetLength() {
        int minLength = MAX_LENGTH+1, index = -1;
        for (SymbolSet symbolSet: symbolSets) {
            if (!symbolSet.isUsage() && !symbolSet.isRandomLength()) {
                if (minLength > symbolSet.getLength() && symbolSet.getLength() != 0) {
                    minLength = symbolSet.getLength();
                    index = symbolSet.getType();
                }
            }
        }
        symbolSets[index].setLength(symbolSets[index].getLength() +1);
    }

    // Повертає загальну кількість символів активних сетів
    private int getTotalLength() {
        int totalSetsLen = 0;
        for (SymbolSet symbolSet: symbolSets) {
            if (!symbolSet.isUsage() && !symbolSet.isRandomLength()) {
                totalSetsLen += symbolSet.getLength();
            } else {
                symbolSet.setLength(SymbolSet.DEFAULT_LENGTH);
            }
        }
        return totalSetsLen;
    }

    // Генерація випадкових довжин активних символьних сетів перед генерацією пароля
    private void randomGenerateSymbolSetsLength() {
        int totalRandLengths = 0, clearLen = length - getTotalLength();
        for (int i = 0; i < symbolSets.length; i++) {
            if (!symbolSets[i].isUsage() && symbolSets[i].isRandomLength()) {
                int newLen = generateRandomSetLength(clearLen - totalRandLengths);
                totalRandLengths = totalRandLengths + newLen;
                symbolSets[i].setLength(newLen);
            }
        }
        if (clearLen - totalRandLengths != 0) {
            for (int i = 0; i < symbolSets.length; i++) {
                if (!symbolSets[i].isUsage() && symbolSets[i].isRandomLength()) {
                    symbolSets[i].setLength(
                            symbolSets[i].getLength() + clearLen - totalRandLengths
                    );
                    break;
                }
            }
        }
    }

    // Повертая випадкову довжину для символьного сету
    private int generateRandomSetLength(int maxValue) {
        Random random = new Random();
        return random.nextInt(maxValue+1);
    }


    /*
     * Генератор пароля
     */
    public String generatePassword() {
        randomGenerateSymbolSetsLength();

        StringBuilder passwordBuilder = new StringBuilder();
        Random random = new Random();

        for (SymbolSet symbolSet : symbolSets) {
            if (!symbolSet.isUsage()) {
                String symbols = getValidSymbolsSet(symbolSet.getSymbols());
                if (symbols.length() > 0) {
                    for (int i = 0; i < symbolSet.getLength(); i++) {
                        passwordBuilder.append(symbols.charAt(random.nextInt(symbols.length())));
                    }
                }
            }
        }

        String password = passwordBuilder.toString();
        char[] passwordChars = password.toCharArray();
        for (int i = 0; i < passwordChars.length; i++) {
            int randomIndex = random.nextInt(passwordChars.length);
            char temp = passwordChars[i];
            passwordChars[i] = passwordChars[randomIndex];
            passwordChars[randomIndex] = temp;
        }

        password = new String(passwordChars);

        return password;
    }
}
