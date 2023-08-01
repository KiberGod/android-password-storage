package com.example.passwordstorage;

public class Record {

    private String title;
    private String text;
    private String category;

    public Record(String title, String text, String category) {
        this.title = title;
        this.text = text;
        this.category = category;
    }

    public String getTitle() {
        return title;
    }

    public String getText() {
        return text;
    }

    public String getCategory() {
        return category;
    }
}