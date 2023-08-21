package com.example.passwordstorage.model;

/*
    Даний клас є другорядною структурною одиницею сховища - категорією записів
 */
public class Category {

    private Integer id;
    private String name;

    public static final Integer MAX_NAME_LENGTH = 20;

    public Category(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getName() { return name; }

    public Integer getId() { return id; }

    @Override
    public String toString() {
        return name;
    }
}
