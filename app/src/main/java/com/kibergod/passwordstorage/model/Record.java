package com.kibergod.passwordstorage.model;

import java.util.ArrayList;

/*
    Даний клас є основною структурною одиницею сховища - записом
 */
public class Record {

    /*
     * Клас поля
     */
    public class Field {

        public static final int MAX_NAME_LENGTH = 40;
        public static final int MAX_VALUE_LENGTH = 100;

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

        public String getProtectedFieldValue() {
            if (this.valueVisibility && !totalValueVisibility) {
                return value;
            } else {
                return getProtectedValue(value);
            }
        }

        public boolean getValueVisibility() { return valueVisibility; }

        public void inversionValueVisibility() {
            valueVisibility = !valueVisibility;
        }
    }

    private int id;
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

    private String icon_id;

    private Field[] fields;

    private DateTime created_at;
    private DateTime updated_at;
    private DateTime viewed_at;
    private DateTime deleted_at;

    private boolean totalValueVisibility;

    /*
     *  True - запис приховано
     *  false - запис не приховано
     */
    private boolean hidden;

    public static final String NULL_ICON_ID_VALUE = "vector_template_image";
    public static final Integer NULL_CATEGORY_VALUE = -1;
    public static final boolean NULL_BOOKMARK_VALUE = false;
    public static  final boolean DEFAULT_TOTAL_VALUE_VISIBILITY = false;
    public static  final boolean DEFAULT_HIDDEN = false;

    public static final Integer MAX_FIELDS_LENGTH = 10;
    public static final Integer MAX_TITLE_LENGTH = 40;
    public static final Integer MAX_TEXT_LENGTH = 1000;

    private void init(int id, String title, String text, Integer category_id, Boolean bookmark, String icon_id, Field[] fields, DateTime created_at,
                      DateTime updated_at, DateTime viewed_at, boolean totalValueVisibility, DateTime deleted_at, boolean hidden) {
        this.id = id;
        this.title = title;
        this.text = text;
        this.category_id = category_id;
        this.bookmark = bookmark;
        this.icon_id = icon_id;
        this.fields = fields;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.viewed_at = viewed_at;
        this.totalValueVisibility = totalValueVisibility;
        this.deleted_at = deleted_at;
        this.hidden = hidden;
    }

    public Record(int id, String title, String text, Integer category_id, String icon_id) {
        init(id, title, text, category_id, NULL_BOOKMARK_VALUE, icon_id, getEmptyFields(), new DateTime(), new DateTime(), new DateTime(), DEFAULT_TOTAL_VALUE_VISIBILITY, new DateTime(0,0,0,0,0), DEFAULT_HIDDEN);
    }

    public Record(int id, String title, String text, Integer category_id, Boolean bookmark, String icon_id, DateTime created_at, DateTime updated_at, DateTime viewed_at, boolean totalValueVisibility, DateTime deleted_at, boolean hidden) {
        init(id, title, text, category_id, bookmark, icon_id, getEmptyFields(), created_at, updated_at, viewed_at, totalValueVisibility, deleted_at, hidden);
    }

    public static int getMaxFieldNameLength() { return Field.MAX_NAME_LENGTH; }
    public static int getMaxFieldValueLength() { return Field.MAX_VALUE_LENGTH; }

    public int getId() { return id; }

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

    public String getIconId() { return icon_id; }

    public Field[] getFields() { return fields; }

    private Field[] getEmptyFields() {
        Field[] fields = new Field[MAX_FIELDS_LENGTH];
        for (int i = 0; i < fields.length; i++) {
            fields[i] = new Field("", "");
        }
        return fields;
    }

    public String getCreated_at() {return created_at.getTimestamp(); }
    public String getUpdated_at() {return updated_at.getTimestamp(); }
    public String getViewed_at() {return viewed_at.getTimestamp(); }
    public String getDeleted_at() {return deleted_at.getTimestamp(); }
    public DateTime getDeleted_atObj() {return deleted_at; }
    public boolean isDeleted_at() {return !deleted_at.isEmpty(); }
    public long getCreated_atInMillis() {return created_at.getDateTimeInMilliseconds(); }
    public long getUpdated_atInMillis() {return updated_at.getDateTimeInMilliseconds(); }
    public long getViewed_atInMillis() {return viewed_at.getDateTimeInMilliseconds(); }
    public boolean getTotalValueVisibility() { return totalValueVisibility; }
    public boolean getHidden() { return  hidden; }

    public void setUpdated_at() { updated_at.update(); }
    public void setViewed_at() { viewed_at.update(); }
    public void setDeleted_at() { deleted_at.update(); }
    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }
    public void restore() { deleted_at.edit(0,0,0,0,0);}

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

    public void update(String newTitle, String newText, Integer newCategory_id, String newIcon_id, ArrayList<String> newNames, ArrayList<String> newValues) {
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

    public void inversionTotalValueVisibility() { totalValueVisibility = !totalValueVisibility; }

    public static String getProtectedValue(String value) {
        StringBuilder protectedStr = new StringBuilder();
        for (int i = 0; i < value.length(); i++) {
            protectedStr.append('*');
        }
        return protectedStr.toString();
    }

    public void deleteBookmark() {
        bookmark = false;
    }
}