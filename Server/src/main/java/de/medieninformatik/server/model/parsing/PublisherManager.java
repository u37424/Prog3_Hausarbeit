package de.medieninformatik.server.model.parsing;

import de.medieninformatik.common.DBMeta;
import de.medieninformatik.common.Publisher;
import de.medieninformatik.server.model.database.Database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;

/**
 * @author Luca Spirka m29987
 * @version 1.0
 * <p>
 * Programmieren 3 - Hausarbeit.
 * <p>
 * 2023-01-31
 * <p>
 * Die Klasse wird zur Arbeit mit Datenbankanfragen und Ergebnisumwandlungen fuer Publisher benutzt.
 * Es werden entsprechende SQL Anfragen nach den Anforderungen erstellt und an die Datenbank weitergeleitet.
 * Es koennen Publisher aus den Ergebnissen erstellt werden, oder Publisher in Anfragen umngewandelt werden.
 */
public class PublisherManager {
    private final static Database database = Database.getInstance();

    /**
     * Fragt alle vorhandenen Publisher beim Server an und wandelt diese in eine Liste von Publisher um.
     *
     * @return Liste von Publisher-Objekten.
     * @throws SQLException, wenn SQL Anfrage fehlerhaft
     */
    public LinkedList<Publisher> getAll() throws SQLException {
        String query = "SELECT * FROM publishers;";
        ResultSet set = database.query(query);
        return parsePublishers(set);
    }

    /**
     * Fragt alle Publisher an, die spezifischen Vorgaben entsprechen, und wandelt sie in eine Liste von Publishern um.
     *
     * @param start    Start der Liste
     * @param size     Groesse der Liste
     * @param orderAsc Ordnung der Liste
     * @param string   Zeichenkette, die im Namen enthalten sein muss
     * @return Liste von Publishern
     * @throws SQLException, wenn SQL Anfrage fehlerhaft
     */
    public LinkedList<Publisher> getSelection(int start, int size, boolean orderAsc, String string) throws SQLException {
        String queryStart = "SELECT * FROM publishers ";
        if (string == null) string = "";

        String range = "LIMIT " + start + "," + size;
        String order = orderAsc ? " ASC " : " DESC ";

        //Query bauen
        String query = queryStart +
                " WHERE name LIKE('%" + string + "%') " +
                " ORDER BY name" + order +
                range +
                ";";

        ResultSet set = database.query(query);
        return parsePublishers(set);
    }

    /**
     * Fragt einen Publisher von der Datenbank an, der in ein Publisher-Objekt umgewandelt wird.
     *
     * @param id ID des Publishers
     * @return Angefragter Publisher als Objekt
     * @throws SQLException, wenn SQL Anfrage fehlerhaft
     */
    public Publisher getItem(int id) throws SQLException {
        String getQuery = "SELECT * FROM publishers WHERE publisher_id = " + id + ";";
        ResultSet set = database.query(getQuery);
        return parsePublishers(set).get(0);
    }

    /**
     * Aktualisiert die vorhandenen Daten zu einem Publisher in der Datenbank mit den neuen Werten.
     * Der entsprechende Autor muss in der Datenbank vorhanden sein, damit er aktualisiert werden kann.
     *
     * @param publisher Publisher mit aktuellen Daten
     * @return Erfolgsstatus des Updates
     * @throws SQLException, wenn SQL Update fehlerhaft
     */
    public boolean putItem(Publisher publisher) throws SQLException {
        if (publisher == null) return false;
        int id = publisher.getPublisherId();
        if (countByID(id) != 1) return false;

        //Update bauen
        String update = "UPDATE publishers SET" +
                " name = '" + publisher.getName() +
                "', main_country = '" + publisher.getCountry() +
                "', year_of_foundation = " + publisher.getFoundation() +
                " WHERE publisher_id = " + publisher.getPublisherId() + ";";
        int res = database.update(update);
        return res == 1;
    }

    /**
     * Erstellt einen neuen Publisher in der Datenbank.
     * Der uebergebene Publisher darf noch nicht in der Datenbank enthalten sein.
     *
     * @param publisher zu erstellender Publisher
     * @return Erfolgsstatus der Anweisung
     * @throws SQLException, wenn SQL Update fehlerhaft
     */
    public boolean postItem(Publisher publisher) throws SQLException {
        if (publisher == null) return false;
        int id = publisher.getPublisherId();
        if (countByID(id) != 0) return false;

        //Update bauen
        String insert = "INSERT INTO publishers (publisher_id, name, main_country, year_of_foundation)" +
                " VALUES(" + id +
                ",'" + publisher.getName() +
                "','" + publisher.getCountry() +
                "'," + publisher.getFoundation() +
                ");";
        int res = database.update(insert);
        return res == 1;
    }

    /**
     * Loescht einen bestehenden Publisher aus der Datenbank, wenn sie existiert.
     *
     * @param id ID des zu loeschenden Publishers
     * @return Erfolgsstatus der Anweisung
     * @throws SQLException, wenn SQL Update fehlerhaft
     */
    public boolean deleteItem(int id) throws SQLException {
        if (countByID(id) == 0) return false;
        String delete = "DELETE FROM publishers WHERE publisher_id = " + id + ";";
        int res = database.update(delete);
        return res == 1;
    }

    /**
     * Wandelt ein ResultSet auf eine Liste an Publisher-Objekten um.
     *
     * @param set umzuwandelndes ResultSet
     * @return Liste an Publishern, die aus dem ResultSet erzeugt wurde
     * @throws SQLException, wenn Umwandlung fehlerhaft
     */
    private LinkedList<Publisher> parsePublishers(ResultSet set) throws SQLException {
        LinkedList<Publisher> publishers = new LinkedList<>();
        while (set.next()) {
            Publisher publisher = new Publisher();

            publisher.setPublisherId(set.getInt("publisher_id"));
            publisher.setName(set.getString("name"));
            publisher.setCountry(set.getString("main_country"));
            publisher.setFoundation(set.getInt("year_of_foundation"));

            publishers.add(publisher);
        }
        return publishers;
    }

    /**
     * Ermittelt die Maximalanzahl von Publishern in der Datenbank.
     *
     * @return Maximale Anzahl an Publishern, die in der Datenbank vorhanden sind
     * @throws SQLException, wenn SQL Anfrage fehlerhaft
     */
    public int getMax() throws SQLException {
        return getMax(null);
    }

    /**
     * Ermittelt fuer eine durch Filter eingeschraenkte Abfrage die Maximalanzahl von Publishern.
     *
     * @param string Zeichenkette, die im Namen (Vor- oder Nachname) enthalten sein muss
     * @return Maximalanzahl von entsprechenden Publishern
     * @throws SQLException, wenn SQL Anfrage fehlerhaft
     */
    public int getMax(String string) throws SQLException {
        if (string == null) string = "";
        String query = "SELECT COUNT(*) FROM publishers WHERE name LIKE('%" + string + "%');";
        ResultSet set = database.query(query);
        return RequestManager.getInstance().count(set);
    }

    /**
     * Wandelt eine (partielle) Liste von Publishern in ein DBMeta Objekt um.
     * Dem DBMeta Objekt kann ebenso die Maximalanzahl der kompletten Liste uebergeben werden.
     *
     * @param publishers (partielle) Liste von Publishern
     * @param max        Maximaler Wert der Ergebnisliste
     * @return umgewandeltes DBMeta Objekt
     * @throws SQLException, wenn SQL Anfrage fehlerhaft
     */
    public DBMeta asDBMeta(LinkedList<Publisher> publishers, int max) throws SQLException {
        DBMeta meta = new DBMeta();
        meta.setResultMax(max);
        meta.setPublishers(publishers);
        return meta;
    }

    /**
     * Zaehlt alle Publisher in der Datenbank, die unter einer ID zu finden sind.
     *
     * @param id ID des zu zaehlenden Publishers
     * @return Anzahl von entsprechenden Publishern
     * @throws SQLException, wenn SQL Anfrage fehlerhaft
     */
    private int countByID(int id) throws SQLException {
        String exists = "SELECT COUNT(*) FROM publishers WHERE publisher_id = " + id + ";";
        ResultSet set = database.query(exists);
        return RequestManager.getInstance().count(set);
    }
}
