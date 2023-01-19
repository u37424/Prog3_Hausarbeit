package de.medieninformatik.client.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.medieninformatik.common.Category;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.io.IOException;
import java.io.InputStream;
import java.util.ResourceBundle;

public class Request {
    //Fragt Server an, gewisse Funktionen auszufuehren.
    //Uebergibt JSON Strings bei einigen Anfragen
    //Erhaelt meistens eine JSON Antwort, die auf POJOS umgewandelt werden muessen
    //Eine Methode kann unterschiedliche Ressourcen beim Server ansprechen. Unterschiedliche ressourcen = unterschiedliche JSON Objekte
    //Umwandlung durch einheitliches JSON Objet / Objekte oder durch verschiedene

    private final Client client;
    private static final Request instance = new Request();
    private final String baseURI;

    private Request() {
        //Serverattribute
        ResourceBundle bundle = ResourceBundle.getBundle("Server");
        String serverAddress = bundle.getString("ServerAddress");
        String port = bundle.getString("Port");
        String path = bundle.getString("Path");

        final String BASE_URI = "http://" + serverAddress + ":" + port + "/" + path;
        this.baseURI = BASE_URI;
        //Client zu Server
        this.client = ClientBuilder.newClient();
    }

    //Versionen: getBook, getTitleList, getCategories...
    public void getTest() {
        WebTarget target = getTarget("GET", "/data/test");
        try {
            Response response =
                    target.request().accept(MediaType.APPLICATION_JSON).get();
            ObjectMapper mapper = new ObjectMapper();
            if (status(response) == 200 && response.getLength() != 0) {
                Category cat = mapper.readValue(response.readEntity(InputStream.class), Category.class);
                System.out.println(cat);
            }
        } catch (RuntimeException e) {
            System.err.println("Error in communication to server.");
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("Error in reading Server Response");
            e.printStackTrace();
        }
    }

    public void putTest() {
        WebTarget target = getTarget("PUT", "/data/test");
        Category c = new Category(2);
        c.setName("PutCategory");
        ObjectMapper mapper = new ObjectMapper();
        String json;
        try {
            json = mapper.writeValueAsString(c);
            Response response = target.request().put(Entity.entity(json, MediaType.APPLICATION_JSON));
            status(response);
        } catch (RuntimeException e) {
            System.err.println("Error in communication to server.");
        } catch (JsonProcessingException e) {
            System.err.println("Error with writing JSON Object.");
        }
    }

    public boolean login() {
        WebTarget target = getTarget("GET", "/data/login");
        return isOk(target);
    }

    public boolean logout() {
        WebTarget target = getTarget("GET", "/data/logout");
        return isOk(target);
    }

    private boolean isOk(WebTarget target) {
        try {
            Response response =
                    target.request().accept(MediaType.APPLICATION_JSON).get();
            if (status(response) == 200) {
                return true;
            }
            return false;
        } catch (RuntimeException e) {
            System.err.println("Error in communication to server.");
            return false;
        }
    }

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

    public static Request getInstance() {
        return instance;
    }
}
