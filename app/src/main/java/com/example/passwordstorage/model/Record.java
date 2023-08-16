package com.example.passwordstorage.model;

/*
    Даний клас є основною структурною одиницею сховища - записом
 */
public class Record {

    private String title;
    private String text;
    private Integer category_id;

    public Record(String title, String text, Integer category_id) {
        this.title = title;
        this.text = text;
        this.category_id = category_id;
    }

    public String getTitle() {
        return title;
    }

    public String getText() {
        return text;
    }

    public Integer getCategoryId() {
        return category_id;
    }
}