package de.medieninformatik.client.model;

import de.medieninformatik.common.DBMeta;
import de.medieninformatik.common.Publisher;
import jakarta.ws.rs.core.Response;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.ResourceBundle;

public class PublisherRequest {
    private final Request request;
    private final String publisherPath;

    private Publisher item;
    private LinkedList<Publisher> publishers;
    private int publisherMax;

    public PublisherRequest(Request request) {
        this.request = request;

        ResourceBundle bundle = ResourceBundle.getBundle("ServerResources");
        this.publisherPath = "/"+bundle.getString("Publisher.Path");
    }

    //----------POJOs FOR MODEL

    public Publisher getItem() {
        return item;
    }

    public LinkedList<Publisher> getItemList() {
        return publishers;
    }

    public int getMax() {
        return publisherMax;
    }

    //--------REQUESTS TO SERVER

    public void loadItem(String id) {
        Response response = request.serverRequest("GET",  publisherPath + "/" + id);
        this.item = request.createObject(response, Publisher.class);
    }

    public void loadAll() {
        Response response = request.serverRequest("GET", publisherPath);
        this.publishers = request.createObject(response, DBMeta.class).getPublishers();
    }

    public void loadSelection(int start, int limit, boolean orderAsc, String string) {
        string = URLEncoder.encode(string, StandardCharsets.UTF_8);
        String query = publisherPath + "/" + start + "/" + limit + "/" + orderAsc + "?string=" + string;
        Response response = request.serverRequest("GET", query);
        DBMeta result = request.createObject(response, DBMeta.class);
        this.publishers = result.getPublishers();
        this.publisherMax = result.getResultMax();
    }

    public boolean editItem() {
        Response response = request.serverRequest("PUT", publisherPath, item);
        return request.isOk(response);
    }

    public boolean createItem() {
        Response response = request.serverRequest("POST", publisherPath, item);
        return request.isCreated(response);
    }

    public boolean deleteItem(String id) {
        Response response = request.serverRequest("DELETE",  publisherPath + "/" + id);
        return request.isOk(response);
    }

    public void reset() {
        this.item = new Publisher();
    }
}
