package de.medieninformatik.server.model;

import de.medieninformatik.common.Author;
import de.medieninformatik.common.Book;
import de.medieninformatik.common.Category;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ObjectToQuery {
    private static final ObjectToQuery instance = new ObjectToQuery();
    private final DBConnection connection;

    private ObjectToQuery() {
        connection = DBConnection.getInstance();
    }

    public static ObjectToQuery getInstance() {
        return instance;
    }

    public boolean putBook(Book book) throws SQLException {
        String isbn = book.getIsbn();
        ResultSet count = connection.query("SELECT count(*) FROM books WHERE ISBN = '" + isbn + "';");
        int amount = 0;
        while (count.next()) {
            amount = count.getInt(1);
        }
        if (amount != 1) return false;
        putBookCategories(isbn, book.getCategories());
        putBookAuthors(isbn, book.getAuthors());
        return updateBook(book);
    }

    private boolean updateBook(Book book) throws SQLException {
        int res = connection.update("UPDATE books SET" +
                " Title = '" + book.getTitle() + "'" +
                ", Publisher_Id = " + book.getPublisher().getPublisherId() +
                ", Release_Year = " + book.getReleaseYear() +
                ", Pages = " + book.getPages() +
                ", Rating = " + book.getRating() +
                ", Description = '" + book.getDescription() + "'" +
                " WHERE isbn = '" + book.getIsbn() + "';");
        return res == 1;
    }

    public boolean postBook(Book book) throws SQLException {
        String isbn = book.getIsbn();
        ResultSet count = connection.query("SELECT count(*) FROM books WHERE ISBN = '" + isbn + "';");
        int amount = 0;
        while (count.next()) {
            amount = count.getInt(1);
        }
        if (amount != 0) return false;
        if (!createBook(book)) return false;
        putBookCategories(isbn, book.getCategories());
        putBookAuthors(isbn, book.getAuthors());
        return true;
    }

    private boolean createBook(Book book) throws SQLException {
        int res = connection.update("INSERT INTO books VALUES('" + book.getIsbn() + "','"
                + book.getTitle() + "'," + book.getPublisher().getPublisherId() + ","
                + book.getReleaseYear() + "," + book.getPages() + ","
                + book.getRating() + ",'" + book.getDescription() + "');");
        return res == 1;
    }

    private void putBookCategories(String isbn, Category[] categories) throws SQLException {
        connection.update("DELETE FROM book_categories WHERE isbn = '" + isbn + "';");
        for (Category category : categories) {
            connection.update("INSERT INTO book_categories VALUES('" + isbn + "'," + category.getCategoryId() + ");");
        }
    }

    private void putBookAuthors(String isbn, Author[] authors) throws SQLException {
        connection.update("DELETE FROM book_authors WHERE isbn = '" + isbn + "';");
        for (Author author : authors) {
            connection.update("INSERT INTO book_authors VALUES('" + isbn + "'," + author.getAuthorId() + ");");
        }
    }

    public boolean deleteBook(String isbn) throws SQLException {
        int catRes = connection.update("DELETE FROM book_categories WHERE ISBN = '" + isbn + "';");
        int authRes = connection.update("DELETE FROM book_authors WHERE ISBN = '" + isbn + "';");
        int res = connection.update("DELETE FROM books WHERE ISBN = '" + isbn + "';");
        return res == 1;
    }
}
