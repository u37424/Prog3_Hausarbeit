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

    private Publisher selection;
    private LinkedList<Publisher> publishers;
    private int publisherMax;

    public PublisherRequest(Request request) {
        this.request = request;

        ResourceBundle bundle = ResourceBundle.getBundle("ServerResources");
        this.publisherPath = "/"+bundle.getString("Publisher.Path");
    }

    //----------POJOs FOR MODEL

    public Publisher getSelection() {
        return selection;
    }

    public LinkedList<Publisher> getPublishers() {
        return publishers;
    }

    public int getMax() {
        return publisherMax;
    }

    //--------REQUESTS TO SERVER

    public void getPublisher(int id) {
        Response response = request.serverRequest("GET",  publisherPath + "/" + id);
        this.selection = request.createObject(response, Publisher.class);
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

    public boolean editPublisher() {
        Response response = request.serverRequest("PUT", publisherPath, selection);
        return request.isOk(response);
    }

    public boolean createPublisher() {
        Response response = request.serverRequest("POST", publisherPath, selection);
        return request.isOk(response);
    }

    public boolean deletePublisher(int id) {
        Response response = request.serverRequest("DELETE",  publisherPath + "/" + id);
        return request.isOk(response);
    }

    public void reset() {
        this.selection = new Publisher();
    }
}
