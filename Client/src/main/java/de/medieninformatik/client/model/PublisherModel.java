package de.medieninformatik.client.model;

import de.medieninformatik.common.DBMeta;
import de.medieninformatik.common.Publisher;
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
 * Diese Klasse wird verwendet, um Anfragen an einen REST Server zu Publishern zu senden und Antworten vom Server zu verarbeiten.
 * Publisher koennen als Listen oder einzeln abgefragt werden.
 * Autoren koennen erstellt, veraendert oder geloescht werden.
 * Die Klasse speichert alle benoetigten Daten zum Zugriff fuer andere Klassen.
 * Es kann jeweils eine Liste an Publishern und ein konkreter Publisher gespeichert werden.
 * </p>
 */
public class PublisherModel {
    private final Request request;
    private final String publisherPath;

    private Publisher item;
    private LinkedList<Publisher> publishers;
    private int publisherMax;

    public PublisherModel(Request request) {
        this.request = request;

        ResourceBundle bundle = ResourceBundle.getBundle("ServerResources");
        this.publisherPath = "/" + bundle.getString("Publisher.Path");
    }

    //----------POJOs FUER MODEL

    /**
     * Gibt das aktuell gespeicherte konkrete Publisher-Objekt zurueck.
     *
     * @return konkretes Publisher-Objekt
     */
    public Publisher getItem() {
        return item;
    }

    /**
     * Gibt die aktuell gespeicherte Liste von Publishern zurueck.
     *
     * @return Liste an Publishern
     */
    public LinkedList<Publisher> getItemList() {
        return publishers;
    }

    /**
     * Gibt die aktuell gespeicherte Maximalanzahl an Publishern zurueck.
     * Die Maximalanzahl gibt nur auskunft ueber die Maximalanzahl der zuletzt ausgefuehrten Anfrage aus,
     * nicht unbedingt ueber die Maximalanzahl an Publishern in der Datenbank.
     *
     * @return Maximalanzahl der zuletzt ausgefuehrten Anfrage
     */
    public int getMax() {
        return publisherMax;
    }

    //--------REQUESTS AN SERVER

    /**
     * Fragt einen spezifischen Publisher beim Server an.
     *
     * @param id ID des Publishers
     */
    public void loadItem(String id) {
        Response response = request.serverRequest("GET", publisherPath + "/" + id);
        this.item = request.JSONtoObject(response, Publisher.class);
    }

    /**
     * Fragt eine komplette Liste aller Publisher beim Server an.
     */
    public void loadAll() {
        Response response = request.serverRequest("GET", publisherPath);
        this.publishers = request.JSONtoObject(response, DBMeta.class).getPublishers();
    }

    /**
     * Fragt eine durch Filter einschraenkbare Liste von Publishern beim Server an.
     *
     * @param start    Start der Liste
     * @param limit    Groesse der Liste
     * @param orderAsc Sortierung der Liste
     * @param string   String, der im Namen des Publishers enthalten sein muss
     */
    public void loadSelection(int start, int limit, boolean orderAsc, String string) {
        string = URLEncoder.encode(string, StandardCharsets.UTF_8);
        String query = publisherPath + "/" + start + "/" + limit + "/" + orderAsc + "?string=" + string;
        Response response = request.serverRequest("GET", query);
        DBMeta result = request.JSONtoObject(response, DBMeta.class);
        this.publishers = result.getPublishers();
        this.publisherMax = result.getResultMax();
    }

    /**
     * Uebergibt eine aktuelle Version eines Publishers an den Server.
     * Der Server aktualisiert vorhandene Daten zum Publisher mit den aktuellen Daten.
     * Der Publisher muss in der Datenbank vorhanden sein.
     *
     * @return Erfolgsstatus der Anweisung
     */
    public boolean editItem() {
        Response response = request.serverRequest("PUT", publisherPath, item);
        return request.isOk(response);
    }

    /**
     * Uebergibt einen neuen Publisher an den Server.
     * Der Server nimmt den neuen Publisher in der Datenbank auf.
     * Der Publisher darf in der Datenbank noch nicht vorhanden sein.
     *
     * @return Erfolgsstatus der Anweisung
     */
    public boolean createItem() {
        Response response = request.serverRequest("POST", publisherPath, item);
        return request.isCreated(response);
    }

    /**
     * Weist den Server an, den angegebenen Publisher aus der Datenbank zu loeschen.
     * Der Publisher muss in der Datenbank einmalig vorhanden sein.
     *
     * @param id ID des Publishers
     * @return Erfolgsstatus der Anwendung
     */
    public boolean deleteItem(String id) {
        Response response = request.serverRequest("DELETE", publisherPath + "/" + id);
        return request.isOk(response);
    }


    /**
     * Setzt die aktuelle Publisher-Auswahl auf einen leeren Publisher zurueck.
     */
    public void clear() {
        this.item = new Publisher();
    }
}
