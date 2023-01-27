package de.medieninformatik.server.model.parsing;

import de.medieninformatik.common.Author;
import de.medieninformatik.common.Book;
import de.medieninformatik.common.Category;
import de.medieninformatik.common.Publisher;
import de.medieninformatik.server.model.database.Database;

public class UpdateManager {
    private final Database database =  Database.getInstance();
    public boolean putBook(Book book) {
        return false;
    }
    public boolean putCategory(Category category) {
        return false;
    }

    public boolean putPublisher(Publisher publisher) {
        return false;
    }

    public boolean putAuthor(Author author) {
        return false;
    }


    public boolean postAuthor(Author author) {
        return false;
    }
    public boolean postBook(Book book) {
        return false;
    }

    public boolean postCategory(Category category) {
        return false;
    }

    public boolean postPublisher(Publisher publisher) {
        return false;
    }


    public boolean deleteAuthor(int id) {
        return false;
    }

    public boolean deleteBook(String isbn) {
        return false;
    }

    public boolean deleteCategory(int id) {
        return false;
    }

    public boolean deletePublisher(int id) {
        return false;
    }
}
