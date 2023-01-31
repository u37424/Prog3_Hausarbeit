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
 * <p>
 * 2023-01-31
 * <p>
 * Die Klasse wird zur Arbeit mit Datenbankanfragen und Ergebnisumwandlungen fuer Autoren benutzt.
 * Es werden entsprechende SQL Anfragen nach den Anforderungen erstellt und an die Datenbank weitergeleitet.
 * Es koennen Autoren aus den Ergebnissen erstellt werden, oder Autoren in Anfragen umngewandelt werden.
 */
public class AuthorManager {

    public LinkedList<Author> getAll() throws SQLException {
        String query = "SELECT * FROM authors;";
        ResultSet set = Database.getInstance().query(query);
        return parseAuthors(set);
    }

    public LinkedList<Author> getSelection(int start, int size, boolean orderAsc, String string) throws SQLException {
        String queryStart = "SELECT * FROM authors a ";
        boolean hasString = string != null && !string.isBlank();
        String filterString = hasString ? (" first_name LIKE('%" + string + "%') OR last_name LIKE('%" + string + "%') ") : "";
        String order = orderAsc ? " ASC " : " DESC ";
        String range = "LIMIT " + start + "," + size;

        String query = queryStart +
                (hasString ? (" WHERE ") : "") + filterString +
                "ORDER BY alias" + order +
                range +
                ";";

        ResultSet set = Database.getInstance().query(query);
        return parseAuthors(set);
    }

    public LinkedList<Author> getBookAuthors(String isbn) throws SQLException {
        String query = "SELECT * from authors a, books b, book_authors ba WHERE a.author_id = ba.author_id AND b.ISBN = ba.ISBN AND b.ISBN ='" + isbn + "';";
        ResultSet set = Database.getInstance().query(query);
        return parseAuthors(set);
    }

    public Author getItem(int id) throws SQLException {
        String getQuery = "SELECT * FROM authors WHERE author_id = " + id + ";";
        ResultSet set = Database.getInstance().query(getQuery);
        return parseAuthors(set).get(0);
    }

    public boolean putItem(Author author) throws SQLException {
        if (author == null) return false;
        int id = author.getAuthorId();
        if (countByID(id) != 1) return false;
        String update = "UPDATE authors SET" +
                " first_name = '" + author.getFirstName() +
                "', last_name = '" + author.getLastName() +
                "', alias = '" + author.getAlias() +
                "', birthday = '" + author.getBirthday() +
                "', age = " + author.getAge() +
                " WHERE author_id = " + author.getAuthorId() + ";";
        int res = Database.getInstance().update(update);
        return res == 1;
    }

    public void addBookAuthors(LinkedList<Author> authors, String isbn) throws SQLException {
        for (Author author : authors) {
            String insert = "INSERT INTO book_authors (ISBN, Author_ID) VALUES('"+
                    isbn + "', " + author.getAuthorId() + ");";
            Database.getInstance().update(insert);
        }
    }

    public boolean postItem(Author author) throws SQLException {
        if (author == null) return false;
        int id = author.getAuthorId();
        if (countByID(id) != 0) return false;
        String insert = "INSERT INTO authors (author_id, first_name, last_name, alias, birthday, age)" +
                " VALUES(" + id +
                ",'" + author.getFirstName() +
                "','" + author.getLastName() +
                "','" + author.getAlias() +
                "','" + author.getBirthday() +
                "'," + author.getAge() +
                ");";
        int res = Database.getInstance().update(insert);
        return res == 1;
    }

    public boolean deleteItem(int id) throws SQLException {
        if (countByID(id) == 0) return false;
        String delete = "DELETE FROM authors WHERE author_id = " + id + ";";
        int res = Database.getInstance().update(delete);
        return res == 1;
    }

    public void deleteBookAuthors(String id) throws SQLException {
        String delete = "DELETE FROM book_authors WHERE isbn = '" + id + "';";
        int res = Database.getInstance().update(delete);
    }

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

    public int getMax() throws SQLException {
        return getMax(null);
    }

    public int getMax(String string) throws SQLException {
        if (string == null) string = "";
        String query = "SELECT COUNT(*) FROM authors WHERE first_name LIKE('%" + string + "%') || last_name LIKE('" + string + "');";
        ResultSet set = Database.getInstance().query(query);
        return count(set);
    }

    public DBMeta asDBMeta(LinkedList<Author> authors, int max) throws SQLException {
        DBMeta meta = new DBMeta();
        meta.setResultMax(max);
        meta.setAuthors(authors);
        return meta;
    }

    private int countByID(int id) throws SQLException {
        String exists = "SELECT COUNT(*) FROM authors WHERE author_id = " + id + ";";
        ResultSet set = Database.getInstance().query(exists);
        return count(set);
    }

    private int count(ResultSet set) throws SQLException {
        int count = 0;
        while (set.next()) {
            count = set.getInt(1);
        }
        return count;
    }
}
