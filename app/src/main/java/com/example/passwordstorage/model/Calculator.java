package com.example.passwordstorage.model;

/*
 *   Даний клас містить певні дані, пов`язані з калькулятором та не повинен
 *   існувати більш ніж в одному екземплярі
 */
public class Calculator {
    private String number1;
    private String number2;
    private Character operation;

    public Calculator(String number1, String number2, Character operation) {
        this.number1 = number1;
        this.number2 = number2;
        this.operation = operation;
    }

    public String getNumber1() { return number1; }
    public String getNumber2() { return number2; }
    public Character getOperation() { return operation; }
}
