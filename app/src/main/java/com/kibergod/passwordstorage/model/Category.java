package com.kibergod.passwordstorage.model;

/*
    Даний клас є другорядною структурною одиницею сховища - категорією записів
 */
public class Category {

    private Integer id;
    private String name;
    private String icon_id;
    private DateTime created_at;
    private DateTime updated_at;
    private DateTime viewed_at;

    public static final String NULL_ICON_ID_VALUE = "vector_template_image";

    public static final Integer MAX_NAME_LENGTH = 40;

    public Category(Integer id, String name) {
        init(id, name, NULL_ICON_ID_VALUE, new DateTime(), new DateTime(), new DateTime());
    }

    public Category(Integer id, String name, String icon_id, DateTime created_at, DateTime updated_at, DateTime viewed_at) {
        init(id, name, icon_id, created_at, updated_at, viewed_at);
    }

    private void init(Integer id, String name, String icon_id, DateTime created_at, DateTime updated_at, DateTime viewed_at) {
        this.id = id;
        this.name = name;
        this.icon_id = icon_id;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.viewed_at = viewed_at;
    }

    public String getName() { return name; }

    public Integer getId() { return id; }

    public String getIconId() { return icon_id; }

    public String getCreated_at() {return created_at.getTimestamp(); }
    public String getUpdated_at() {return updated_at.getTimestamp(); }
    public String getViewed_at() {return viewed_at.getTimestamp(); }
    public long getCreated_atInMillis() {return created_at.getDateTimeInMilliseconds(); }
    public long getUpdated_atInMillis() {return updated_at.getDateTimeInMilliseconds(); }
    public long getViewed_atInMillis() {return viewed_at.getDateTimeInMilliseconds(); }

    public void setUpdated_at() { updated_at.update(); }
    public void setViewed_at() { viewed_at.update(); }

    @Override
    public String toString() {
        return name;
    }

    public void update(String name, String icon_id) {
        this.name = name;
        this.icon_id = icon_id;
    }
}