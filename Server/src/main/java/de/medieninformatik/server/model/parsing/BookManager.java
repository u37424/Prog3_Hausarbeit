package de.medieninformatik.server.model.parsing;

import de.medieninformatik.common.Book;
import de.medieninformatik.common.DBMeta;

public class BookManager {
    RequestManager manager = RequestManager.getInstance();

    public DBMeta getAll() {
        return null;
    }

    public DBMeta getSelection(int start, int size, boolean orderAsc, String string, String category) {
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
}
