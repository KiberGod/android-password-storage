package com.kibergod.passwordstorage.model;

/*
 *   Даний клас містить певні дані, пов`язані з калькулятором та не повинен
 *   існувати більш ніж в одному екземплярі
 */
public class Calculator {
    private String expression;

    public Calculator(String expression) {
        this.expression = expression;
    }

    public String getExpression() { return expression; }
}
