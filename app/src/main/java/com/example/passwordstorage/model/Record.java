package com.example.passwordstorage.model;

import java.util.ArrayList;

/*
    Даний клас є основною структурною одиницею сховища - записом
 */
public class Record {

    /*
     * Клас поля
     */
    public class Field {

        public static final int MAX_NAME_LENGTH = 20;
        public static final int MAX_VALUE_LENGTH = 80;

        public static  final boolean DEFAULT_VALUE_VISIBILITY = false;

        private String name;
        private String value;

        private boolean valueVisibility;

        public Field(String name, String value) {
            this(name, value, DEFAULT_VALUE_VISIBILITY);
        }

        public Field(String name, String value, Boolean valueVisibility) {
            this.name = name;
            this.value = value;
            this.valueVisibility = valueVisibility;
        }

        public String getName() { return name; }

        public String getValue() { return value; }

        public String getProtectedgetValue() {
            if (this.valueVisibility == true) {
                return value;
            } else {
                StringBuilder protectedStr = new StringBuilder();
                for (int i = 0; i < value.length(); i++) {
                    protectedStr.append('*');
                }
                return protectedStr.toString();
            }
        }

        public boolean getValueVisibility() { return valueVisibility; }

        public void inversionValueVisibility() {
            valueVisibility = !valueVisibility;
        }
    }

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

    private int icon_id;

    private Field[] fields;


    public static final int NULL_ICON_ID_VALUE = -1;
    public static final Integer NULL_CATEGORY_VALUE = -1;
    public static final boolean NULL_BOOKMARK_VALUE = false;

    public static final Integer MAX_FIELDS_LENGTH = 10;
    public static final Integer MAX_TITLE_LENGTH = 20;
    public static final Integer MAX_TEXT_LENGTH = 100;

    private void init(String title, String text, Integer category_id, Boolean bookmark, int icon_id, Field[] fields) {
        this.title = title;
        this.text = text;
        this.category_id = category_id;
        this.bookmark = bookmark;
        this.icon_id = icon_id;
        this.fields = fields;
    }

    public Record(String title, String text, Integer category_id, int icon_id) {
        init(title, text, category_id, NULL_BOOKMARK_VALUE, icon_id, getEmptyFields());
    }

    public Record(String title, String text, Integer category_id, Boolean bookmark, int icon_id, Field[] fields) {
        init(title, text, category_id, bookmark, icon_id, fields);
    }

    public Record(String title, String text, Integer category_id, Boolean bookmark, int icon_id) {
        init(title, text, category_id, bookmark, icon_id, getEmptyFields());
    }


    public static int getMaxFieldNameLength() { return Field.MAX_NAME_LENGTH; }
    public static int getMaxFieldValueLength() { return Field.MAX_VALUE_LENGTH; }

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

    public int getIconId() { return icon_id; }

    public Field[] getFields() { return fields; }

    private Field[] getEmptyFields() {
        Field[] fields = new Field[MAX_FIELDS_LENGTH];
        for (int i = 0; i < fields.length; i++) {
            fields[i] = new Field("", "");
        }
        return fields;
    }


    public void setEmptyCategoryId() {
        this.category_id = NULL_CATEGORY_VALUE;
    }
    public void setFields(Field[] fields) { this.fields = fields; }
    public void setFields(ArrayList<String> names, ArrayList<String> values) {
        for (int i=0; i<names.size(); i++) {
            fields[i] = createField(names.get(i), values.get(i), Field.DEFAULT_VALUE_VISIBILITY);
        }
    }

    public Field createField(String name, String value, Boolean valueVisibility) { return new Field(name, value, valueVisibility); }

    public void update(String newTitle, String newText, Integer newCategory_id, int newIcon_id, ArrayList<String> newNames, ArrayList<String> newValues) {
        this.title = newTitle;
        this.text = newText;
        this.category_id = newCategory_id;
        this.icon_id = newIcon_id;
        this.fields = getEmptyFields();
        setFields(newNames, newValues);
    }

    public void inversionBookmark() {
        bookmark = !bookmark;
    }

}