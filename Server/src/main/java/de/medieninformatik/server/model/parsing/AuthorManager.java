package de.medieninformatik.server.model.parsing;

import de.medieninformatik.common.Author;
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
 * Die Klasse wird zur Arbeit mit Datenbankanfragen und Ergebnisumwandlungen fuer Autoren benutzt.
 * Es werden entsprechende SQL Anfragen nach den Anforderungen erstellt und an die Datenbank weitergeleitet.
 * Es koennen Autoren aus den Ergebnissen erstellt werden, oder Autoren in Anfragen umngewandelt werden.
 * </p>
 */
public class AuthorManager {
    private final static Database database = Database.getInstance();

    /**
     * Fragt alle vorhandenen Autoren beim Server an und wandelt diese in eine Liste von Autoren um.
     *
     * @return Liste von Autor-Objekten.
     * @throws SQLException, wenn SQL Anfrage fehlerhaft
     */
    public LinkedList<Author> getAll() throws SQLException {
        String query = "SELECT * FROM authors;";
        ResultSet set = database.query(query);
        return parseAuthors(set);
    }

    /**
     * Fragt alle Autoren an, die spezifischen Vorgaben entsprechen, und wandelt sie in eine Liste von Autoren um.
     *
     * @param start    Start der Liste
     * @param size     Groesse der Liste
     * @param orderAsc Ordnung der Liste
     * @param string   Zeichenkette, die im Namen (Vor- oder Nachname) enthalten sein muss
     * @return Liste von Autoren
     * @throws SQLException, wenn SQL Anfrage fehlerhaft
     */
    public LinkedList<Author> getSelection(int start, int size, boolean orderAsc, String string) throws SQLException {
        String queryStart = "SELECT * FROM authors a ";
        if (string == null) string = "";

        String order = orderAsc ? " ASC " : " DESC ";
        String range = "LIMIT " + start + "," + size;

        //Query bauen
        String query = queryStart +
                " WHERE first_name LIKE('%" + string + "%') OR last_name LIKE('%" + string + "%') " +
                " ORDER BY alias" + order +
                range +
                ";";

        ResultSet set = database.query(query);
        return parseAuthors(set);
    }

    /**
     * Fragt einen Autor von der Datenbank an, der in ein Autor-Objekt umgewandelt wird.
     *
     * @param id Id des Autors
     * @return Angefragter Autor als Objekt
     * @throws SQLException, wenn SQL Anfrage fehlerhaft
     */
    public Author getItem(int id) throws SQLException {
        String getQuery = "SELECT * FROM authors WHERE author_id = " + id + ";";
        ResultSet set = database.query(getQuery);
        return parseAuthors(set).get(0);
    }

    /**
     * Gibt alle Autoren aus, die zu einem bestimmten Buch gehoeren.
     *
     * @param isbn ISBN des zugehoerigen Buches
     * @return Liste von Autoren
     * @throws SQLException, wenn SQL Anfrage fehlerhaft
     */
    public LinkedList<Author> getBookAuthors(String isbn) throws SQLException {
        String query = "SELECT * from authors a, books b, book_authors ba WHERE a.author_id = ba.author_id AND b.ISBN = ba.ISBN AND b.ISBN ='" + isbn + "';";
        ResultSet set = database.query(query);
        return parseAuthors(set);
    }

    /**
     * Aktualisiert die vorhandenen Daten zu einem Autor in der Datenbank mit den neuen Werten.
     * Der entsprechende Autor muss in der Datenbank vorhanden sein, damit er aktualisiert werden kann.
     *
     * @param author Autor mit aktuellen Daten
     * @return Erfolgsstatus des Updates
     * @throws SQLException, wenn SQL Update fehlerhaft
     */
    public boolean putItem(Author author) throws SQLException {
        if (author == null) return false;
        int id = author.getAuthorId();
        if (countByID(id) != 1) return false;

        //Update bauen
        String update = "UPDATE authors SET" +
                " first_name = '" + author.getFirstName() +
                "', last_name = '" + author.getLastName() +
                "', alias = '" + author.getAlias() +
                "', birthday = '" + author.getBirthday() +
                "', age = " + author.getAge() +
                " WHERE author_id = " + author.getAuthorId() + ";";
        int res = database.update(update);
        return res == 1;
    }

    /**
     * Traegt neue Autor-Kategorie-Beziehungen in die Datenbank ein.
     *
     * @param authors Autoren eines Buches
     * @param isbn    ISBN des Buches
     * @throws SQLException, wenn SQL Update fehlerhaft
     */
    public void putBookAuthors(LinkedList<Author> authors, String isbn) throws SQLException {
        LinkedList<Author> vorhanden = getBookAuthors(isbn);
        for (Author author : vorhanden) authors.removeIf(e -> e.getAuthorId() == author.getAuthorId());

        for (Author author : authors) {
            String insert = "INSERT INTO book_authors (ISBN, Author_ID) VALUES('" +
                    isbn + "', " + author.getAuthorId() + ");";
            database.update(insert);
        }
    }

    /**
     * Erstellt einen neuen Autor in der Datenbank.
     * Der uebergebene Autor darf noch nicht in der Datenbank enthalten sein.
     *
     * @param author zu erstellender Autor
     * @return Erfolgsstatus der Anweisung
     * @throws SQLException, wenn SQL Update fehlerhaft
     */
    public boolean postItem(Author author) throws SQLException {
        if (author == null) return false;
        int id = author.getAuthorId();
        if (countByID(id) != 0) return false;

        //Update bauen
        String insert = "INSERT INTO authors (author_id, first_name, last_name, alias, birthday, age)" +
                " VALUES(" + id +
                ",'" + author.getFirstName() +
                "','" + author.getLastName() +
                "','" + author.getAlias() +
                "','" + author.getBirthday() +
                "'," + author.getAge() +
                ");";
        int res = database.update(insert);
        return res == 1;
    }

    /**
     * Loescht einen bestehenden Autor aus der Datenbank, wenn sie existiert.
     *
     * @param id ID des zu loeschenden Autors
     * @return Erfolgsstatus der Anweisung
     * @throws SQLException, wenn SQL Update fehlerhaft
     */
    public boolean deleteItem(int id) throws SQLException {
        if (countByID(id) == 0) return false;
        String delete = "DELETE FROM authors WHERE author_id = " + id + ";";
        int res = database.update(delete);
        return res == 1;
    }

    /**
     * Loescht alle Autor-Kategorie-Beziehungen aus der Datenbank.
     *
     * @param id ID des Autors
     * @throws SQLException, wenn SQL Update fehlerhaft
     */
    public void deleteBookAuthors(String id) throws SQLException {
        String delete = "DELETE FROM book_authors WHERE isbn = '" + id + "';";
        database.update(delete);
    }


    /**
     * Wandelt ein ResultSet auf eine Liste von Autor-Objekten um.
     *
     * @param set umzuwandelndes ResultSet
     * @return Liste an Autoren, die aus dem ResultSet erzeugt wurde
     * @throws SQLException, wenn Umwandlung fehlerhaft
     */
    private LinkedList<Author> parseAuthors(ResultSet set) throws SQLException {
        LinkedList<Author> authors = new LinkedList<>();
        while (set.next()) {
            Author author = new Author();

            author.setAuthorId(set.getInt("author_id"));
            author.setFirstName(set.getString("first_name"));
            author.setLastName(set.getString("last_name"));
            author.setAlias(set.getString("alias"));
            author.setBirthday(set.getString("birthday"));
            author.setAge(set.getInt("age"));

            authors.add(author);
        }
        return authors;
    }

    /**
     * Ermittelt die Maximalanzahl von Autoren in der Datenbank.
     *
     * @return Maximale Anzahl an Autoren, die in der Datenbank vorhanden sind
     * @throws SQLException, wenn SQL Anfrage fehlerhaft
     */
    public int getMax() throws SQLException {
        return getMax(null);
    }

    /**
     * Ermittelt fuer eine durch Filter eingeschraenkte Abfrage die Maximalanzahl von Autoren.
     *
     * @param string Zeichenkette, die im Namen (Vor- oder Nachname) enthalten sein muss
     * @return Maximalanzahl von entsprechenden Autoren
     * @throws SQLException, wenn SQL Anfrage fehlerhaft
     */
    public int getMax(String string) throws SQLException {
        if (string == null) string = "";
        String query = "SELECT COUNT(*) FROM authors WHERE first_name LIKE('%" + string + "%') || last_name LIKE('" + string + "');";
        ResultSet set = database.query(query);
        return RequestManager.getInstance().count(set);
    }

    /**
     * Wandelt eine (partielle) Liste von Autoren in ein DBMeta Objekt um.
     * Dem DBMeta Objekt kann ebenso die Maximalanzahl der kompletten Liste uebergeben werden.
     *
     * @param authors (partielle) Liste an Autoren
     * @param max     Maximaler Wert der Ergebnisliste
     * @return umgewandeltes DBMeta Objekt
     * @throws SQLException, wenn SQL Anfrage fehlerhaft
     */
    public DBMeta asDBMeta(LinkedList<Author> authors, int max) throws SQLException {
        DBMeta meta = new DBMeta();
        meta.setResultMax(max);
        meta.setAuthors(authors);
        return meta;
    }

    /**
     * Zaehlt alle Autoren in der Datenbank, die unter einer ID zu finden sind.
     *
     * @param id ID des zu zaehlenden Autors
     * @return Anzahl von entsprechenden Autoren
     * @throws SQLException, wenn SQL Anfrage fehlerhaft
     */
    private int countByID(int id) throws SQLException {
        String exists = "SELECT COUNT(*) FROM authors WHERE author_id = " + id + ";";
        ResultSet set = database.query(exists);
        return RequestManager.getInstance().count(set);
    }
}
