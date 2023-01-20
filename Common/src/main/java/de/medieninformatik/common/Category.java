package de.medieninformatik.common;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;

public class Category {
    public static final Category ALL;
    private Category[] categories;
    private int categoryId;
    private String name;

    static {
        ALL = new Category(0);
        ALL.setName("ALL");
        ALL.setCategories(new Category[0]);
    }

    @JsonCreator
    public Category(@JsonProperty("categoryId") int categoryId) {
        this.categoryId = categoryId;
    }

    @JsonGetter("categories")
    public Category[] getCategories() {
        return categories;
    }

    @JsonSetter("categories")
    public void setCategories(Category[] categories) {
        this.categories = categories;
    }

    @JsonGetter("categoryId")
    public int getCategoryId() {
        return categoryId;
    }

    @JsonSetter("categoryId")
    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    @JsonGetter("name")
    public String getName() {
        return name;
    }

    @JsonSetter("name")
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
