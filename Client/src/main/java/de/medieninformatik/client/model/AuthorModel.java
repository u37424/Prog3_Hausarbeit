package de.medieninformatik.client.model;

import de.medieninformatik.common.Author;
import de.medieninformatik.common.DBMeta;
import jakarta.ws.rs.core.Response;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.ResourceBundle;

/**
 * @author Luca Spirka m29987
 * @version 1.0
 * <p>
 * Programmieren 3 - Hausarbeit.
 * </p>
 * 2023-01-31
 * <p>
 * Diese Klasse wird verwendet, um Anfragen an einen REST Server zu Autoren zu senden und Antworten vom Server zu verarbeiten.
 * Autoren koennen als Listen oder einzeln abgefragt werden.
 * Autoren koennen erstellt, veraendert oder geloescht werden.
 * Die Klasse speichert alle benoetigten Daten zum Zugriff fuer andere Klassen.
 * Es kann jeweils eine Liste an Autoren und ein konkreter Autor gespeichert werden.
 * </p>
 */
public class AuthorModel {
    private final Request request;
    private final String authorPath;

    private Author item;
    private LinkedList<Author> authors;
    private int authorMax;

    public AuthorModel(Request request) {
        this.request = request;

        ResourceBundle bundle = ResourceBundle.getBundle("ServerResources");
        this.authorPath = "/" + bundle.getString("Author.Path");
    }

    //----------POJOS FUER MODEL


    /**
     * Gibt das aktuell gespeicherte konkrete Autor-Objekt zurueck.
     *
     * @return konkretes Autor-Objekt
     */
    public Author getItem() {
        return item;
    }

    /**
     * Gibt die aktuell gespeicherte Liste von Autoren zurueck.
     *
     * @return Liste an Autoren
     */
    public LinkedList<Author> getItemList() {
        return authors;
    }

    /**
     * Gibt die aktuell gespeicherte Maximalanzahl an Autoren zurueck.
     * Die Maximalanzahl gibt nur auskunft ueber die Maximalanzahl der zuletzt ausgefuehrten Anfrage aus,
     * nicht unbedingt ueber die Maximalanzahl an Autoren in der Datenbank.
     *
     * @return Maximalanzahl der zuletzt ausgefuehrten Anfrage
     */
    public int getMax() {
        return authorMax;
    }

    //-----------REQUESTS AN SERVER

    /**
     * Fragt einen spezifischen Autor beim Server an.
     *
     * @param id ID des Autors
     */
    public void loadItem(String id) {
        Response response = request.serverRequest("GET", authorPath + "/" + id);
        this.item = request.JSONtoObject(response, Author.class);
    }

    /**
     * Fragt eine komplette Liste aller Autoren beim Server an.
     */
    public void loadAll() {
        Response response = request.serverRequest("GET", authorPath);
        this.authors = request.JSONtoObject(response, DBMeta.class).getAuthors();
    }

    /**
     * Fragt eine durch Filter einschraenkbare Liste von Autoren beim Server an.
     *
     * @param start    Start der Liste
     * @param limit    Groesse der Liste
     * @param orderAsc Sortierung der Liste
     * @param string   String, der im Namen (Vor- oder Nachname) der Autoren enthalten sein muss
     */
    public void loadSelection(int start, int limit, boolean orderAsc, String string) {
        string = URLEncoder.encode(string, StandardCharsets.UTF_8);
        String query = authorPath + "/" + start + "/" + limit + "/" + orderAsc + "?string=" + string;
        Response response = request.serverRequest("GET", query);
        DBMeta result = request.JSONtoObject(response, DBMeta.class);
        this.authors = result.getAuthors();
        this.authorMax = result.getResultMax();
    }

    /**
     * Uebergibt eine aktuelle Version eines Autors an den Server.
     * Der Server aktualisiert vorhandene Daten zum Autor mit den aktuellen Daten.
     * Der Autor muss in der Datenbank vorhanden sein.
     *
     * @return Erfolgsstatus der Anweisung
     */
    public boolean editItem() {
        Response response = request.serverRequest("PUT", authorPath, item);
        return request.isOk(response);
    }

    /**
     * Uebergibt einen neuen Autor an den Server.
     * Der Server nimmt den neuen Autor in der Datenbank auf.
     * Der Autor darf in der Datenbank noch nicht vorhanden sein.
     *
     * @return Erfolgsstatus der Anweisung
     */
    public boolean createItem() {
        Response response = request.serverRequest("POST", authorPath, item);
        return request.isCreated(response);
    }

    /**
     * Weist den Server an, den angegebenen Autoren aus der Datenbank zu loeschen.
     * Der Autor muss in der Datenbank einmalig vorhanden sein.
     *
     * @param id ID des Autors
     * @return Erfolgsstatus der Anwendung
     */
    public boolean deleteItem(String id) {
        Response response = request.serverRequest("DELETE", authorPath + "/" + id);
        return request.isOk(response);
    }

    /**
     * Setzt die aktuelle Autor-Auswahl auf einen leeren Autor zurueck.
     */
    public void clear() {
        this.item = new Author();
    }
}
