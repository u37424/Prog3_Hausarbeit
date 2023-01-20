package de.medieninformatik.client.model;

import de.medieninformatik.common.Book;
import de.medieninformatik.common.Category;
import de.medieninformatik.common.Publisher;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class Model {
    //Alle POJOS / Daten die vom Server abgefragt werden hier abgefragt und werden im View dargestellt
    //Wie lange muessen Daten gespeichert werden? Es sind nie alle Daten auf einmal vonnoeten
    //Aber Daten muessen nicht gleich verworfen werden
    //Buch abfragen -> Temporaer bis Fenster schliessen / anderes Buch aufrufen
    //Listen erhalten, auch wenn Buch abgefragt
    //Objekte die variable Listen enthalten
    //Ein Display-Buch
    //Wann erhaelt man Arbeits-Objekte (Auswahl von Kategorien und Autoren im Erstellen / Bearbeiten || Kategorieauswahl fuer Kategorie Filter <-wann laden?)
    //Was, wenn erfragte Objekte aus Listen bei spezieller Anfrage schon geloescht sind?
    //Was, wenn Objekte falsch erstellt werden?
    //Wie koennen Modifizierungen oder erstellte Buecher / Autoren / Kategorien / Publisher an den Server uebermittelt werden?

    //Hier Anfragen von Controller mithilfe von Request erhalten und bearbeiten
    private boolean isMainUser = false;
    Request request = Request.getInstance();
    private Book displayBook;
    private Book[] books;
    private Category[] categories;
    private Publisher[] publishers;

    public void startup() {
        displayBook = new Book("");
        loadAllCategories();
    }

    public void loadBook(String isbn) {
        this.displayBook = request.getBook(isbn);
    }

    public void loadBookList(int start, int amount, String order, String match, String category) {
        this.books = request.getBookList(start, amount, order, match, category);
    }

    public void loadAllCategories() {
        Category[] categories = request.getCategoryList();
        this.categories = categories;
    }

    public void loadAllPublishers() {
        Publisher[] publishers = request.getPublisherList();
        this.publishers = publishers;
    }

    public boolean login() {
        if (isMainUser) return false;
        else {
            return isMainUser = request.login();
        }
    }

    public boolean logout() {
        if (!isMainUser) return false;
        else {
            return !(isMainUser = !request.logout());
        }
    }

    public boolean isMainUser() {
        return isMainUser;
    }

    public Book getDisplayBook() {
        return displayBook;
    }

    public LinkedList<Book> getBooks() {
        if(books == null) return new LinkedList<>();
        return new LinkedList<>(List.of(books));
    }

    public LinkedList<String> getCategoryNames() {
        LinkedList<String> c = new LinkedList<>();
        c.add("All");
        if (categories == null) return c;
        for (int i = 0; i < categories.length; i++) {
            c.add(categories[i].getName());
        }
        return c;
    }

    public LinkedList<Publisher> getPublishers() {
        LinkedList<Publisher> p = new LinkedList<>();
        if (publishers == null) return p;
        Collections.addAll(p, publishers);
        return p;
    }
}
