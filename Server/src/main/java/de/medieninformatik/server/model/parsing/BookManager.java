package de.medieninformatik.server.model.parsing;

import de.medieninformatik.common.*;
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
 * Die Klasse wird zur Arbeit mit Datenbankanfragen und Ergebnisumwandlungen fuer Buecher benutzt.
 * Es werden entsprechende SQL Anfragen nach den Anforderungen erstellt und an die Datenbank weitergeleitet.
 * Es koennen Buecher aus den Ergebnissen erstellt werden, oder Buecher in Anfragen umngewandelt werden.
 */
public class BookManager {
    /**
     * Fragt alle Buecher beim Server an und wandelt sie in eine Liste von Buechern um.
     * @return Liste von Buch Objekten.
     * @throws SQLException, wenn Anfrage fehlerhaft
     */
    public LinkedList<Book> getAll() throws SQLException {
        String query = "SELECT * FROM books;";
        ResultSet set = Database.getInstance().query(query);
        return parseBooks(set);
    }

    /**
     * Fragt alle Buecher an, die spezifischen Vorgaben entsprechen, und wandelt sie in eine Liste von Buechern um.
     * @param start Start der Liste
     * @param size Groesse der Liste
     * @param orderAsc Ordnung der Liste
     * @param string Zeichenkette, die im Titel enthalten sein muss
     * @param category Kategorie, die mit dem Buch verbunden sein muss
     * @return Liste von Buechern
     * @throws SQLException, wenn Anfrage fehlerhaft
     */
    public LinkedList<Book> getSelection(int start, int size, boolean orderAsc, String string, String category) throws SQLException {
        String queryStart = "SELECT * FROM books b";

        if (string == null) string = "";

        boolean hasCategory = category != null && !category.isBlank();
        String filterCategory = (hasCategory) ? (" AND b.isbn = bc.isbn AND bc.category_id = c.category_id AND c.name = '" + category + "' ") : "";

        String order = orderAsc ? " ASC " : " DESC ";
        String range = "LIMIT " + start + "," + size;

        String query = queryStart +
                (hasCategory ? ", categories c, book_categories bc " : "") +
                " WHERE title LIKE('%" + string + "%') " +
                filterCategory +
                "ORDER BY title" + order +
                range +
                ";";

        ResultSet set = Database.getInstance().query(query);
        return parseBooks(set);
    }

    /**
     * Fragt ein Buch an, das in ein Buch Objekt umgewandelt wird.
     * @param id ISBN des Buches
     * @return Angefragtes Buch Objekt
     * @throws SQLException, wenn Anfrage fehlerhaft
     */
    public Book getItem(String id) throws SQLException {
        String getQuery = "SELECT * FROM books WHERE isbn = '" + id + "';";
        ResultSet set = Database.getInstance().query(getQuery);
        return parseBooks(set).get(0);
    }

    /**
     * Wandelt ein existierendes Buch in eine SQL Update anweisung um, die die Daten dafuer in der Datenbank mit den neuen Daten aktualisiert.
     * @param book aktualisiertes Buch
     * @return Erfolgsstatus des Updates
     * @throws SQLException, wenn Update fehlerhaft
     */
    public boolean putItem(Book book) throws SQLException {
        if (book == null) return false;
        String isbn = book.getIsbn();
        if (countByISBN(isbn) != 1) return false;
        RequestManager.getInstance().getAuthorManager().deleteBookAuthors(isbn);
        RequestManager.getInstance().getCategoryManager().deleteBookCategories(isbn);
        RequestManager.getInstance().getAuthorManager().addBookAuthors(book.getAuthors(), isbn);
        RequestManager.getInstance().getCategoryManager().addBookCategories(book.getCategories(), isbn);
        String update = "UPDATE books SET " +
                " title = '" + book.getTitle() +
                "', publisher_id = " + book.getPublisher().getPublisherId() +
                ", release_year = " + book.getReleaseYear() +
                ", pages = " + book.getPages() +
                ", rating = " + book.getRating() +
                ", description = '" + book.getDescription() +
                "' WHERE ISBN = '" + isbn + "';";
        int res = Database.getInstance().update(update);
        return res == 1;
    }

    /**
     * Erstellt einen neuen Bucheintrag in der Datenbank durch ein SQL Insert.
     * @param book neues Buch
     * @return Erfolgsstatus der Anweisung
     * @throws SQLException, wenn Update fehlerhaft
     */
    public boolean postItem(Book book) throws SQLException {
        if (book == null) return false;
        String isbn = book.getIsbn();
        if (countByISBN(isbn) != 0) return false;

        String insert = "INSERT INTO books (ISBN, title, publisher_id, release_year, pages, rating, description)" +
                " VALUES('" + isbn +
                "','" + book.getTitle() +
                "'," + book.getPublisher().getPublisherId() +
                "," + book.getReleaseYear() +
                "," + book.getPages() +
                "," + book.getRating() +
                ",'" + book.getDescription() +
                "');";
        int res = Database.getInstance().update(insert);

        RequestManager.getInstance().getAuthorManager().addBookAuthors(book.getAuthors(), isbn);
        RequestManager.getInstance().getCategoryManager().addBookCategories(book.getCategories(), isbn);
        return res == 1;
    }

    /**
     * Loescht ein Buch aus der Datenbank, wenn es existiert.
     * @param id ISBN des Buches
     * @return Erfolgsstatus der Anweisung
     * @throws SQLException, wenn fehlerhaft
     */
    public boolean deleteItem(String id) throws SQLException {
        if (countByISBN(id) == 0) return false;

        RequestManager.getInstance().getCategoryManager().deleteBookCategories(id);
        RequestManager.getInstance().getAuthorManager().deleteBookAuthors(id);
        System.out.println("here");
        String delete = "DELETE FROM books WHERE isbn = '" + id + "';";
        int res = Database.getInstance().update(delete);
        return res == 1;
    }

    /**
     * Wandelt ein ResultSet auf eine Liste an Buch Objekten um.
     * @param set umzuwandelndes ResultSet
     * @return Liste an Buechern aus dem ResultSet
     * @throws SQLException, wenn fehlerhaft
     */
    private LinkedList<Book> parseBooks(ResultSet set) throws SQLException {
        LinkedList<Book> books = new LinkedList<>();
        while (set.next()) {
            Book book = new Book();
            book.setIsbn(set.getString("ISBN"));
            book.setTitle(set.getString("Title"));
            book.setReleaseYear(set.getInt("Release_Year"));
            book.setPages(set.getInt("Pages"));
            book.setRating(set.getDouble("Rating"));
            book.setDescription(set.getString("Description"));

            Publisher publisher = RequestManager.getInstance().getPublisherManager().getItem(set.getInt("Publisher_ID"));
            book.setPublisher(publisher);

            LinkedList<Author> authors = RequestManager.getInstance().getAuthorManager().getBookAuthors(book.getIsbn());
            book.setAuthors(authors);

            LinkedList<Category> categories = RequestManager.getInstance().getCategoryManager().getBookCategories(book.getIsbn());
            book.setCategories(categories);

            books.add(book);
        }
        return books;
    }

    /**
     * Ermittelt die Maximalanzahl von Buechern in der Datenbank
     * @return Maximale Anzahl an Buechern
     * @throws SQLException, wenn fehlerhaft
     */
    public int getMax() throws SQLException {
        return getMax(null, null);
    }

    /**
     *  Ermittelt die Maximalanzahl von Buechern in der Datenbank, die durch entsprechende Filter eingeschraenkt wird
     * @param string Zeichenkette, die im Titel enthalten sein muss
     * @param category Kategorie, die zum Buch gehoeren muss
     * @return Maximalanzahl von entsprechenden Buechern
     * @throws SQLException, wenn fehlerhaft
     */
    public int getMax(String string, String category) throws SQLException {
        boolean hasCategory = category != null && !category.isBlank();
        String filterCategory = (hasCategory) ? (" AND b.isbn = bc.isbn AND bc.category_id = c.category_id AND c.name = '" + category + "' ") : "";
        String query = "SELECT COUNT(*) FROM books b" +
                (hasCategory ? ", categories c, book_categories bc " : "") +
                " WHERE title LIKE('%" + string + "%') " +
                filterCategory +
                ";";
        ResultSet set = Database.getInstance().query(query);
        return count(set);
    }

    /**
     * Wandelt eine Liste von Buechern in ein DBMeta Objekt um.
     * Dem DBMeta Objekt kann ebenso eine passende Maximalanzahl uebergeben werden.
     * @param books Liste an Buechern
     * @param max Maximaler Wert der Ergebnisliste
     * @return umgewandeltes DBMeta Objekt
     * @throws SQLException, wenn fehlerhaft
     */
    public DBMeta asDBMeta(LinkedList<Book> books, int max) throws SQLException {
        DBMeta meta = new DBMeta();
        meta.setResultMax(max);
        meta.setBooks(books);
        return meta;
    }

    /**
     * Zaehlt alle Buecher, die zu einer ISBN gehoeren.
     * @param isbn ISBN des Buches
     * @return Anzahl von entsprechenden Buechern
     * @throws SQLException, wenn fehlerhaft
     */
    private int countByISBN(String isbn) throws SQLException {
        String exists = "SELECT COUNT(*) FROM books WHERE isbn= '" + isbn + "';";
        ResultSet set = Database.getInstance().query(exists);
        return count(set);
    }

    /**
     * Wandelt ein ResultSet mit nur einer Anzahl in einen Integer um
     * @param set ResultSet mit Anzahl
     * @return Anzahl die im ResultSet gespeichert wurde
     * @throws SQLException, wenn fehlerhaft
     */
    private int count(ResultSet set) throws SQLException {
        int count = 0;
        while (set.next()) {
            count = set.getInt(1);
        }
        return count;
    }
}
