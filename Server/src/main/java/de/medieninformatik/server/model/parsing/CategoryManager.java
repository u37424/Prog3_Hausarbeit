package de.medieninformatik.server.model.parsing;

import de.medieninformatik.common.Category;
import de.medieninformatik.common.DBMeta;
import de.medieninformatik.server.model.database.Database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;

/**
 * @author Luca Spirka m29987
 * @version 1.0
 * <p>
 * Programmieren 3 - Hausarbeit.
 * </p>
 * 2023-01-31
 * <p>
 * Die Klasse wird zur Arbeit mit Datenbankanfragen und Ergebnisumwandlungen fuer Kategorien benutzt.
 * Es werden entsprechende SQL Anfragen nach den Anforderungen erstellt und an die Datenbank weitergeleitet.
 * Es koennen Kategorien aus den Ergebnissen erstellt werden, oder Kategorien in Anfragen umngewandelt werden.
 * </p>
 */
public class CategoryManager {
    private final static Database database = Database.getInstance();

    /**
     * Fragt alle vorhandenen Kategorien beim Server an und wandelt diese in eine Liste von Kategorien um.
     *
     * @return Liste von Kategorie-Objekten.
     * @throws SQLException, wenn SQL Anfrage fehlerhaft
     */
    public LinkedList<Category> getAll() throws SQLException {
        String query = "SELECT * FROM categories;";
        ResultSet set = database.query(query);
        return parseCategories(set);
    }

    /**
     * Fragt alle Kategorien an, die spezifischen Vorgaben entsprechen, und wandelt sie in eine Liste von Kategorien um.
     *
     * @param start    Start der Liste
     * @param size     Groesse der Liste
     * @param orderAsc Ordnung der Liste
     * @param string   Zeichenkette, die im Namen enthalten sein muss
     * @return Liste von Kategorien
     * @throws SQLException, wenn SQL Anfrage fehlerhaft
     */
    public LinkedList<Category> getSelection(int start, int size, boolean orderAsc, String string) throws SQLException {
        String queryStart = "SELECT * FROM categories c ";

        if (string == null) string = "";

        String order = orderAsc ? " ASC " : " DESC ";
        String range = "LIMIT " + start + "," + size;

        //Query bauen
        String query = queryStart +
                " WHERE name LIKE('%" + string + "%') " +
                " ORDER BY name " + order +
                range +
                ";";

        ResultSet set = database.query(query);
        return parseCategories(set);
    }

    /**
     * Fragt eine Kategorie von der Datenbank an, das in ein Kategorie-Objekt umgewandelt wird.
     *
     * @param id Id der kategorie
     * @return Angefragte Kategorie als Objekt
     * @throws SQLException, wenn SQL Anfrage fehlerhaft
     */
    public Category getItem(int id) throws SQLException {
        String getQuery = "SELECT * FROM categories WHERE category_id = " + id + ";";
        ResultSet set = database.query(getQuery);
        return parseCategories(set).get(0);
    }

    /**
     * Gibt alle Kategorien aus, die zu einem bestimmten Buch gehoeren.
     *
     * @param isbn ISBN des zugehoerigen Buches
     * @return Liste von Kategorien
     * @throws SQLException, wenn SQL Anfrage fehlerhaft
     */
    public LinkedList<Category> getBookCategories(String isbn) throws SQLException {
        String query = "SELECT * from categories c, books b, book_categories bc WHERE c.category_id = bc.category_id AND b.ISBN = bc.ISBN AND b.ISBN ='" + isbn + "';";
        ResultSet set = database.query(query);
        return parseCategories(set);
    }

    /**
     * Aktualisiert die vorhandenen Daten zu einer Kategorie in der Datenbank mit den neuen Werten.
     * Die entsprechende Kategorie muss in der Datenbank vorhanden sein, damit sie aktualisiert werden kann.
     *
     * @param category Kategorie mit aktuellen Daten
     * @return Erfolgsstatus des Updates
     * @throws SQLException, wenn SQL Update fehlerhaft
     */
    public boolean putItem(Category category) throws SQLException {
        if (category == null) return false;
        int id = category.getCategoryId();
        if (countByID(id) != 1) return false;

        //Update bauen
        String update = "UPDATE categories SET" +
                " name = '" + category.getName() +
                "' WHERE category_id = " + category.getCategoryId() + ";";
        int res = database.update(update);
        return res == 1;
    }

    /**
     * Traegt neue Buch-Kategorie-Beziehungen in die Datenbank ein.
     *
     * @param categories Kategorien eines Buches
     * @param isbn       ISBN des Buches
     * @throws SQLException, wenn SQL Update fehlerhaft
     */
    public void putBookCategories(LinkedList<Category> categories, String isbn) throws SQLException {
        LinkedList<Category> vorhanden = getBookCategories(isbn);
        for (Category category : vorhanden) categories.removeIf(e -> e.getCategoryId() == category.getCategoryId());

        for (Category category : categories) {
            String insert = "INSERT INTO book_categories (ISBN, Category_ID) VALUES('" +
                    isbn + "', " + category.getCategoryId() + ");";
            database.update(insert);
        }
    }

    /**
     * Erstellt eine neue Kategorie in der Datenbank.
     * Die uebergebene Kategorie darf noch nicht in der Datenbank enthalten sein.
     *
     * @param category zu erstellende Kategorie
     * @return Erfolgsstatus der Anweisung
     * @throws SQLException, wenn SQL Update fehlerhaft
     */
    public boolean postItem(Category category) throws SQLException {
        if (category == null) return false;
        int id = category.getCategoryId();
        if (countByID(id) != 0) return false;
        //Update bauen
        String insert = "INSERT INTO categories (category_id, name)" +
                " VALUES(" + id +
                ",'" + category.getName() +
                "');";
        int res = database.update(insert);
        return res == 1;
    }

    /**
     * Loescht eine bestehende Kategorie aus der Datenbank, wenn sie existiert.
     *
     * @param id ID der zu loeschenden Kategorie
     * @return Erfolgsstatus der Anweisung
     * @throws SQLException, wenn SQL Update fehlerhaft
     */
    public boolean deleteItem(int id) throws SQLException {
        if (countByID(id) == 0) return false;
        String delete = "DELETE FROM categories WHERE category_id = " + id + ";";
        int res = database.update(delete);
        return res == 1;
    }

    /**
     * Loescht alle Buch-Kategorie-Beziehungen aus der Datenbank.
     *
     * @param id ID der Kategorie
     * @throws SQLException, wenn SQL Update fehlerhaft
     */
    public void deleteBookCategories(String id) throws SQLException {
        String delete = "DELETE FROM book_categories WHERE isbn = '" + id + "';";
        database.update(delete);
    }

    /**
     * Wandelt ein ResultSet auf eine Liste von Kategorie-Objekten um.
     *
     * @param set umzuwandelndes ResultSet
     * @return Liste an Kategorien, die aus dem ResultSet erzeugt wurde
     * @throws SQLException, wenn Umwandlung fehlerhaft
     */
    private LinkedList<Category> parseCategories(ResultSet set) throws SQLException {
        LinkedList<Category> categories = new LinkedList<>();
        while (set.next()) {
            Category category = new Category();

            category.setCategoryId(set.getInt("category_id"));
            category.setName(set.getString("name"));

            categories.add(category);
        }
        return categories;
    }

    /**
     * Ermittelt die Maximalanzahl von Kategorien in der Datenbank.
     *
     * @return Maximale Anzahl an Kategorien, die in der Datenbank vorhanden sind
     * @throws SQLException, wenn SQL Anfrage fehlerhaft
     */
    public int getMax() throws SQLException {
        return getMax(null);
    }

    /**
     * Ermittelt fuer eine durch Filter eingeschraenkte Abfrage die Maximalanzahl von Kategorien.
     *
     * @param string Zeichenkette, die im Namen enthalten sein muss
     * @return Maximalanzahl von entsprechenden Kategorien
     * @throws SQLException, wenn SQL Anfrage fehlerhaft
     */
    public int getMax(String string) throws SQLException {

        if (string == null) string = "";
        String query = "SELECT COUNT(*) FROM categories WHERE name LIKE('%" + string + "%');";
        ResultSet set = database.query(query);
        return RequestManager.getInstance().count(set);
    }


    /**
     * Wandelt eine (partielle) Liste von Kategorien in ein DBMeta Objekt um.
     * Dem DBMeta Objekt kann ebenso die Maximalanzahl der kompletten Liste uebergeben werden.
     *
     * @param categories (partielle) Liste an Kategorien
     * @param max        Maximaler Wert der Ergebnisliste
     * @return umgewandeltes DBMeta Objekt
     * @throws SQLException, wenn SQL Anfrage fehlerhaft
     */
    public DBMeta asDBMeta(LinkedList<Category> categories, int max) throws SQLException {
        DBMeta meta = new DBMeta();
        meta.setResultMax(max);
        meta.setCategories(categories);
        return meta;
    }

    /**
     * Zaehlt alle Kategorien in der Datenbank, die unter einer ID zu finden sind.
     *
     * @param id ID der zu zaehlenden Kategorien
     * @return Anzahl von entsprechenden Kategorien
     * @throws SQLException, wenn SQL Anfrage fehlerhaft
     */
    private int countByID(int id) throws SQLException {
        String exists = "SELECT COUNT(*) FROM categories WHERE category_id = " + id + ";";
        ResultSet set = database.query(exists);
        return RequestManager.getInstance().count(set);
    }
}
