package de.medieninformatik.client.model;

import de.medieninformatik.common.Author;
import de.medieninformatik.common.Book;
import de.medieninformatik.common.Category;
import de.medieninformatik.common.Publisher;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
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
    private Author[] authors;

    public void startup() {
        displayBook = new Book("");
        loadAllCategories();
    }

    public void loadBook(String isbn) {
        this.displayBook = request.getBook(isbn);
    }

    public void loadBookList(int start, int amount, String order, String match, String category) {
        String queryMatch = URLEncoder.encode(match, StandardCharsets.UTF_8);
        String queryCategory = URLEncoder.encode(category, StandardCharsets.UTF_8);
        this.books = request.getBookList(start, amount, order, queryMatch, queryCategory);
    }

    public void loadAllCategories() {
        this.categories = request.getCategoryList();
    }

    public void loadAllPublishers() {
        this.publishers = request.getPublisherList();
    }

    public void loadAllAuthors() {
        this.authors = request.getAuthorList();
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
        if (books == null) return new LinkedList<>();
        return new LinkedList<>(List.of(books));
    }

    public LinkedList<Category> getCategories() {
        LinkedList<Category> c = new LinkedList<>();
        c.add(Category.ALL);
        if (categories == null) return c;
        c.addAll(Arrays.asList(categories));
        return c;
    }


    public LinkedList<Category> getSelectCategories() {
        LinkedList<Category> c = new LinkedList<>();
        if (categories == null) return c;
        c.addAll(Arrays.asList(categories));
        return c;
    }

    public LinkedList<Publisher> getPublishers() {
        LinkedList<Publisher> p = new LinkedList<>();
        if (publishers == null) return p;
        Collections.addAll(p, publishers);
        return p;
    }

    public void setTemplate() {
        this.displayBook = Book.NONE;
    }

    public LinkedList<Author> getAuthorNames() {
        LinkedList<Author> a = new LinkedList<>();
        if (authors == null) return a;
        Collections.addAll(a, authors);
        return a;
    }

    public boolean updateEntry() {
        return request.putEntry(this.displayBook);
    }

    public boolean createEntry() {
        return request.postEntry(this.displayBook);
    }

    public boolean deleteBook(String isbn) {
        return request.deleteEntry(isbn);
    }
}
