package de.medieninformatik.common;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;

import java.util.LinkedList;

public class DBMeta {
    private LinkedList<Book> books;
    private LinkedList<Category> categories;
    private LinkedList<Author> authors;
    private LinkedList<Publisher> publishers;
    private int resultMax;

    public DBMeta(){
        books = new LinkedList<>();
        categories = new LinkedList<>();
        authors = new LinkedList<>();
        publishers = new LinkedList<>();
    }

    @JsonGetter("books")
    public LinkedList<Book> getBooks() {
        return books;
    }

    @JsonSetter("books")
    public void setBooks(LinkedList<Book> books) {
        this.books = books;
    }

    @JsonGetter("categories")
    public LinkedList<Category> getCategories() {
        return categories;
    }

    @JsonSetter("categories")
    public void setCategories(LinkedList<Category> categories) {
        this.categories = categories;
    }

    @JsonGetter("authors")
    public LinkedList<Author> getAuthors() {
        return authors;
    }

    @JsonSetter("authors")
    public void setAuthors(LinkedList<Author> authors) {
        this.authors = authors;
    }

    @JsonGetter("publishers")
    public LinkedList<Publisher> getPublishers() {
        return publishers;
    }

    @JsonSetter("publishers")
    public void setPublishers(LinkedList<Publisher> publishers) {
        this.publishers = publishers;
    }

    @JsonGetter("max")
    public int getResultMax() {
        return resultMax;
    }

    @JsonSetter("max")
    public void setResultMax(int resultMax) {
        this.resultMax = resultMax;
    }
}
