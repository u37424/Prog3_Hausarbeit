package de.medieninformatik.server.model.parsing;

import de.medieninformatik.common.Author;
import de.medieninformatik.common.Category;
import de.medieninformatik.common.DBMeta;
import de.medieninformatik.server.model.database.Database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;

public class CategoryManager {
    public LinkedList<Category> getAll() throws SQLException {
        String query = "SELECT * FROM categories;";
        ResultSet set = Database.getInstance().query(query);
        return parseCategories(set);
    }

    public LinkedList<Category> getSelection(int start, int size, boolean orderAsc, String string) throws SQLException {
        String queryStart = "SELECT * FROM categories c ";
        boolean hasString = string != null && !string.isBlank();
        String filterString = hasString ? " name LIKE('%" + string + "%') " : "";
        String order = orderAsc ? " ASC " : " DESC ";
        String range = "LIMIT " + start + "," + size;

        String query = queryStart +
                (hasString ?  (" WHERE ") : "") + filterString +
                "ORDER BY name" + order +
                range +
                ";";

        ResultSet set = Database.getInstance().query(query);
        return parseCategories(set);
    }

    public Category getItem(int id) throws SQLException {
        String getQuery = "SELECT * FROM categories WHERE category_id = " + id + ";";
        ResultSet set = Database.getInstance().query(getQuery);
        return parseCategories(set).get(0);
    }

    public boolean putItem(Category category) throws SQLException {
        if (category == null) return false;
        int id = category.getCategoryId();
        if (countByID(id) != 1) return false;
        String update = "UPDATE categories SET" +
                " category_id = " + id +
                ", name = '" + category.getName() +
                "';";
        int res = Database.getInstance().update(update);
        return res == 1;
    }

    public boolean postItem(Category category) throws SQLException {
        if (category == null) return false;
        int id = category.getCategoryId();
        if (countByID(id) != 0) return false;
        String insert = "INSERT INTO categories (category_id, name)" +
                " VALUES(" + id +
                ",'" + category.getName() +
                "';";
        int res = Database.getInstance().update(insert);
        return res == 1;
    }

    public boolean deleteItem(int id) throws SQLException {
        if (countByID(id) != 0) return false;
        String delete = "DELETE FROM categories WHERE category_id = " + id + ";";
        int res = Database.getInstance().update(delete);
        return res == 1;
    }

    private LinkedList<Category> parseCategories(ResultSet set) throws SQLException {
        LinkedList<Category> categories = new LinkedList<>();
        while (set.next()) {
            Category category = new Category();
            category.setCategoryId(set.getInt("category_id"));
            category.setName(set.getString("name"));
            categories.add(category);
        }
        return categories;
    }

    public int getMax() throws SQLException {
        return getMax(null);
    }

    public int getMax(String string) throws SQLException {
        if(string == null) string = "";
        String query = "SELECT COUNT(*) FROM categories WHERE name LIKE('%" + string + "%');";
        ResultSet set = Database.getInstance().query(query);
        return count(set);
    }

    public DBMeta asDBMeta(LinkedList<Category> categories, int max) throws SQLException {
        DBMeta meta = new DBMeta();
        meta.setResultMax(max);
        meta.setCategories(categories);
        return meta;
    }

    private int countByID(int id) throws SQLException {
        String exists = "SELECT COUNT(*) FROM categories WHERE category_id = " + id + ";";
        ResultSet set = Database.getInstance().query(exists);
        return count(set);
    }

    private int count(ResultSet set) throws SQLException {
        int count = 0;
        while (set.next()) {
            count = set.getInt(1);
        }
        return count;
    }
}
