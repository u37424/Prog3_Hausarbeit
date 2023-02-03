package de.medieninformatik.client.model;

import de.medieninformatik.common.Author;
import de.medieninformatik.common.DBMeta;
import jakarta.ws.rs.core.Response;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.ResourceBundle;

public class AuthorRequest {
    private final Request request;
    private final String authorPath;

    private Author item;
    private LinkedList<Author> authors;
    private int authorMax;

    public AuthorRequest(Request request) {
        this.request = request;

        ResourceBundle bundle = ResourceBundle.getBundle("ServerResources");
        this.authorPath = "/"+bundle.getString("Author.Path");
    }

    //----------POJOS FOR MODEL

    public Author getItem() {
        return item;
    }

    public LinkedList<Author> getItemList() {
        return authors;
    }

    public int getMax() {
        return authorMax;
    }

    //-----------REQUESTS TO SERVER

    public void loadItem(String id) {
        Response response = request.serverRequest("GET",  authorPath + "/" + id);
        this.item =  request.createObject(response, Author.class);
    }

    public void loadAll() {
        Response response = request.serverRequest("GET", authorPath);
        this.authors = request.createObject(response, DBMeta.class).getAuthors();
    }

    public void loadSelection(int start, int limit, boolean orderAsc, String string) {
        string = URLEncoder.encode(string, StandardCharsets.UTF_8);
        String query = authorPath + "/" + start + "/" + limit + "/" + orderAsc + "?string=" + string;
        Response response = request.serverRequest("GET", query);
        DBMeta result = request.createObject(response, DBMeta.class);
        this.authors = result.getAuthors();
        this.authorMax = result.getResultMax();
    }

    public boolean editItem() {
        Response response = request.serverRequest("PUT", authorPath, item);
        return request.isOk(response);
    }

    public boolean createItem() {
        Response response = request.serverRequest("POST", authorPath, item);
        return request.isCreated(response);
    }

    public boolean deleteItem(String id) {
        Response response = request.serverRequest("DELETE",  authorPath + "/" + id);
        return request.isOk(response);
    }

    public void reset() {
        this.item = new Author();
    }
}
