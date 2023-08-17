package com.example.passwordstorage.model;

/*
    Даний клас є другорядною структурною одиницею сховища - категорією записів
 */
public class Category {

    private String name;

    public static final Integer MAX_NAME_LENGTH = 20;

    public Category(String name) {
        this.name = name;
    }

    public String getName() { return name; }

    @Override
    public String toString() {
        return name;
    }
}
