package de.medieninformatik.server.model;

import de.medieninformatik.common.Author;
import de.medieninformatik.common.Book;
import de.medieninformatik.common.Category;
import de.medieninformatik.common.Publisher;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MyQuery {
    private static final MyQuery instance = new MyQuery();
    private final DBConnection connection;

    private MyQuery() {
        connection = DBConnection.getInstance();
    }


    public static MyQuery getInstance() {
        return instance;
    }

    public Book getBookByIsbn(String isbn) throws SQLException {
        String title = "";
        int year = 0;
        int pages = 0;
        double rating = 0;
        String description = "";
        Author[] authors = null;
        Publisher publisher = null;
        Category[] categories = null;
        ResultSet book = connection.query("SELECT Title, Release_Year, Pages, Rating, Description FROM books WHERE ISBN = \"" + isbn + "\" LIMIT 1");
        while (book.next()) {
            title = book.getString(1);
            year = book.getInt(2);
            pages = book.getInt(3);
            rating = book.getDouble(4);
            description = book.getString(5);
        }
        return null;
    }
}
