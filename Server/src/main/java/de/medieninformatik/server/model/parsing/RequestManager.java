package de.medieninformatik.server.model.parsing;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.medieninformatik.common.*;

import java.util.LinkedList;

public class RequestManager {
    private static final QueryManager query = new QueryManager();
    private static final UpdateManager update = new UpdateManager();

    //-------GET METHODS

    //-------GET MAX

    public String getBookMax() {
        return asMax(query.getBookMax());
    }

    public String getCategoryMax() {
        return asMax(query.getCategoryMax());
    }

    public String getAuthorMax() {
        return asMax(query.getAuthorMax());
    }

    public String getPublisherMax() {
        return asMax(query.getPublisherMax());
    }

    // --------GET LISTS

    public String getAllBooks() {
        return getAllBooks(0, 0, true, null, null);
    }

    public String getAllBooks(int start, int size, boolean orderAsc, String string, String category) {
        LinkedList<Book> books = query.getAllBooks(start, size, orderAsc, string, category);
        DBMeta meta = new DBMeta();
        meta.setBooks(books);
        return asJSON(meta);
    }

    public String getAllCategories() {
        return getAllCategories(0, 0, true, null);
    }

    public String getAllCategories(int start, int size, boolean orderAsc, String string) {
        LinkedList<Category> categories = query.getAllCategories(start, size, orderAsc, string);
        DBMeta meta = new DBMeta();
        meta.setCategories(categories);
        return asJSON(meta);
    }

    public String getAllPublishers() {
        return getAllPublishers(0, 0, true, null);
    }

    public String getAllPublishers(int start, int size, boolean orderAsc, String string) {
        LinkedList<Publisher> publishers = query.getAllPublishers(start, size, orderAsc, string);
        DBMeta meta = new DBMeta();
        meta.setPublishers(publishers);
        return asJSON(meta);
    }

    public String getAllAuthors() {
        return getAllAuthors(0, 0, true, null);
    }

    public String getAllAuthors(int start, int size, boolean orderAsc, String string) {
        LinkedList<Author> authors = query.getAllAuthors(start, size, orderAsc, string);
        DBMeta meta = new DBMeta();
        meta.setAuthors(authors);
        return asJSON(meta);
    }

    //-------GET INSTANCE

    public String getBook(String isbn) {
        return asJSON(query.getBook(isbn));
    }

    public String getCategory(int id) {
        return asJSON(query.getCategory(id));
    }


    public String getPublisher(int id) {
        return asJSON(query.getPublisher(id));
    }


    public String getAuthor(int id) {
        return asJSON(query.getAuthor(id));
    }


    //------CONVERT GET METHODS

    private String asMax(int max) {
        DBMeta meta = new DBMeta();
        meta.setResultMax(max);
        return asJSON(meta);
    }

    private <T> String asJSON(T object) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(object);
        } catch (RuntimeException | JsonProcessingException e) {
            System.err.println("Could not convert from " + object.getClass() + " to JSON.");
        }
        return null;
    }

    //----------PUT METHODS

    public boolean putBook(Book book) {
        return update.putBook(book);
    }

    public boolean putCategory(Category category) {
        return update.putCategory(category);
    }

    public boolean putPublisher(Publisher publisher) {
        return update.putPublisher(publisher);
    }

    public boolean putAuthor(Author author) {
        return update.putAuthor(author);
    }

    //----------POST METHODS

    public boolean postAuthor(Author author) {
        return update.postAuthor(author);
    }

    public boolean postBook(Book book) {
        return update.postBook(book);
    }

    public boolean postCategory(Category category) {
        return update.postCategory(category);
    }

    public boolean postPublisher(Publisher publisher) {
        return update.postPublisher(publisher);
    }

    //---------DELETE METHODS

    public boolean deleteAuthor(int id) {
        return update.deleteAuthor(id);
    }

    public boolean deleteBook(String isbn) {
        return update.deleteBook(isbn);
    }

    public boolean deleteCategory(int id) {
        return update.deleteCategory(id);
    }

    public boolean deletePublisher(int id) {
        return update.deletePublisher(id);
    }
}
