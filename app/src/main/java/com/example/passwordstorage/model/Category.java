package com.example.passwordstorage.model;

/*
    Даний клас є другорядною структурною одиницею сховища - категорією записів
 */
public class Category {

    private String name;

    public Category(String name) {
        this.name = name;
    }

    public String getName() { return name; }
}
