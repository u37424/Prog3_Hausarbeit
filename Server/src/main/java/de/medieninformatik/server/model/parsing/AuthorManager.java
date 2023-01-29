package de.medieninformatik.server.model.parsing;

import de.medieninformatik.common.Author;
import de.medieninformatik.common.DBMeta;

public class AuthorManager {
    RequestManager manager = RequestManager.getInstance();

    public DBMeta getAll() {
        return null;
    }

    public DBMeta getSelection(int start, int size, boolean orderAsc, String string) {
        return null;
    }

    public Author getItem(int id) {
        return null;
    }

    public boolean putItem(Author author) {
        return false;
    }

    public boolean postItem(Author author) {
        return false;
    }

    public boolean deleteItem(int id) {
        return false;
    }
}
