package com.kibergod.passwordstorage.model;

/*
    Даний клас є другорядною структурною одиницею сховища - категорією записів
 */
public class Category {

    private Integer id;
    private String name;
    private String icon_id;
    private DateTime created_at;

    public static final String NULL_ICON_ID_VALUE = "vector_template_image";

    public static final Integer MAX_NAME_LENGTH = 20;

    public Category(Integer id, String name) {
        DateTime created_at = new DateTime();
        init(id, name, NULL_ICON_ID_VALUE, created_at);
    }

    public Category(Integer id, String name, String icon_id, DateTime created_at) {
        init(id, name, icon_id, created_at);
    }

    private void init(Integer id, String name, String icon_id, DateTime created_at) {
        this.id = id;
        this.name = name;
        this.icon_id = icon_id;
        this.created_at = created_at;
    }

    public String getName() { return name; }

    public Integer getId() { return id; }

    public String getIconId() { return icon_id; }

    public String getCreated_at() {return created_at.getTimestamp(); }

    @Override
    public String toString() {
        return name;
    }

    public void update(String name, String icon_id) {
        this.name = name;
        this.icon_id = icon_id;
    }
}
