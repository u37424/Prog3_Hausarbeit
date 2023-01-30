package de.medieninformatik.common;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;

public class Category {
    private int categoryId;
    private String name;

    public Category(){
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
