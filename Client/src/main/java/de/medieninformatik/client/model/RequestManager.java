package de.medieninformatik.client.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.medieninformatik.common.*;
import jakarta.ws.rs.HttpMethod;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.Optional;
import java.util.ResourceBundle;

public class RequestManager {
    //Path Attributes
    private final String bookPath;
    private final String maxPath;
    private final String categoryPath;
    private final String publisherPath;
    private final String authorPath;
    private final String userPath;
    private final String loginPath;
    private final String logoutPath;
    private final String resetPath;

    private final String baseURI;
    private final Client client;

    public RequestManager() {
        ResourceBundle bundle = ResourceBundle.getBundle("ServerResources");
        //Set BASE URI
        String hostAddress = bundle.getString("Host.Address");
        String hostPort = bundle.getString("Host.Port");
        String basePath = bundle.getString("Base.Path");
        this.baseURI = "http://" + hostAddress + ":" + hostPort + "/" + basePath;

        //Set Path Attributes
        this.bookPath = bundle.getString("Book.Path");
        this.maxPath = bundle.getString("Max.Path");
        this.categoryPath = bundle.getString("Category.Path");
        this.publisherPath = bundle.getString("Publisher.Path");
        this.authorPath = bundle.getString("Author.Path");
        this.userPath = bundle.getString("User.Path");
        this.loginPath = bundle.getString("User.Login.Path");
        this.logoutPath = bundle.getString("User.Logout.Path");
        this.resetPath = bundle.getString("User.Reset.Path");

        this.client = ClientBuilder.newClient();
    }

    //--------------Data Request Methods (Request -> JSON -> POJO -> Data)

    public boolean login() {
        Response response = serverRequest(HttpMethod.GET, "/" + userPath + "/" + loginPath);
        return status(response) == 200;
    }

    public boolean logout() {
        Response response = serverRequest(HttpMethod.GET, "/" + userPath + "/" + logoutPath);
        return status(response) == 200;
    }

    public Book getBook(String isbn) {

        Response response = serverRequest(HttpMethod.GET, "/" + bookPath + "/" + isbn);
        return createBook(response);
    }

    public int getBookListMax() {
        Response response = serverRequest(HttpMethod.GET, "/" + bookPath + "/" + maxPath);
        return createDBMeta(response).getResultMax();
    }

    public LinkedList<Book> loadBooks(int start, int limit, boolean orderAsc, String string, String category) {
        string = URLEncoder.encode(string, StandardCharsets.UTF_8);
        category = URLEncoder.encode(category, StandardCharsets.UTF_8);
        String query = "/" + bookPath + "/" + start + "/" + limit + "/" + orderAsc + "?string=" + string + "?category=" + category;
        Response response = serverRequest(HttpMethod.GET, query);
        return createDBMeta(response).getBooks();
    }

    public LinkedList<Category> loadCategories() {
        Response response = serverRequest(HttpMethod.GET, "/" + categoryPath);
        return createDBMeta(response).getCategories();
    }

    public LinkedList<Author> loadAuthors() {
        Response response = serverRequest(HttpMethod.GET, "/" + authorPath);
        return createDBMeta(response).getAuthors();
    }

    public LinkedList<Publisher> loadPublishers() {
        Response response = serverRequest(HttpMethod.GET, "/" + publisherPath);
        return createDBMeta(response).getPublishers();
    }

    public boolean editBook(Book selection) {
        Response response = serverRequest(HttpMethod.PUT, "/" + bookPath + "/" + selection.getIsbn(), selection);
        return status(response) == 200;
    }

    public boolean createBook(Book selection) {
        Response response = serverRequest(HttpMethod.POST, "/" + bookPath + "/" + selection.getIsbn(), selection);
        return status(response) == 200;
    }

    public boolean deleteBook(String isbn) {
        Response response = serverRequest(HttpMethod.DELETE, "/" + bookPath + "/" + isbn);
        return status(response) == 200;
    }

    public boolean resetDatabase() {
        Response response = serverRequest(HttpMethod.POST, "/" + userPath + "/" + resetPath);
        return status(response) == 200;
    }

    //----------Converter Methods (JSON -> POJO)

    private DBMeta createDBMeta(Response response) {
        DBMeta meta = null;
        if (status(response) == 200) meta = readJSON(response, DBMeta.class);
        if (meta == null) return new DBMeta();
        return meta;
    }

    private Book createBook(Response response) {
        Book book = null;
        if (status(response) == 200) book = readJSON(response, Book.class);
        if (book == null) return new Book("");
        return book;
    }

    private <T> T readJSON(Response response, Class<T> objectClass) {
        ObjectMapper mapper = new ObjectMapper();
        T res = null;
        try {
            res = mapper.readValue(response.readEntity(InputStream.class), objectClass);
        } catch (IOException e) {
            System.err.println("Error reading Response JSON String from Server into " + objectClass.getName());
        }
        return res;
    }

    //------------Server Request Methods (Response)

    private Response serverRequest(String crud, String path) {
        return serverRequest(crud, path, Optional.empty());
    }

    private <T> Response serverRequest(String crud, String path, T object) {
        WebTarget target = getTarget(crud, path);
        ObjectMapper mapper = new ObjectMapper();

        try {
            String json = object == null ? "" : mapper.writeValueAsString(object);

            return switch (crud) {
                case "GET" -> target.request().accept(MediaType.APPLICATION_JSON).get();
                case "PUT" -> target.request().put(Entity.entity(json, MediaType.APPLICATION_JSON));
                case "POST" -> target.request().post(Entity.entity(json, MediaType.APPLICATION_JSON));
                case "DELETE" -> target.request().delete();
                default -> Response.noContent().build();
            };

        } catch (JsonProcessingException e) {
            System.err.println("Error parsing Object from " + object.getClass() + " to JSON.");
        } catch (RuntimeException e) {
            System.err.println("Connection refused.");
        }
        return Response.noContent().build();
    }

    //--------Debug

    private WebTarget getTarget(String crud, String uri) {
        System.out.printf("%n--- %s %s%s%n", crud, baseURI, uri);
        return client.target(baseURI + uri);
    }

    private int status(Response response) {
        int code = response.getStatus();
        String reason = response.getStatusInfo().getReasonPhrase();
        System.out.printf("Status: %d %s%n", code, reason);
        return code;
    }
}