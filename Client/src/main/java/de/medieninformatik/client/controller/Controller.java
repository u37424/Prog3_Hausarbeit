package de.medieninformatik.client.controller;

import de.medieninformatik.client.model.Model;
import de.medieninformatik.client.view.MainWindow;
import de.medieninformatik.common.Author;
import de.medieninformatik.common.Book;
import de.medieninformatik.common.Category;
import de.medieninformatik.common.Publisher;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Objects;

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
        view.setDefaultListLength(10);
        view.switchToMainScene();
        scene = 1;
        model.startup();
        showBookList();
        LinkedList<Category> categories = model.getCategories();
        LinkedList<String> categoryNames = new LinkedList<>();
        categories.forEach(c -> categoryNames.add(c.getName()));
        view.setCategoryNames(categoryNames);
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
            this.create = false;
            view.setNonEditMode();
        }
        view.switchToMainScene();
        scene = 1;
    }

    public void forwardSide() {
        this.listStart += this.listLength;
        showBookList();
    }

    public void backSide() {
        this.listStart -= this.listLength;
        if (this.listStart < 0) this.listStart = 0;
        showBookList();
    }

    public void updateListSize(int amount) {
        this.listLength = amount;
        showBookList();
    }

    public void updateFilters(String filterString, String category) {
        if (filterString == null) filterString = "";
        if (category == null) category = "";
        if (this.filterString.equals(filterString) && this.category.equals(category)) return;
        this.filterString = filterString;
        this.category = category;
        showBookList();
    }

    public void updateOrder(String order) {
        if (this.order.equals(order)) return;
        this.order = order;
        showBookList();
    }

    private void showBookList() {
        model.loadBookList(listStart, listLength, order, filterString, (category.equalsIgnoreCase("all")) ? "" : category);
        LinkedList<Book> books = model.getBooks();
        if (books == null || books.size() == 0) {
            view.errorMessage("Keine Bücher gefunden!");
        }
        view.setList(view.createList(books));
    }

    public void displayBook(String isbn) {
        Book book = loadBook(isbn);
        view.switchToBookScene(book);
    }

    public void deleteBook(String isbn) {
        if(!model.deleteBook(isbn)) view.errorMessage("Couldn't delete Book from Database!");
        else showBookList();
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
        model.setTemplate();
        view.switchToBookScene(model.getDisplayBook());
        view.setEditMode();
        edit = true;
        create = true;
    }

    public boolean isMainUser() {
        return model.isMainUser();
    }

    public void editBookValue(String label) {
        switch (label) {
            case "title" -> editTitle();
            case "year" -> editYear();
            case "pages" -> editPages();
            case "isbn" -> editIsbn();
        }
        view.updateBook(model.getDisplayBook());
    }

    private void editIsbn() {
        String newIsbn = view.editTextMessage("Edit ISBN", model.getDisplayBook().getIsbn());
        model.getDisplayBook().setIsbn(newIsbn);
    }

    private void editPages() {
        String newPages = view.editTextMessage("Edit Pages", String.valueOf(model.getDisplayBook().getPages()));
        try {
            model.getDisplayBook().setPages(Integer.parseInt(newPages));
        } catch (NumberFormatException e) {
            view.errorMessage("Not a Number!");
        }
    }

    private void editYear() {
        String newYear = view.editTextMessage("Edit Year", String.valueOf(model.getDisplayBook().getReleaseYear()));
        try {
            model.getDisplayBook().setReleaseYear(Integer.parseInt(newYear));
        } catch (NumberFormatException e) {
            view.errorMessage("Not a Number!");
        }
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
        view.updateBook(model.getDisplayBook());
    }

    public void editBookDescription(String description) {
        model.getDisplayBook().setDescription(description);
    }

    public void addCategory() {
        model.loadAllCategories();
        Category c = view.editChoiceMessage("Add Category", null, model.getSelectCategories());
        if (c == null) return;
        if (hasCategory(c)) view.infoMessage("Category already exists for this Element!");
        else {
            int index = model.getDisplayBook().getCategories().length;
            Category[] current = model.getDisplayBook().getCategories();
            model.getDisplayBook().setCategories(Arrays.copyOf(current, index + 1));
            model.getDisplayBook().getCategories()[index] = c;
            view.updateBook(model.getDisplayBook());
        }
    }

    private boolean hasCategory(Category category) {
        if (model.getDisplayBook().getCategories().length == 0) return false;
        for (Category c : model.getDisplayBook().getCategories()) {
            if (c.getCategoryId() == category.getCategoryId()) return true;
        }
        return false;
    }

    public void removeCategory(int index) {
        if (model.getDisplayBook().getCategories().length == 1) {
            view.errorMessage("You need at least one Category!");
            return;
        }
        model.getDisplayBook().getCategories()[index] = null;
        Category[] noNull = Arrays.stream(model.getDisplayBook().getCategories())
                .filter(Objects::nonNull)
                .toArray(Category[]::new);
        model.getDisplayBook().setCategories(noNull);
        view.updateBook(model.getDisplayBook());
    }

    public void addAuthor() {
        model.loadAllAuthors();
        Author a = view.editChoiceMessage("Add Author", null, model.getAuthorNames());
        if (a == null) return;
        if (hasAuthor(a))
            view.infoMessage("Author already exists for this Element!");
        else {
            int index = model.getDisplayBook().getAuthors().length;
            Author[] current = model.getDisplayBook().getAuthors();
            model.getDisplayBook().setAuthors(Arrays.copyOf(current, index + 1));
            model.getDisplayBook().getAuthors()[index] = a;
            view.updateBook(model.getDisplayBook());
        }
    }

    private boolean hasAuthor(Author author) {
        if (model.getDisplayBook().getAuthors().length == 0) return false;
        for (Author a : model.getDisplayBook().getAuthors()) {
            if (a.getAuthorId() == author.getAuthorId()) return true;
        }
        return false;
    }

    public void removeAuthor(int index) {
        if (model.getDisplayBook().getAuthors().length == 1) {
            view.errorMessage("You need at least one Author!");
            return;
        }
        model.getDisplayBook().getAuthors()[index] = null;
        Author[] noNull = Arrays.stream(model.getDisplayBook().getAuthors())
                .filter(Objects::nonNull)
                .toArray(Author[]::new);
        model.getDisplayBook().setAuthors(noNull);
        view.updateBook(model.getDisplayBook());
    }

    public void updateEntry() {
        Book book = model.getDisplayBook();
        if (book.getTitle().isBlank()) view.infoMessage("Yor Entry needs to have a Title!");
        if (book.getPublisher() == Publisher.NONE) {
            view.infoMessage("You need to have a Publisher!");
            return;
        }
        if (book.getCategories().length == 0) {
            view.infoMessage("You need to have at least one Category!");
            return;
        }
        if (book.getAuthors().length == 0) {
            view.infoMessage("You need to have at least one Author!");
            return;
        }
        if(create) createSubmit();
        else if(edit) editSubmit();
    }

    private void editSubmit() {
        if (model.updateEntry()) {
            view.infoMessage("Entry successfully updated!");
            this.edit = false;
            this.create = false;
            view.setNonEditMode();
            showBookList();
            toMainView();
        } else view.errorMessage("Couldn't update Entry!");
    }

    private void createSubmit() {
        if (model.createEntry()) {
            view.infoMessage("Entry successfully created!");
            this.edit = false;
            this.create = false;
            view.setNonEditMode();
            showBookList();
            toMainView();
        } else view.errorMessage("Couldn't create Entry!");
    }
}
