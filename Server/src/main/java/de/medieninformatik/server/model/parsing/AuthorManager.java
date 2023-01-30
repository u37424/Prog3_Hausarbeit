package de.medieninformatik.server.model.parsing;

import de.medieninformatik.common.Author;
import de.medieninformatik.common.Category;
import de.medieninformatik.common.DBMeta;

import java.util.LinkedList;

public class AuthorManager {
    RequestManager manager = RequestManager.getInstance();

    public LinkedList<Author> getAll() {
        return null;
    }

    public LinkedList<Author> getSelection(int start, int size, boolean orderAsc, String string) {
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

    public DBMeta asDBMeta(LinkedList<Author> authors) {
        DBMeta meta = new DBMeta();
        meta.setResultMax(getMax());
        meta.setAuthors(authors);
        return meta;
    }

    private int getMax() {
        return 0;
    }
}
