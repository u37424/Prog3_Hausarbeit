package de.medieninformatik.client.controller;

import de.medieninformatik.client.model.Model;
import de.medieninformatik.client.view.MainWindow;
import de.medieninformatik.common.Book;
import de.medieninformatik.common.Publisher;

import java.util.LinkedList;

public class Controller {
    private final Model model;
    private final MainWindow view;
    private int listStart;
    private int listLength;
    private String filterString;
    private String category;
    private String order;
    private boolean edit;
    private boolean create;
    private int scene;

    public Controller(MainWindow window, Model model) {
        //Kennt Model und View.
        //Verarbeitet Filter
        this.model = model;
        this.view = window;
        this.filterString = "";
        this.category = "ALL";
        this.listStart = 0;
        this.listLength = 10;
        this.order = "asc";
        this.scene = 0;
        this.edit = false;
        this.create = false;
    }

    public void exitProgram() {
        if (model.isMainUser())
            if (!model.logout()) return;
        System.exit(0);
    }

    public void login() {
        if (model.login()) {
            view.mainUser();
            startup();
        } else {
            view.mainUserFail();
        }
    }

    public void startup() {
        view.switchToMainScene();
        scene = 1;
        model.startup();
        loadBookList();
        view.setCategoryNames(model.getCategoryNames());
    }

    public void toLoginView() {
        if (model.isMainUser())
            if (!model.logout()) return;
        view.mainUserLogout();
        view.switchToLoginScene();
        scene = 0;
    }

    public void toMainView() {
        if (this.edit) {
            boolean leave_edit = view.confirmAction("Leave Edit", "All unsaved changes get lost!");
            if (!leave_edit) return;
            this.edit = false;
            view.setNonEditMode();
        }
        view.switchToMainScene();
        scene = 1;
    }

    public void incrementListStart(int listStart) {
        this.listStart += this.listLength;
        loadBookList();
    }

    public void decrementListStart(int listStart) {
        this.listStart -= this.listLength;
        if (this.listStart < 0) this.listStart = 0;
        loadBookList();
    }

    public void updateListLimit(int amount) {
        this.listLength = amount;
        loadBookList();
    }

    public void updateFilters(String filterString, String category) {
        if (filterString == null) filterString = "";
        if (category == null) category = "";
        filterString = filterString.replace(" ", "+");
        category = category.replace(" ", "+");
        if (this.filterString == filterString && this.category == category) return;
        this.filterString = filterString;
        this.category = category;
        loadBookList();
    }

    public void updateOrder(String order) {
        if (this.order == order) return;
        this.order = order;
        loadBookList();
    }

    private void loadBookList() {
        model.loadBookList(listStart, listLength, order, filterString, (category.equalsIgnoreCase("all")) ? "" : category);
        LinkedList<Book> books = model.getBooks();
        if (books == null || books.size() == 0) {
            view.errorMessage("Keine Bücher gefunden!");
            return;
        }
        view.setList(view.createList(books));
    }

    public void displayBook(String isbn) {
        Book book = loadBook(isbn);
        view.switchToBookScene(book);
    }

    public void deleteBook(String isbn) {

    }

    public void editBook(String isbn) {
        Book book = loadBook(isbn);
        view.switchToBookScene(book);
        view.setEditMode();
        this.edit = true;
    }

    private Book loadBook(String isbn) {
        model.loadBook(isbn);
        Book book = model.getDisplayBook();
        if (book == null) {
            view.errorMessage("No Information available.");
        }
        return book;
    }

    public void createEntry() {

    }

    public boolean isMainUser() {
        return model.isMainUser();
    }

    public void editBookValue(String label) {
        switch (label) {
            case "title":
                editTitle();
                break;
            case "year":
                editYear();
                break;
            case "pages":
                editPages();
                break;
            case "isbn":
                editIsbn();
                break;
        }
    }

    private void editIsbn() {
        String newIsbn = view.editTextMessage("Edit ISBN", model.getDisplayBook().getIsbn());
        model.getDisplayBook().setIsbn(newIsbn);
    }

    private void editPages() {
        String newPages = view.editTextMessage("Edit Pages", String.valueOf(model.getDisplayBook().getPages()));
        model.getDisplayBook().setPages(Integer.parseInt(newPages));
    }

    private void editYear() {
        String newYear = view.editTextMessage("Edit Year", String.valueOf(model.getDisplayBook().getReleaseYear()));
        model.getDisplayBook().setReleaseYear(Integer.parseInt(newYear));
    }

    private void editTitle() {
        String newTitle = view.editTextMessage("Edit Title", String.valueOf(model.getDisplayBook().getTitle()));
        model.getDisplayBook().setTitle(newTitle);
    }

    public void editBookPublisher() {
        model.loadAllPublishers();
        LinkedList<Publisher> publishers = model.getPublishers();
        Publisher newPublisher = view.editChoiceMessage("Select Publisher", model.getDisplayBook().getPublisher(), publishers);
        model.getDisplayBook().setPublisher(newPublisher);
    }
}
