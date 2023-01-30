package de.medieninformatik.common;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;

import java.util.LinkedList;

public class Book {
    private String isbn;
    private String title;
    private Publisher publisher;
    private int releaseYear;
    private int pages;
    private double rating;
    private String description;
    private LinkedList<Category> categories;
    private LinkedList<Author> authors;

    public Book(){
        categories = new LinkedList<>();
        authors = new LinkedList<>();
    }

    @JsonGetter("isbn")
    public String getIsbn() {
        return isbn;
    }

    @JsonSetter("isbn")
    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    @JsonGetter("title")
    public String getTitle() {
        return title;
    }

    @JsonSetter("title")
    public void setTitle(String title) {
        this.title = title;
    }

    @JsonGetter("publisher")
    public Publisher getPublisher() {
        return publisher;
    }

    @JsonSetter("publisher")
    public void setPublisher(Publisher publisher) {
        this.publisher = publisher;
    }

    @JsonGetter("year")
    public int getReleaseYear() {
        return releaseYear;
    }

    @JsonSetter("year")
    public void setReleaseYear(int releaseYear) {
        this.releaseYear = releaseYear;
    }

    @JsonGetter("pages")
    public int getPages() {
        return pages;
    }

    @JsonSetter("pages")
    public void setPages(int pages) {
        this.pages = pages;
    }

    @JsonGetter("rating")
    public double getRating() {
        return rating;
    }

    @JsonSetter("rating")
    public void setRating(double rating) {
        this.rating = rating;
    }

    @JsonGetter("description")
    public String getDescription() {
        return description;
    }

    @JsonSetter("description")
    public void setDescription(String description) {
        this.description = description;
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

    @Override
    public String toString() {
        return isbn+" "+title+" "+" ("+releaseYear+")";
    }
}
