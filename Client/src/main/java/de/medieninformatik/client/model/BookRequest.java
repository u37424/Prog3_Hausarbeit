package de.medieninformatik.client.model;

import de.medieninformatik.common.Book;
import de.medieninformatik.common.DBMeta;
import jakarta.ws.rs.core.Response;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.ResourceBundle;

public class BookRequest {
    private final Request request;
    private final String bookPath;

    private Book item;
    private LinkedList<Book> books;
    private int bookMax;

    public BookRequest(Request request) {
        this.request = request;

        ResourceBundle bundle = ResourceBundle.getBundle("ServerResources");
        this.bookPath = "/" + bundle.getString("Book.Path");
    }

    //--------POJOS FOR MODEL

    public Book getItem() {
        return item;
    }

    public LinkedList<Book> getItemList() {
        return books;
    }

    public int getMax() {
        return bookMax;
    }

    //------------SERVER REQUESTS

    public void loadItem(String id) {
        Response response = request.serverRequest("GET",  bookPath + "/" + id);
        this.item = request.createObject(response, Book.class);
    }

    public void loadAll() {
        Response response = request.serverRequest("GET", bookPath);
        this.books = request.createObject(response, DBMeta.class).getBooks();
    }

    public void loadSelection(int start, int limit, boolean orderAsc, String string, String category) {
        string = URLEncoder.encode(string, StandardCharsets.UTF_8);
        category = URLEncoder.encode(category, StandardCharsets.UTF_8);
        String query = bookPath + "/" + start + "/" + limit + "/" + orderAsc + "?string=" + string + "&category=" + category;
        Response response = request.serverRequest("GET", query);
        DBMeta result = request.createObject(response, DBMeta.class);
        this.books = result.getBooks();
        this.bookMax = result.getResultMax();
    }

    public boolean editItem() {
        Response response = request.serverRequest("PUT", bookPath, item);
        return request.isOk(response);
    }

    public boolean createItem() {
        Response response = request.serverRequest("POST", bookPath, item);
        return request.isCreated(response);
    }

    public boolean deleteItem(String id) {
        Response response = request.serverRequest("DELETE",  bookPath + "/" + id);
        return request.isOk(response);
    }

    public void reset() {
        this.item = new Book();
    }
}
