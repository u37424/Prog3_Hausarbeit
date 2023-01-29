package de.medieninformatik.client.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ResourceBundle;

public class Request {
    //Path Attributes
    private final String userPath;
    private final String loginPath;
    private final String logoutPath;
    private final String resetPath;

    private final String baseURI;
    private final Client client;

    public Request() {
        ResourceBundle bundle = ResourceBundle.getBundle("ServerResources");
        //Set BASE URI
        String hostAddress = bundle.getString("Host.Address");
        String hostPort = bundle.getString("Host.Port");
        String basePath = bundle.getString("Base.Path");
        this.baseURI = "http://" + hostAddress + ":" + hostPort + "/" + basePath;

        //Set Path Attributes
        this.userPath = bundle.getString("User.Path");
        this.loginPath = bundle.getString("User.Login.Path");
        this.logoutPath = bundle.getString("User.Logout.Path");
        this.resetPath = bundle.getString("User.Reset.Path");

        this.client = ClientBuilder.newClient();
    }

    //---------USER REQUESTS

    public boolean login() {
        Response response = serverRequest("GET", "/" + userPath + "/" + loginPath);
        return isOk(response);
    }

    public boolean logout() {
        Response response = serverRequest("GET", "/" + userPath + "/" + logoutPath);
        return isOk(response);
    }

    public boolean resetDatabase() {
        Response response = serverRequest("POST", "/" + userPath + "/" + resetPath);
        return isOk(response);
    }

    //----------Converter Methods (JSON -> POJO)

    boolean isOk(Response response) {
        return status(response) == 200;
    }

    <T> T createObject(Response response, Class<T> tClass) {
        T object = null;
        if (isOk(response)) object = parseJSON(response, tClass);
        if (object == null) {
            try {
                Constructor<T> constructor = tClass.getDeclaredConstructor();
                object = constructor.newInstance();
            } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
                System.err.println("Error converting JSON Response to " + tClass);
            }
        }
        return object;
    }


    <T> T parseJSON(Response response, Class<T> objectClass) {
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

    Response serverRequest(String crud, String path) {
        return serverRequest(crud, path, null);
    }

    <T> Response serverRequest(String crud, String path, T object) {
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
