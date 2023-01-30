package de.medieninformatik.server.model.parsing;

import de.medieninformatik.common.Book;
import de.medieninformatik.common.DBMeta;
import de.medieninformatik.common.Publisher;

import java.util.LinkedList;

public class BookManager {
    RequestManager manager = RequestManager.getInstance();

    public LinkedList<Book> getAll() {
        return null;
    }

    public LinkedList<Book> getSelection(int start, int size, boolean orderAsc, String string, String category) {
        return null;
    }

    public Book getItem(String id) {
        return null;
    }

    public boolean putItem(Book book) {
        return false;
    }

    public boolean postItem(Book book) {
        return false;
    }

    public boolean deleteItem(String id) {
        return false;
    }

    public DBMeta asDBMeta(LinkedList<Book> books) {
        DBMeta meta = new DBMeta();
        meta.setResultMax(getMax());
        meta.setBooks(books);
        return meta;
    }

    public int getMax() {
        return 4;
    }
}
