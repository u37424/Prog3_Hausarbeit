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

/**
 * @author Luca Spirka m29987
 * @version 1.0
 * <p>
 * Programmieren 3 - Hausarbeit.
 * </p>
 * 2023-01-31
 * <p>
 * Diese Klasse wird verwendet, um Anfragen an einen REST Server zu senden und Antworten vom Server zu verarbeiten.
 * Es koennen GET, PUT, POST und DELETE Anfragen gestellt werden.
 * Die Verarbeitung von Daten geschieht mithilfe von JSON.
 * </p>
 */
public class Request {
    private final String hostPort;
    private final String basePath;
    private String baseURI;
    private final Client client;

    public Request() {
        ResourceBundle bundle = ResourceBundle.getBundle("ServerResources");
        //Set BASE URI
        String hostAddress = bundle.getString("Host.Address");
        this.hostPort = bundle.getString("Host.Port");
        this.basePath = bundle.getString("Base.Path");
        this.baseURI = "http://" + hostAddress + ":" + hostPort + "/" + basePath;
        this.client = ClientBuilder.newClient();
    }

    /**
     * Veraendert den Namen des Hosts in der Adresse.
     *
     * @param hostName neuer Hostname
     */
    public void changeHostName(String hostName) {
        this.baseURI = "http://" + hostName + ":" + hostPort + "/" + basePath;
    }

    //----------Response Reading/Converting Methoden

    /**
     * Prueft den Status einer Antwort des Servers auf Erfolgs.
     *
     * @param response Antwort des Servers
     * @return true, wenn Status der Antwort erfolgreich
     */
    boolean isOk(Response response) {
        if (response == null) return false;
        return status(response) == 200;
    }

    /**
     * Prueft den Status einer Antwort des Servers auf erfolgreiche Erstellung.
     *
     * @param response Antwort des Servers
     * @return true, wenn erfolgreich Erstellt
     */
    public boolean isCreated(Response response) {
        if (response == null) return false;
        return status(response) == 201;
    }

    /**
     * Liest einen JSON String aus einer Serverantwort aus und wandelt ihn in ein angegebenes (erwartetes) Objekt um.
     * Kann kein Objekt aus dem JSON String gelesen werden, wird ein leeres Objekt (kein null-Objekt) zurueckgegeben.
     *
     * @param response Antwort des Servers
     * @param tClass   Klasse des erwarteten Objektes
     * @param <T>      Art des erwarteten Objektes
     * @return Objekt der angegebenen Klasse
     */
    <T> T JSONtoObject(Response response, Class<T> tClass) {
        T object = null;
        if (isOk(response)) object = readJSON(response, tClass);
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

    /**
     * Liest einen JSON String aus einer Serverantwort und wandelt es in ein Objekt der angegebenen Klasse um.
     *
     * @param response    Serverantwort
     * @param objectClass Klasse des erwarteten Objektes
     * @param <T>         Art des Objektes
     * @return gelesenes Objekt
     */
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

    /**
     * Stellt eine Anfrage an den angegebenen Ressourcenpfad beim Server.
     *
     * @param crud auszufuehrende Operation
     * @param path anzusprechender Ressourcenpfad
     * @return Antwort des Servers
     */
    Response serverRequest(String crud, String path) {
        return serverRequest(crud, path, null);
    }

    /**
     * Stellt eine Anfrage an den angegebenen Ressourcenpfad beim Server.
     * Ein Objekt kann ebenfalls mitgeliefert werden. Dieses wird dann als JSON String an den Server uebertragen.
     *
     * @param crud   auszufuehrende Operation
     * @param path   anzusprechender Ressourcenpfad
     * @param object zu uebertragenes Objekt
     * @param <T>    Art des Objektes
     * @return Antwort des Servers
     */
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
                default -> null;
            };
        } catch (JsonProcessingException e) {
            System.err.println("Error parsing Object from " + object.getClass() + " to JSON.");
        } catch (RuntimeException e) {
            System.err.println("Connection refused.");
        }
        return null;
    }

    //--------Debug/Logging Methoden

    /**
     * Debug Methode: Gibt Zielanfrage auf Konsole aus.
     *
     * @param crud Operation
     * @param uri  Zielpfad
     * @return benutzbares WebTarget
     */
    private WebTarget getTarget(String crud, String uri) {
        System.out.printf("%n--- %s %s%s%n", crud, baseURI, uri);
        return client.target(baseURI + uri);
    }

    /**
     * Debug Methode: Gibt den Status einer Serverantwort auf der Konsole aus.
     *
     * @param response Antwort des Servers
     * @return Statuscode der Antwort
     */
    private int status(Response response) {
        int code = response.getStatus();
        String reason = response.getStatusInfo().getReasonPhrase();
        System.out.printf("Status: %d %s%n", code, reason);
        return code;
    }
}
