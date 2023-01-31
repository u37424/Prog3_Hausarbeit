package de.medieninformatik.common;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;

import java.util.LinkedList;
/**
 * @author Luca Spirka m29987
 * @version 1.0
 * <p>
 * Programmieren 3 - Hausarbeit.
 * <p>
 * 2023-01-31
 * <p>
 * Die Klasse dient als Schnittstelle fuer alle Listen von Objekten die uebertragen werden sollen.
 * Listen von Objekten, die aus der Datenbank gelesen werden, koennen hiermit zusammengefasst werden.
 * Ebenso koennen Werte wie die Maximalgroesse an Listen uebertagen werden.
 * Die Klasse bietet ausserdem die Schnittstelle fuer die Umwandlung von Listen an Objekten in JSON und zurueck.
 */
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

    /**
     * Gibt eine Liste von Buechern zurueck
     * @return Liste von Buechern
     */
    @JsonGetter("books")
    public LinkedList<Book> getBooks() {
        return books;
    }

    /**
     * Setzt eine Liste von buechern
     * @param books  Liste von buechern
     */
    @JsonSetter("books")
    public void setBooks(LinkedList<Book> books) {
        this.books = books;
    }

    /**
     * Gibt eine Liste von Buechern zurueck
     * @return Liste von Buechern
     */
    @JsonGetter("categories")
    public LinkedList<Category> getCategories() {
        return categories;
    }

    /**
     * Setzt eine Liste von kategorien
     * @param categories Liste von kategorien
     */
    @JsonSetter("categories")
    public void setCategories(LinkedList<Category> categories) {
        this.categories = categories;
    }

    /**
     * Gibt eine Liste von Buechern zurueck
     * @return Liste von Buechern
     */
    @JsonGetter("authors")
    public LinkedList<Author> getAuthors() {
        return authors;
    }

    /**
     * Setzt eine Liste von Autoren
     * @param authors Liste von Autoren
     */
    @JsonSetter("authors")
    public void setAuthors(LinkedList<Author> authors) {
        this.authors = authors;
    }

    /**
     * Gibt eine Liste von Buechern zurueck
     * @return Liste von Buechern
     */
    @JsonGetter("publishers")
    public LinkedList<Publisher> getPublishers() {
        return publishers;
    }

    /**
     * Setzt eine Liste von publishern
     * @param publishers Liste von publishern
     */
    @JsonSetter("publishers")
    public void setPublishers(LinkedList<Publisher> publishers) {
        this.publishers = publishers;
    }

    /**
     * Gibt eine Maximalzahl einer angefragten Partiellen Liste aus.
     * @return Maximalzahl der Liste
     */
    @JsonGetter("max")
    public int getResultMax() {
        return resultMax;
    }

    /**
     * Setzt die Maximalanzahl einer Liste
     * @param resultMax Maximalanzahl zu einer partiellen Liste.
     */
    @JsonSetter("max")
    public void setResultMax(int resultMax) {
        this.resultMax = resultMax;
    }
}
