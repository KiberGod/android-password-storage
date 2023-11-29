package com.kibergod.passwordstorage.model;

/*
    Даний клас є другорядною структурною одиницею сховища - категорією записів
 */
public class Category {

    private Integer id;
    private String name;
    private int icon_id;

    public static final int NULL_ICON_ID_VALUE = -1;

    public static final Integer MAX_NAME_LENGTH = 20;

    public Category(Integer id, String name) {
        this(id, name, NULL_ICON_ID_VALUE);
    }

    public Category(Integer id, String name, int icon_id) {
        this.id = id;
        this.name = name;
        this.icon_id = icon_id;
    }

    public String getName() { return name; }

    public Integer getId() { return id; }

    public int getIconId() { return icon_id; }

    @Override
    public String toString() {
        return name;
    }

    public void update(String name, int icon_id) {
        this.name = name;
        this.icon_id = icon_id;
    }

    public boolean hasIcon() {
        if (icon_id == NULL_ICON_ID_VALUE) {
            return false;
        }
        return true;
    }
}
