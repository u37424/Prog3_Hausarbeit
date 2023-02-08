package de.medieninformatik.client.model;

import de.medieninformatik.common.Book;
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
 * Diese Klasse wird verwendet, um Anfragen an einen REST Server zu Buechern zu senden und Antworten vom Server zu verarbeiten.
 * Buecher koennen als Listen oder einzeln abgefragt werden.
 * Buecher koennen erstellt, veraendert oder geloescht werden.
 * Die Klasse speichert alle benoetigten Daten zum Zugriff fuer andere Klassen.
 * Es kann jeweils eine Liste an Buechern und ein konkretes Buch gespeichert werden.
 * </p>
 */
public class BookModel {
    private final Request request;
    private final String bookPath;

    private Book item;
    private LinkedList<Book> books;
    private int bookMax;

    public BookModel(Request request) {
        this.request = request;

        ResourceBundle bundle = ResourceBundle.getBundle("ServerResources");
        this.bookPath = "/" + bundle.getString("Book.Path");
    }

    //--------POJOS FUER MODEL

    /**
     * Gibt das aktuell gespeicherte konkrete Buch-Objekt zurueck.
     *
     * @return konkretes Buch-Objekt
     */
    public Book getItem() {
        return item;
    }

    /**
     * Gibt die aktuell gespeicherte Liste von Buechern zurueck.
     *
     * @return Liste an Buechern
     */
    public LinkedList<Book> getItemList() {
        return books;
    }

    /**
     * Gibt die aktuell gespeicherte Maximalanzahl an Buechern zurueck.
     * Die Maximalanzahl gibt nur auskunft ueber die Maximalanzahl der zuletzt ausgefuehrten Anfrage aus,
     * nicht unbedingt ueber die Maximalanzahl an Buechern in der Datenbank.
     *
     * @return Maximalanzahl der zuletzt ausgefuehrten Anfrage
     */
    public int getMax() {
        return bookMax;
    }

    //------------SERVER REQUESTS

    /**
     * Fragt ein spezifisches Buch beim Server an.
     *
     * @param id ISBN des Buches
     */
    public void loadItem(String id) {
        Response response = request.serverRequest("GET", bookPath + "/" + id);
        this.item = request.JSONtoObject(response, Book.class);
    }

    /**
     * Fragt eine komplette Liste aller Buecher beim Server an.
     */
    public void loadAll() {
        Response response = request.serverRequest("GET", bookPath);
        this.books = request.JSONtoObject(response, DBMeta.class).getBooks();
    }

    /**
     * Fragt eine durch Filter einschraenkbare Liste von Buechern beim Server an.
     *
     * @param start    Start der Liste
     * @param limit    Groesse der Liste
     * @param orderAsc Sortierung der Liste
     * @param string   String, der im Titel der Buecher enthalten sein muss
     * @param category Kategorie, die den Buechern zugeteilt sein muss
     */
    public void loadSelection(int start, int limit, boolean orderAsc, String string, String category) {
        string = URLEncoder.encode(string, StandardCharsets.UTF_8);
        category = URLEncoder.encode(category, StandardCharsets.UTF_8);
        String query = bookPath + "/" + start + "/" + limit + "/" + orderAsc + "?string=" + string + "&category=" + category;
        Response response = request.serverRequest("GET", query);
        DBMeta result = request.JSONtoObject(response, DBMeta.class);
        this.books = result.getBooks();
        this.bookMax = result.getResultMax();
    }

    /**
     * Uebergibt eine aktuelle Version eines Buches an den Server.
     * Der Server aktualisiert vorhandene Daten zum Buch mit den aktuellen Daten.
     * Das Buch muss in der Datenbank vorhanden sein.
     *
     * @return Erfolgsstatus der Anweisung
     */
    public boolean editItem() {
        Response response = request.serverRequest("PUT", bookPath, item);
        return request.isOk(response);
    }

    /**
     * Uebergibt ein neues Buch an den Server.
     * Der Server nimmt das neue Buch in der Datenbank auf.
     * Das Buch darf in der Datenbank noch nicht vorhanden sein.
     *
     * @return Erfolgsstatus der Anweisung
     */
    public boolean createItem() {
        Response response = request.serverRequest("POST", bookPath, item);
        return request.isCreated(response);
    }

    /**
     * Weist den Server an, das angegebene Buch aus der Datenbank zu loeschen.
     * Das Buch muss in der Datenbank einmalig vorhanden sein.
     *
     * @param id ISBN des Buches
     * @return Erfolgsstatus der Anwendung
     */
    public boolean deleteItem(String id) {
        Response response = request.serverRequest("DELETE", bookPath + "/" + id);
        return request.isOk(response);
    }

    /**
     * Setzt die aktuelle Buch-Auswahl auf ein leeres Buch zurueck.
     */
    public void clear() {
        this.item = new Book();
    }
}
