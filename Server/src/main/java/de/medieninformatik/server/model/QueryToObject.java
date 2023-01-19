package de.medieninformatik.server.model;

import de.medieninformatik.common.Author;
import de.medieninformatik.common.Book;
import de.medieninformatik.common.Category;
import de.medieninformatik.common.Publisher;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;

public class QueryToObject {
    private static final QueryToObject instance = new QueryToObject();
    private final DBConnection connection;

    private QueryToObject() {
        connection = DBConnection.getInstance();
    }


    public static QueryToObject getInstance() {
        return instance;
    }

    public Book getBookByIsbn(String isbn) throws SQLException {
        Book book = new Book(isbn);
        getAllBookData(isbn, book);
        getBookAuthorData(isbn, book);
        getBookCategoryData(isbn, book);
        return book;
    }

    private void getAllBookData(String isbn, Book book) throws SQLException {
        String title = "";
        int year = 0;
        int pages = 0;
        double rating = 0;
        String description = "";
        Publisher publisher = null;

        ResultSet bookSet = connection.query("SELECT b.Title, b.Release_Year, b.Pages, b.Rating, b.Description, p.Publisher_ID, p.Name, p.Main_Country " +
                "FROM books b, publishers p " +
                "WHERE ISBN = \"" + isbn + "\" AND b.Publisher_ID = p.Publisher_ID;");
        while (bookSet.next()) {
            title = bookSet.getString(1);
            year = bookSet.getInt(2);
            pages = bookSet.getInt(3);
            rating = bookSet.getDouble(4);
            description = bookSet.getString(5);

            int publisherId = bookSet.getInt(6);
            String name = bookSet.getString(7);
            String country = bookSet.getString(8);

            Publisher p = new Publisher(publisherId);
            p.setName(name);
            p.setCountry(country);
            publisher = p;
        }

        book.setTitle(title);
        book.setReleaseYear(year);
        book.setPages(pages);
        book.setRating(rating);
        book.setDescription(description);
        book.setPublisher(publisher);
    }

    private void getBookAuthorData(String isbn, Book book) throws SQLException {
        Author[] authors;

        ResultSet authorSet = connection.query("SELECT a.Author_ID, a.First_Name, a.Last_Name, a.Alias " +
                "FROM authors a, book_authors ba, books b " +
                "WHERE ba.ISBN = b.ISBN AND ba.Author_ID = a.Author_ID AND b.ISBN = \"" + isbn + "\";");

        LinkedList<Author> aRes = new LinkedList<>();
        while (authorSet.next()) {
            int authorId = authorSet.getInt(1);
            String firstName = authorSet.getString(2);
            String lastName = authorSet.getString(3);
            String alias = authorSet.getString(4);
            Author author = new Author(authorId);
            author.setFirstName(firstName);
            author.setLastName(lastName);
            author.setAlias(alias);
            aRes.add(author);
        }
        authors = aRes.toArray(new Author[0]);

        book.setAuthors(authors);
    }

    private void getBookCategoryData(String isbn, Book book) throws SQLException {
        Category[] categories;

        ResultSet categorySet = connection.query("SELECT c.Category_ID, c.Name FROM categories c, book_categories bc, books b " +
                "WHERE bc.ISBN = b.ISBN AND bc.Category_ID = c.Category_ID AND b.ISBN = \"" + isbn + "\";");

        LinkedList<Category> cRes = new LinkedList<>();
        while (categorySet.next()) {
            int categoryId = categorySet.getInt(1);
            String name = categorySet.getString(2);
            Category category = new Category(categoryId);
            category.setName(name);
            cRes.add(category);
        }
        categories = cRes.toArray(new Category[0]);

        book.setCategories(categories);
    }

    public Book getBookList(int from, int amount, String order, String filterString, String filterCategory) throws SQLException {
        Book books = new Book(null);

        String orderBy = (order == null || order.equals("")) ? "ASC" : (order.equalsIgnoreCase("asc")) ? "ASC" : "DESC";
        String titleMatch = (filterString == null || filterString.equals("")) ? "" : ("AND b.Title LIKE('%" + filterString + "%')");
        String categoryMatch = (filterCategory == null || filterCategory.equals("")) ? "" : "AND bc.ISBN = b.ISBN AND bc.Category_ID = c.Category_ID AND c.Name = \"" + filterCategory + "\"";
        String categoryFrom = (categoryMatch.equals("")) ? "" : ", categories c, book_categories bc ";

        ResultSet
                bookSet = connection.query("SELECT b.ISBN, b.Title, b.Release_Year, b.Pages, b.Rating, p.Publisher_ID, p.Name " +
                "FROM books b, publishers p" + categoryFrom +
                " WHERE b.Publisher_ID = p.Publisher_ID " + titleMatch + categoryMatch +
                " ORDER BY b.Title " + orderBy +
                " LIMIT " + from + "," + amount + ";");

        LinkedList<Book> bookList = new LinkedList<>();
        while (bookSet.next()) {
            Book book;
            String isbn = bookSet.getString(1);
            String title = bookSet.getString(2);
            int year = bookSet.getInt(3);
            int pages = bookSet.getInt(4);
            double rating = bookSet.getDouble(5);

            int publisherId = bookSet.getInt(6);
            String name = bookSet.getString(7);
            Publisher publisher = new Publisher(publisherId);
            publisher.setName(name);

            book = new Book(isbn);
            book.setTitle(title);
            book.setReleaseYear(year);
            book.setPages(pages);
            book.setRating(rating);
            book.setPublisher(publisher);
            bookList.add(book);
        }
        books.setBooks(bookList.toArray(new Book[0]));
        return books;
    }
}
