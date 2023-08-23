package com.example.passwordstorage.model;

/*
    Даний клас є основною структурною одиницею сховища - записом
 */
public class Record {

    private String title;
    private String text;

    /*
     *  Відсутність значення у даному полі слід позначати як NULL_CATEGORY_VALUE (-1)
     */
    private Integer category_id;

    /*
     *  True - запис додано до закладок
     *  false - запис не додано до закладок
     */
    private Boolean bookmark;

    public static final Integer NULL_CATEGORY_VALUE = -1;
    public static final boolean NULL_BOOKMARK_VALUE = false;

    public static final Integer MAX_TITLE_LENGTH = 20;
    public static final Integer MAX_TEXT_LENGTH = 100;

    public Record(String title, String text, Integer category_id) {
        this(title, text, category_id, NULL_BOOKMARK_VALUE);
    }

    public Record(String title, String text, Integer category_id, Boolean bookmark) {
        this.title = title;
        this.text = text;
        this.category_id = category_id;
        this.bookmark = bookmark;
    }

    public void setEmptyCategoryId() {
        this.category_id = NULL_CATEGORY_VALUE;
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

    public boolean getBookmark() {
        return bookmark;
    }

    public void update(String newTitle, String newText, Integer newCategory_id) {
        this.title = newTitle;
        this.text = newText;
        this.category_id = newCategory_id;
    }

    public void inversionBookmark() {
        bookmark = !bookmark;
    }
}