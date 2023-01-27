package de.medieninformatik.server.model.parsing;

import de.medieninformatik.common.Author;
import de.medieninformatik.common.Book;
import de.medieninformatik.common.Category;
import de.medieninformatik.common.Publisher;
import de.medieninformatik.server.model.database.Database;

import java.util.LinkedList;

public class QueryManager {
    private final Database database =  Database.getInstance();

    public QueryManager() {

    }

    //------Get Max Data

    public int getBookMax() {
        return 0;
    }

    public int getCategoryMax() {
        return 0;
    }

    public int getAuthorMax() {
        return 0;
    }

    public int getPublisherMax() {
        return 0;
    }

    //----Convert Data to LinkedLists

    public LinkedList<Category> getAllCategories(int start, int size, boolean orderAsc, String string) {
        return new LinkedList<>();
    }

    public LinkedList<Book> getAllBooks(int start, int size, boolean orderAsc, String string, String category) {
        return null;
    }

    public LinkedList<Publisher> getAllPublishers(int start, int size, boolean orderAsc, String string) {
        return null;
    }

    public LinkedList<Author> getAllAuthors(int start, int size, boolean orderAsc, String string) {
        return null;
    }

    //-------Get Instance Data

    public Book getBook(String isbn) {
        return null;
    }

    public Category getCategory(int isbn) {
        return null;
    }

    public Author getAuthor(int isbn) {
        return null;
    }

    public Publisher getPublisher(int isbn) {
        return null;
    }
}
