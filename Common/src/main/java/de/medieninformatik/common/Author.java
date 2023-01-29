package de.medieninformatik.common;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;

public class Author {
    private int authorId;
    private String firstName;
    private String lastName;
    private String alias;
    private String birthday;
    private int age;

    public Author(){
        firstName = "";
        lastName = "";
        alias = "";
        birthday = "";
    }

    @JsonGetter("authorId")
    public int getAuthorId() {
        return authorId;
    }

    @JsonSetter("authorId")
    public void setAuthorId(int authorId) {
        this.authorId = authorId;
    }

    @JsonGetter("firstName")
    public String getFirstName() {
        return firstName;
    }

    @JsonSetter("firstName")
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @JsonGetter("lastName")
    public String getLastName() {
        return lastName;
    }

    @JsonSetter("lastName")
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @JsonGetter("alias")
    public String getAlias() {
        return alias;
    }

    @JsonSetter("alias")
    public void setAlias(String alias) {
        this.alias = alias;
    }

    @JsonGetter("birthday")
    public String getBirthday() {
        return birthday;
    }

    @JsonSetter("birthday")
    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    @JsonGetter("age")
    public int getAge() {
        return age;
    }

    @JsonSetter("age")
    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return firstName+ " "+lastName;
    }
}
