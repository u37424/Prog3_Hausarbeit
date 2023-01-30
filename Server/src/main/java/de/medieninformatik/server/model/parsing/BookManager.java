package de.medieninformatik.server.model.parsing;

import de.medieninformatik.common.Book;
import de.medieninformatik.common.DBMeta;
import de.medieninformatik.common.Publisher;
import de.medieninformatik.server.model.database.Database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;

public class BookManager {
    public LinkedList<Book> getAll() throws SQLException {
        String query = "SELECT * FROM books;";
        ResultSet set = Database.getInstance().query(query);
        return parseBooks(set);
    }

    public LinkedList<Book> getSelection(int start, int size, boolean orderAsc, String string, String category) throws SQLException {
        String queryStart = "SELECT * FROM books b ";
        boolean hasString = string != null && !string.isBlank();
        boolean hasCategory = category != null && !string.isBlank();
        String filterString = (hasString) ? " title LIKE('%" + string + "%') " : "";
        String filterCategory = (hasCategory) ? (" b.isbn = bc.isbn AND bc.category_id = c.category_id AND c.name = '" + category + "' ") : "";
        String order = orderAsc ? " ASC " : " DESC ";
        String range = "LIMIT " + start + "," + size;

        String query = queryStart +
                (hasCategory ? " categories c, book_categories bc " : "") +
                (hasString || hasCategory ? " WHERE " : "") + filterString +
                filterCategory +
                "ORDER BY title" + order +
                range +
                ";";

        ResultSet set = Database.getInstance().query(query);
        return parseBooks(set);
    }

    public Book getItem(String id) throws SQLException {
        String getQuery = "SELECT * FROM books WHERE isbn = '" + id + "';";
        ResultSet set = Database.getInstance().query(getQuery);
        return parseBooks(set).get(0);
    }

    public boolean putItem(Book book) throws SQLException {
        if (book == null) return false;
        String isbn = book.getIsbn();
        if (countByISBN(isbn) != 1) return false;

        //INSERT BOOK_CATEGORIES
        //if true
        //INSERT BOOK_AUTHORS
        //if true

        String update = "UPDATE books SET" +
                " isbn = '" + isbn +
                "', title = '" + book.getTitle() +
                "', publisher_id = " + book.getPublisher().getPublisherId() +
                ", release_year = " + book.getReleaseYear() +
                ", pages = " + book.getPages() +
                ", rating = " + book.getRating() +
                ", description = '" + book.getDescription() +
                "';";
        int res = Database.getInstance().update(update);
        return res == 1;
    }

    public boolean postItem(Book book) throws SQLException {
        if (book == null) return false;
        String isbn = book.getIsbn();
        if (countByISBN(isbn) != 0) return false;
        String insert = "INSERT INTO books (ISBN, title, publisher_id, release_year, pages, rating, description)" +
                " VALUES('" + isbn + "'" +
                ",'" + book.getTitle() +
                "'," + book.getPublisher().getPublisherId() +
                "," + book.getReleaseYear() +
                "," + book.getPages() +
                "," + book.getRating() +
                ",'" + book.getDescription() +
                "';";
        int res = Database.getInstance().update(insert);
        return res == 1;
    }

    public boolean deleteItem(String id) throws SQLException {
        if (countByISBN(id) != 0) return false;
        String delete = "DELETE FROM books WHERE isbn = '" + id + "';";
        int res = Database.getInstance().update(delete);
        return res == 1;
    }

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
            books.add(book);
        }
        return books;
    }

    public DBMeta asDBMeta(LinkedList<Book> books) throws SQLException {
        DBMeta meta = new DBMeta();
        meta.setResultMax(getMax());
        meta.setBooks(books);
        return meta;
    }

    public int getMax() throws SQLException {
        String query = "SELECT COUNT(*) FROM books;";
        ResultSet set = Database.getInstance().query(query);
        return count(set);
    }

    private int countByISBN(String isbn) throws SQLException {
        String exists = "SELECT COUNT(*) FROM books WHERE isbn= '" + isbn + "';";
        ResultSet set = Database.getInstance().query(exists);
        return count(set);
    }

    private int count(ResultSet set) throws SQLException {
        int count = 0;
        while (set.next()) {

        }
        return count;
    }
}
