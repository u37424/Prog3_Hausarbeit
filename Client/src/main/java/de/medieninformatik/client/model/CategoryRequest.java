package de.medieninformatik.client.model;

import de.medieninformatik.common.Category;
import de.medieninformatik.common.DBMeta;
import jakarta.ws.rs.core.Response;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.ResourceBundle;

public class CategoryRequest {
    private final Request request;
    private final String categoryPath;

    private Category item;
    private LinkedList<Category> categories;
    private int categoryMax;

    public CategoryRequest(Request request) {
        this.request = request;

        ResourceBundle bundle = ResourceBundle.getBundle("ServerResources");
        this.categoryPath = "/"+bundle.getString("Category.Path");
    }

    //---------POJOS FOR MODEL

    public Category getItem() {
        return item;
    }

    public LinkedList<Category> getItemList() {
        return categories;
    }

    public int getMax() {
        return categoryMax;
    }

    //--------REQUESTS TO SERVER

    public void loadItem(String id) {
        Response response = request.serverRequest("GET",  categoryPath + "/" + id);
        this.item = request.createObject(response, Category.class);
    }

    public void loadAll() {
        Response response = request.serverRequest("GET", categoryPath);
        this.categories = request.createObject(response, DBMeta.class).getCategories();
    }

    public void loadSelection(int start, int limit, boolean orderAsc, String string) {
        string = URLEncoder.encode(string, StandardCharsets.UTF_8);
        String query = categoryPath + "/" + start + "/" + limit + "/" + orderAsc + "?string=" + string;
        Response response = request.serverRequest("GET", query);
        DBMeta result = request.createObject(response, DBMeta.class);
        this.categories = result.getCategories();
        this.categoryMax = result.getResultMax();
    }

    public boolean editItem() {
        Response response = request.serverRequest("PUT", categoryPath, item);
        return request.isOk(response);
    }

    public boolean createItem() {
        Response response = request.serverRequest("POST", categoryPath, item);
        return request.isCreated(response);
    }

    public boolean deleteItem(String id) {
        Response response = request.serverRequest("DELETE",  categoryPath + "/" + id);
        return request.isOk(response);
    }

    public void reset() {
        this.item = new Category();
    }
}
