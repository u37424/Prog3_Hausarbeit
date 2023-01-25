package de.medieninformatik.client.model;

import de.medieninformatik.common.*;

import java.util.LinkedList;

public class MainModel {

    private boolean mainUser;
    private boolean editMode;
    private boolean crateMode;

    private Book selection;
    private LinkedList<Book> books;
    private LinkedList<Category> categories;
    private LinkedList<Author> authors;
    private LinkedList<Publisher> publishers;
    private int bookListMax;

    public boolean login() {
        //if server succeeded
        mainUser = true;
        return true;
    }

    public boolean logout() {
        //if server succeeded
        mainUser = false;
        return true;
    }

    public void loadBookList(String userString, String userCategory, int pageStart, int pageSize, boolean ascending) {
        //DBMeta POJO erhalten
        //Liste von Buechern (im DBMeta POJO) nach angegebenen Parametern laden
        this.books = new DBMeta().getBooks();
        this.bookListMax = new DBMeta().getResultMax();
    }

    public void setMainUser(boolean isMainUser) {
        this.mainUser = isMainUser; //Set Admin Status
    }

    public void setEditMode(boolean editMode) {
        this.editMode = editMode;   //Set Edit Status
    }

    public void setCrateMode(boolean crateMode) {
        this.crateMode = crateMode; //Set Create Status
    }

    public LinkedList<Book> getBooks() {
        return books;
    }

    public LinkedList<Category> getCategories() {
        return categories;
    }

    public LinkedList<Author> getAuthors() {
        return authors;
    }

    public LinkedList<Publisher> getPublishers() {
        return publishers;
    }

    public boolean isMainUser() {
        return mainUser;
    }

    public boolean isEditMode() {
        return editMode;
    }

    public boolean isCrateMode() {
        return crateMode;
    }

    public int getResultMax() {
        return bookListMax;
    }

}
