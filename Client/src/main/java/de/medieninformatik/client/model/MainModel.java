package de.medieninformatik.client.model;

import de.medieninformatik.common.Author;
import de.medieninformatik.common.Book;
import de.medieninformatik.common.Category;
import de.medieninformatik.common.Publisher;

import java.util.LinkedList;

public class MainModel {

    private final RequestManager requestManager;

    private boolean mainUser;
    private boolean editMode;
    private boolean crateMode;

    private String userString;
    private String userCategory;
    private boolean ascending;
    private int pageStart;
    private int pageSize;

    private Book selection;
    private LinkedList<Book> books;
    private LinkedList<Category> categories;
    private LinkedList<Author> authors;
    private LinkedList<Publisher> publishers;
    private int bookListMax;

    public MainModel() {
        this.requestManager = new RequestManager();

        this.userString = "";
        this.userCategory = "";
        this.ascending = true;
        this.pageStart = 0;
        this.pageSize = 5;
    }

    public boolean login() {
        return mainUser = requestManager.login();
    }

    public boolean logout() {
        return !(mainUser = !requestManager.logout());
    }

    public void loadBookList() {
        this.books = requestManager.loadBooks(this.pageStart, this.pageSize, this.ascending, this.userString, this.userCategory);
        this.bookListMax = requestManager.getBookListMax();
    }

    public void loadCategoryList() {
        //Categories from Server
        this.categories = requestManager.loadCategories();
    }

    public void loadPublisherList() {
        //Categories from Server
        this.publishers = requestManager.loadPublishers();
    }

    public void loadAuthorList() {
        //Categories from Server
        this.authors = requestManager.loadAuthors();
    }

    public void loadBook(String isbn) {
        this.selection = requestManager.getBook(isbn);
    }

    public boolean editBook() {
        return requestManager.editBook(this.selection);
    }

    public boolean createBook() {
        return requestManager.createBook(this.selection);
    }

    public boolean deleteBook(String isbn) {
        return requestManager.deleteBook(isbn);
    }

    public void resetSelection() {
        this.selection = new Book("");
    }

    public boolean resetDatabase() {
        return requestManager.resetDatabase();
    }

    public void pageBackward() {
        if (pageStart - pageSize >= 0) this.pageStart -= pageSize;
    }

    public void pageForward() {
        if (pageStart + pageSize <= bookListMax) this.pageStart += pageSize;
    }

    public boolean updateFilters(String string, String category) {
        if (string == null) string = "";
        if (category == null) category = "";

        if (userString.equals(string) && userCategory.equals(category)) return false;
        this.userString = string;
        this.userCategory = category;
        return true;
    }

    public boolean resetFilters() {
        if (userString.isBlank() && userCategory.equals("")) return false;
        this.userString = "";
        this.userCategory = "";
        return true;
    }

    //GETTER, SETTER

    //--------------Results from Server / Daten des Model

    public Book getSelection() {
        return selection;
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

    //-------------User Settings im Model, um Anmeldung ueber mehrere Views / Controller zu behalten

    public boolean isMainUser() {
        return mainUser;
    }

    public boolean isEditMode() {
        return editMode;
    }

    public void setEditMode(boolean editMode) {
        this.editMode = editMode;   //Set Edit Status
    }

    public boolean isCrateMode() {
        return crateMode;
    }

    public void setCrateMode(boolean crateMode) {
        this.editMode = crateMode;
        this.crateMode = crateMode; //Set Create Status
    }

    //------------Filter im Model, um konsistent ueber mehrere Views/ Controller zu bleiben

    public boolean isAscending() {
        return ascending;
    }

    public void setAscending(boolean ascending) {
        this.ascending = ascending;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public String getUserString() {
        return userString;
    }

    public String getUserCategory() {
        return userCategory;
    }
}
