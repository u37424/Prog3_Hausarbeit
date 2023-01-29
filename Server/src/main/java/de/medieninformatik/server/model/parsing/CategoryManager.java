package de.medieninformatik.server.model.parsing;

import de.medieninformatik.common.Category;
import de.medieninformatik.common.DBMeta;

import java.util.LinkedList;

public class CategoryManager {
    public DBMeta getAll() {
        LinkedList<Category> categories = new LinkedList<>();
        return asDBMeta(categories);
    }

    public DBMeta getSelection(int start, int size, boolean orderAsc, String string) {
        return null;
    }

    public Category getItem(int id) {
        return null;
    }

    public boolean putItem(Category author) {
        return false;
    }

    public boolean postItem(Category author) {
        return false;
    }

    public boolean deleteItem(int id) {
        return false;
    }

    private DBMeta asDBMeta(LinkedList<Category> categories) {
        DBMeta meta = new DBMeta();
        meta.setResultMax(getMax());
        meta.setCategories(categories);
        return meta;
    }

    private int getMax() {
        return 0;
    }
}
