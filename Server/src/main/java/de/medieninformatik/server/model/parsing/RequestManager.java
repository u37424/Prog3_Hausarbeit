package de.medieninformatik.server.model.parsing;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Luca Spirka m29987
 * @version 1.0
 * <p>
 * Programmieren 3 - Hausarbeit.
 * <p>
 * 2023-01-31
 * <p>
 * Die Klasse stellt alle Manager fuer die einzelnen Ressourcen bereit.
 * Ebenso enthaelt sie Methoden zum Wandeln zwischen Objekten und JSON.
 * Der ResourceManager ist ein genereller Manager und wurde als Singleton entworfen, um fuer alle resourcen benutzt werden zu koennen.
 */
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

    /**
     * Gibt dem manager fuer Buecher zurueck
     *
     * @return instanz des BookManager
     */
    public BookManager getBookManager() {
        return bookManager;
    }

    /**
     * Gibt dem manager fuer Kategorien zurueck
     *
     * @return instanz des CategoryManager
     */
    public CategoryManager getCategoryManager() {
        return categoryManager;
    }

    /**
     * Gibt dem manager fuer Autoren zurueck
     *
     * @return instanz des AutorManager
     */
    public AuthorManager getAuthorManager() {
        return authorManager;
    }

    /**
     * Gibt dem manager fuer Publisher zurueck
     *
     * @return instanz des PublisherManager
     */
    public PublisherManager getPublisherManager() {
        return publisherManager;
    }

    /**
     * Wandelt ein Objekt in einen JSON String um.
     *
     * @param object Objekt das umgewandlet werden soll
     * @param <T>    Art des Objektes
     * @return JSON String
     * @throws JsonProcessingException Wenn Umwandlung fehlerhaft
     */
    public <T> String asJSON(T object) throws JsonProcessingException {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            System.err.println("Cannot parse " + object.getClass() + " to JSON.");
            throw e;
        }
    }

    /**
     * Wandelt einen JSON String auf ein Objekt um.
     *
     * @param json   JSON String, der umgewandlet werden soll
     * @param tClass Klasse des Objektes
     * @param <T>    Art des Objektes
     * @return Objekt, das aus dem JSON String befuellt wurde
     * @throws JsonProcessingException wenn Umwandlung fehlerhaft
     */
    public <T> T JSONasObject(String json, Class<T> tClass) throws JsonProcessingException {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(json, tClass);
        } catch (JsonProcessingException e) {
            System.err.println("Cannot parse JSON to " + tClass);
            throw e;
        }
    }

    /**
     * Liest eine Zahl aus einem ResultSet mit nur einer Spalte und einer Zeile.
     *
     * @param set ResultSet mit einer Anzahl
     * @return Anzahl die im ResultSet gespeichert ist als Integer
     * @throws SQLException, wenn Lesen fehlerhaft
     */
    public int count(ResultSet set) throws SQLException {
        int count = 0;
        while (set.next()) {
            count = set.getInt(1);
        }
        return count;
    }

    /**
     * Gibt die Instanz des Managers zurueck.
     * Sie wird verwendet, um allen Ressourcen eine Schnittstelle fuer die Datenbankkommunikation und Befehlsausfuehrung zu geben.
     * Ausserdem wird dadurch Code Duplizierung oder eine Vielzahl an weiterleitenden Methoden vermieden.
     *
     * @return Singleton Instanz des Managers
     */
    public static RequestManager getInstance() {
        return instance;
    }
}
