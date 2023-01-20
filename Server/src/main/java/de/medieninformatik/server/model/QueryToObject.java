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

    public Book getAllBookData(String isbn) throws SQLException {
        LinkedList<Book> books = new LinkedList<>();
        ResultSet bookSet = connection.query("SELECT b.ISBN, b.Title, b.Release_Year, b.Pages, b.Rating, b.Description" +
                " FROM books b" +
                " WHERE ISBN = \"" + isbn + "\";");

        convertBooksForList(books, bookSet);

        return books.get(0);
    }

    public Book getBookList(int from, int amount, String order, String filterString, String filterCategory) throws SQLException {
        Book books = new Book("");

        String orderBy = (order == null || order.equals("")) ? "ASC" : (order.equalsIgnoreCase("asc")) ? "ASC" : "DESC";
        String titleMatch = (filterString == null || filterString.equals("")) ? "" : ("AND b.Title LIKE('%" + filterString + "%')");
        String categoryMatch = (filterCategory == null || filterCategory.equals("")) ? "" : "AND bc.ISBN = b.ISBN AND bc.Category_ID = c.Category_ID AND c.Name = \"" + filterCategory + "\"";
        String categoryFrom = (categoryMatch.equals("")) ? "" : ", categories c, book_categories bc ";

        LinkedList<Book> bookList = new LinkedList<>();

        ResultSet bookSet = connection.query("SELECT b.ISBN, b.Title, b.Release_Year, b.Pages, b.Rating, b.Description" +
                " FROM books b, publishers p " + categoryFrom +
                " WHERE b.Publisher_ID = p.Publisher_ID " + titleMatch + categoryMatch +
                " ORDER BY b.Title " + orderBy +
                " LIMIT " + from + "," + amount + ";");
        convertBooksForList(bookList, bookSet);
        books.setBooks(bookList.toArray(new Book[0]));
        return books;
    }

    public Category getCategoryList() throws SQLException {
        Category categories = new Category(0);
        LinkedList<Category> categoryList = new LinkedList<>();
        ResultSet categorySet = connection.query("SELECT Category_ID, Name FROM categories;");
        convertCategoriesForList(categoryList, categorySet);
        categories.setCategories(categoryList.toArray(new Category[0]));
        return categories;
    }

    public Publisher getPublisherList() throws SQLException {
        Publisher publishers = new Publisher(0);
        LinkedList<Publisher> publisherList = new LinkedList<>();
        ResultSet publisherSet = connection.query("SELECT Publisher_ID, Name, Year_of_Foundation, Main_Country FROM publishers;");
        convertPublishersForList(publisherList, publisherSet);
        publishers.setPublishers(publisherList.toArray(new Publisher[0]));
        return publishers;
    }

    private void convertBooksForList(LinkedList<Book> list, ResultSet resultSet) throws SQLException {
        while (resultSet.next()) {
            Book book;
            String isbn = resultSet.getString(1);
            String title = resultSet.getString(2);
            int year = resultSet.getInt(3);
            int pages = resultSet.getInt(4);
            double rating = resultSet.getDouble(5);
            String description = resultSet.getString(6);

            LinkedList<Publisher> publishers = new LinkedList<>();

            ResultSet publisherSet = connection.query("SELECT p.Publisher_ID, p.Name, p.Year_Of_Foundation, p.Main_Country" +
                    " FROM publishers p, books b" +
                    " WHERE b.Publisher_ID = p.Publisher_ID AND b.ISBN = '" + isbn + "';");

            convertPublishersForList(publishers, publisherSet);

            Publisher publisher = publishers.get(0);

            LinkedList<Author> authors = new LinkedList<>();

            ResultSet authorSet = connection.query("SELECT a.Author_ID, a.First_Name, a.Last_Name, a.Alias, a.Birthday, a.Age" +
                    " FROM authors a, book_authors ba" +
                    " WHERE ba.ISBN = \"" + isbn + "\" AND ba.Author_ID = a.Author_ID;");
            convertAuthorForList(authors, authorSet);

            LinkedList<Category> categories = new LinkedList<>();

            ResultSet categorySet = connection.query("SELECT c.Category_ID, c.Name" +
                    " FROM categories c, books b, book_categories bc" +
                    " WHERE bc.ISBN = b.ISBN AND bc.Category_Id = c.Category_Id AND b.ISBN = '"+isbn+"';");
            convertCategoriesForList(categories, categorySet);

            book = new Book(isbn);
            book.setTitle(title);
            book.setPages(pages);
            book.setReleaseYear(year);
            book.setRating(rating);
            book.setPublisher(publisher);
            book.setDescription(description);
            book.setAuthors(authors.toArray(new Author[0]));
            book.setCategories(categories.toArray(new Category[0]));
            list.add(book);
        }
    }

    private void convertPublishersForList(LinkedList<Publisher> list, ResultSet resultSet) throws SQLException {
        while (resultSet.next()) {
            int publisherId = resultSet.getInt(1);
            String name = resultSet.getString(2);
            int foundation = resultSet.getInt(3);
            String country = resultSet.getString(4);
            Publisher publisher = new Publisher(publisherId);
            publisher.setName(name);
            publisher.setFoundation(foundation);
            publisher.setCountry(country);
            list.add(publisher);
        }
    }

    private void convertAuthorForList(LinkedList<Author> list, ResultSet resultSet) throws SQLException {
        while (resultSet.next()) {
            int authorId = resultSet.getInt(1);
            String firstName = resultSet.getString(2);
            String lastName = resultSet.getString(3);
            String alias = resultSet.getString(4);
            String birthday = resultSet.getString(5);
            int age = resultSet.getInt(6);
            Author author = new Author(authorId);
            author.setFirstName(firstName);
            author.setLastName(lastName);
            author.setAlias(alias);
            author.setBirthday(birthday);
            author.setAge(age);
            list.add(author);
        }
    }

    private void convertCategoriesForList(LinkedList<Category> list, ResultSet resultSet) throws SQLException {
        while (resultSet.next()) {
            int categoryId = resultSet.getInt(1);
            String name = resultSet.getString(2);
            Category category = new Category(categoryId);
            category.setName(name);
            list.add(category);
        }
    }
}
