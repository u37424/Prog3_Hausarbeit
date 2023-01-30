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

    private Author selection;
    private LinkedList<Author> authors;
    private int authorMax;

    public AuthorRequest(Request request) {
        this.request = request;

        ResourceBundle bundle = ResourceBundle.getBundle("ServerResources");
        this.authorPath = "/"+bundle.getString("Author.Path");
    }

    //----------POJOS FOR MODEL

    public Author getSelection() {
        return selection;
    }

    public LinkedList<Author> getAuthors() {
        return authors;
    }

    public int getMax() {
        return authorMax;
    }

    //-----------REQUESTS TO SERVER

    public void getAuthor(int id) {
        Response response = request.serverRequest("GET", "/" + authorPath + "/" + id);
        this.selection =  request.createObject(response, Author.class);
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

    public boolean editAuthor() {
        Response response = request.serverRequest("PUT", authorPath, selection);
        return request.isOk(response);
    }

    public boolean createAuthor() {
        Response response = request.serverRequest("POST", authorPath, selection);
        return request.isOk(response);
    }

    public boolean deleteAuthor(int id) {
        Response response = request.serverRequest("DELETE",  authorPath + "/" + id);
        return request.isOk(response);
    }

    public void reset() {
        this.selection = new Author();
    }
}
