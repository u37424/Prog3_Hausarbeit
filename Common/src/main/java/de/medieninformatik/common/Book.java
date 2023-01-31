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
 * Die Klasse dient als Schnittstelle fuer alle Daten die zu Buechern uebertragen werden sollen.
 * Buecher, die aus der Datenbank gelesen werden, koennen hiermit auf konkrete Objekte umgewandelt werden.
 * Die Klasse bietet ausserdem die Schnittstelle fuer die Umwandlung von konkreten Buechern in JSON und zurueck.
 */
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

    /**
     * Gibt die ISBN zurueck
     * @return ISBN des Buches
     */
    @JsonGetter("isbn")
    public String getIsbn() {
        return isbn;
    }

    /**
     * Setzt die ISBN des Buches
     * @param isbn ISBN des Buches
     */
    @JsonSetter("isbn")
    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }


    /**
     * Gibt den Titel zurueck
     * @return Titel des Buches
     */
    @JsonGetter("title")
    public String getTitle() {
        return title;
    }

    /**
     * Setzt den Titel des Buches
     * @param title Titel des Buches
     */
    @JsonSetter("title")
    public void setTitle(String title) {
        this.title = title;
    }


    /**
     * Gibt den Publisher zurueck
     * @return Publisher des Buches
     */
    @JsonGetter("publisher")
    public Publisher getPublisher() {
        return publisher;
    }

    /**
     * Setzt den Publisher des Buches
     * @param publisher Publisher des Buches
     */
    @JsonSetter("publisher")
    public void setPublisher(Publisher publisher) {
        this.publisher = publisher;
    }


    /**
     * Gibt das Jahr zurueck
     * @return Erscheinungsjahr des Buches
     */
    @JsonGetter("year")
    public int getReleaseYear() {
        return releaseYear;
    }

    /**
     * Setzt das Jahr des Buches
     * @param releaseYear Jahr des Buches
     */
    @JsonSetter("year")
    public void setReleaseYear(int releaseYear) {
        this.releaseYear = releaseYear;
    }


    /**
     * Gibt die Seitenanzahl zurueck
     * @return Seitenanzahl des Buches
     */
    @JsonGetter("pages")
    public int getPages() {
        return pages;
    }

    /**
     * Setzt die Seitenanzahl des Buches
     * @param pages Seiten des Buches
     */
    @JsonSetter("pages")
    public void setPages(int pages) {
        this.pages = pages;
    }


    /**
     * Gibt die Bewertung zurueck
     * @return Bewertung des Buches
     */
    @JsonGetter("rating")
    public double getRating() {
        return rating;
    }

    /**
     * Setzt die Bewertung des Buches
     * @param rating Bewertung des Buches
     */
    @JsonSetter("rating")
    public void setRating(double rating) {
        this.rating = rating;
    }


    /**
     * Gibt die Beschreibung zurueck
     * @return Beschreibung des Buches
     */
    @JsonGetter("description")
    public String getDescription() {
        return description;
    }

    /**
     * Setzt die Beschreibung des Buches
     * @param description beschreibung des Buches
     */
    @JsonSetter("description")
    public void setDescription(String description) {
        this.description = description;
    }


    /**
     * Gibt die Kategorien zurueck
     * @return Kategorien des Buches
     */
    @JsonGetter("categories")
    public LinkedList<Category> getCategories() {
        return categories;
    }

    /**
     * Setzt die Kategorien eines Buches
     * @param categories kategorien des Buches
     */
    @JsonSetter("categories")
    public void setCategories(LinkedList<Category> categories) {
        this.categories = categories;
    }


    /**
     * Gibt die Autoren zurueck
     * @return Autoren des Buches
     */
    @JsonGetter("authors")
    public LinkedList<Author> getAuthors() {
        return authors;
    }

    /**
     * Setzt die Autoren des Buches
     * @param authors Autoren des Buches
     */
    @JsonSetter("authors")
    public void setAuthors(LinkedList<Author> authors) {
        this.authors = authors;
    }


    /**
     * Wandelt das Buch in eine ausgewaehlte String Darstellung um.
     * @return String Darstellung des Buches.
     */
    @Override
    public String toString() {
        return isbn+" "+title+" "+" ("+releaseYear+")";
    }
}
