package com.example.passwordstorage.model;

/*
    Даний клас є основною структурною одиницею сховища - записом
 */
public class Record {

    private String title;
    private String text;

    /*
     * Відсутність значення у даному полі слід позначати як NULL_CATEGORY_VALUE (-1)
     */
    private Integer category_id;

    public static final Integer NULL_CATEGORY_VALUE = -1;
    public static final Integer MAX_TITLE_LENGTH = 20;
    public static final Integer MAX_TEXT_LENGTH = 100;

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

    public void update(String newTitle, String newText, Integer newCategory_id) {
        this.title = newTitle;
        this.text = newText;
        this.category_id = newCategory_id;
    }
}