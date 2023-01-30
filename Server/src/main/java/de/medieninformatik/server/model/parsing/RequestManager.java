package de.medieninformatik.server.model.parsing;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class RequestManager {
    private final static RequestManager instance = new RequestManager();

    private final BookManager bookManager;
    private final CategoryManager categoryManager;
    private final AuthorManager authorManager;
    private final PublisherManager publisherManager;

    public RequestManager() {
        this.bookManager = new BookManager();
        this.categoryManager = new CategoryManager();
        this.authorManager = new AuthorManager();
        this.publisherManager = new PublisherManager();
    }

    public BookManager getBookManager() {
        return bookManager;
    }

    public CategoryManager getCategoryManager() {
        return categoryManager;
    }

    public AuthorManager getAuthorManager() {
        return authorManager;
    }

    public PublisherManager getPublisherManager() {
        return publisherManager;
    }

    public <T> String asJSON(T object) throws JsonProcessingException {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            System.err.println("Cannot parse " + object.getClass() + " to JSON.");
            throw e;
        }
    }

    public <T> T JSONasObject(String json, Class<T> tClass) throws JsonProcessingException {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(json, tClass);
        } catch (JsonProcessingException e) {
            System.err.println("Cannot parse JSON to " + tClass);
            throw e;
        }
    }

    public static RequestManager getInstance() {
        return instance;
    }
}
