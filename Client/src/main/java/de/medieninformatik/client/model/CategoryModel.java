package de.medieninformatik.client.model;

import de.medieninformatik.common.Category;
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
 * Diese Klasse wird verwendet, um Anfragen an einen REST Server zu Kategorien zu senden und Antworten vom Server zu verarbeiten.
 * Kategorien koennen als Listen oder einzeln abgefragt werden.
 * Kategorien koennen erstellt, veraendert oder geloescht werden.
 * Die Klasse speichert alle benoetigten Daten zum Zugriff fuer andere Klassen.
 * Es kann jeweils eine Liste an Kategorien und eine konkrete Kategorie gespeichert werden.
 * </p>
 */
public class CategoryModel {
    private final Request request;
    private final String categoryPath;

    private Category item;
    private LinkedList<Category> categories;
    private int categoryMax;

    public CategoryModel(Request request) {
        this.request = request;

        ResourceBundle bundle = ResourceBundle.getBundle("ServerResources");
        this.categoryPath = "/" + bundle.getString("Category.Path");
    }

    //---------POJOS FUER MODEL

    /**
     * Gibt das aktuell gespeicherte konkrete Kategorie-Objekt zurueck.
     *
     * @return konkretes Kategorie-Objekt
     */
    public Category getItem() {
        return item;
    }

    /**
     * Gibt die aktuell gespeicherte Liste von Kategorien zurueck.
     *
     * @return Liste an Kategorien
     */
    public LinkedList<Category> getItemList() {
        return categories;
    }

    /**
     * Gibt die aktuell gespeicherte Maximalanzahl an Kategorien zurueck.
     * Die Maximalanzahl gibt nur auskunft ueber die Maximalanzahl der zuletzt ausgefuehrten Anfrage aus,
     * nicht unbedingt ueber die Maximalanzahl an Kategorien in der Datenbank.
     *
     * @return Maximalanzahl der zuletzt ausgefuehrten Anfrage
     */
    public int getMax() {
        return categoryMax;
    }

    //--------REQUESTS AN SERVER

    /**
     * Fragt eine spezifische Kategorie beim Server an.
     *
     * @param id ID der Kategorie
     */
    public void loadItem(String id) {
        Response response = request.serverRequest("GET", categoryPath + "/" + id);
        this.item = request.JSONtoObject(response, Category.class);
    }

    /**
     * Fragt eine komplette Liste aller Kategorien beim Server an.
     */
    public void loadAll() {
        Response response = request.serverRequest("GET", categoryPath);
        this.categories = request.JSONtoObject(response, DBMeta.class).getCategories();
    }

    /**
     * Fragt eine durch Filter einschraenkbare Liste von Kategorien beim Server an.
     *
     * @param start    Start der Liste
     * @param limit    Groesse der Liste
     * @param orderAsc Sortierung der Liste
     * @param string   String, der im Namen der Kategorien enthalten sein muss
     */
    public void loadSelection(int start, int limit, boolean orderAsc, String string) {
        string = URLEncoder.encode(string, StandardCharsets.UTF_8);
        String query = categoryPath + "/" + start + "/" + limit + "/" + orderAsc + "?string=" + string;
        Response response = request.serverRequest("GET", query);
        DBMeta result = request.JSONtoObject(response, DBMeta.class);
        this.categories = result.getCategories();
        this.categoryMax = result.getResultMax();
    }

    /**
     * Uebergibt eine aktuelle Version einer Kategorie an den Server.
     * Der Server aktualisiert vorhandene Daten zur Kategorie mit den aktuellen Daten.
     * Die Kategorie muss in der Datenbank vorhanden sein.
     *
     * @return Erfolgsstatus der Anweisung
     */
    public boolean editItem() {
        Response response = request.serverRequest("PUT", categoryPath, item);
        return request.isOk(response);
    }

    /**
     * Uebergibt eine neue Kategorie an den Server.
     * Der Server nimmt die neue Kategorie in der Datenbank auf.
     * Die Kategorie darf in der Datenbank noch nicht vorhanden sein.
     *
     * @return Erfolgsstatus der Anweisung
     */
    public boolean createItem() {
        Response response = request.serverRequest("POST", categoryPath, item);
        return request.isCreated(response);
    }

    /**
     * Weist den Server an, die angegebene Kategorie aus der Datenbank zu loeschen.
     * Die Kategorie muss in der Datenbank einmalig vorhanden sein.
     *
     * @param id ID der Kategorie
     * @return Erfolgsstatus der Anwendung
     */
    public boolean deleteItem(String id) {
        Response response = request.serverRequest("DELETE", categoryPath + "/" + id);
        return request.isOk(response);
    }

    /**
     * Setzt die aktuelle Kategorie-Auswahl auf eine leere Kategorie zurueck.
     */
    public void clear() {
        this.item = new Category();
    }
}
