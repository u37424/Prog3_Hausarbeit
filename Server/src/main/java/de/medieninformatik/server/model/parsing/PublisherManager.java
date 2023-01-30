package de.medieninformatik.server.model.parsing;

import de.medieninformatik.common.Author;
import de.medieninformatik.common.DBMeta;
import de.medieninformatik.common.Publisher;
import de.medieninformatik.server.model.database.Database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;

public class PublisherManager {

    public LinkedList<Publisher> getAll() throws SQLException {
        String query = "SELECT * FROM publishers;";
        ResultSet set = Database.getInstance().query(query);
        return parsePublishers(set);
    }

    public LinkedList<Publisher> getSelection(int start, int size, boolean orderAsc, String string) throws SQLException {
        String queryStart = "SELECT * FROM publishers ";
        boolean hasString = string != null && !string.isBlank();
        String filterString = hasString ? " name LIKE('%" + string + "%') " : "";
        String range = "LIMIT " + start + "," + size;
        String order = orderAsc ? " ASC " : " DESC ";

        String query = queryStart +
                (hasString ?  (" WHERE ") : "") + filterString +
                "ORDER BY name" + order +
                range +
                ";";

        ResultSet set = Database.getInstance().query(query);
        return parsePublishers(set);
    }

    public Publisher getItem(int id) throws SQLException {
        String getQuery = "SELECT * FROM publishers WHERE publisher_id = " + id + ";";
        ResultSet set = Database.getInstance().query(getQuery);
        return parsePublishers(set).get(0);
    }

    public boolean putItem(Publisher publisher) throws SQLException {
        if (publisher == null) return false;
        int id = publisher.getPublisherId();
        if (countByID(id) != 1) return false;
        String update = "UPDATE publishers SET" +
                " publisher_id = " + id +
                ", name = '" + publisher.getName() +
                "', main_country = '" + publisher.getCountry() +
                "', year_of_foundation = " + publisher.getFoundation() +
                ";";
        int res = Database.getInstance().update(update);
        return res == 1;
    }

    public boolean postItem(Publisher publisher) throws SQLException {
        if (publisher == null) return false;
        int id = publisher.getPublisherId();
        if (countByID(id) != 0) return false;
        String insert = "INSERT INTO publishers (publisher_id, name, main_country, year_of_foundation)" +
                " VALUES(" + id +
                ",'" + publisher.getName() +
                "','" + publisher.getCountry() +
                "'," + publisher.getFoundation() +
                ");";
        int res = Database.getInstance().update(insert);
        return res == 1;
    }

    public boolean deleteItem(int id) throws SQLException {
        if (countByID(id) != 0) return false;
        String delete = "DELETE FROM publishers WHERE publisher_id = " + id + ";";
        int res = Database.getInstance().update(delete);
        return res == 1;
    }

    private LinkedList<Publisher> parsePublishers(ResultSet set) throws SQLException {
        LinkedList<Publisher> publishers = new LinkedList<>();
        while (set.next()) {
            Publisher publisher = new Publisher();
            publisher.setPublisherId(set.getInt("publisher_id"));
            publisher.setName(set.getString("name"));
            publisher.setCountry(set.getString("main_country"));
            publisher.setFoundation(set.getInt("year_of_foundation"));
            publishers.add(publisher);
        }
        return publishers;
    }

    public int getMax() throws SQLException {
        return getMax(null);
    }

    public int getMax(String string) throws SQLException {
        if(string == null) string = "";
        String query = "SELECT COUNT(*) FROM publishers WHERE name LIKE('%" + string + "%');";
        ResultSet set = Database.getInstance().query(query);
        return count(set);
    }

    public DBMeta asDBMeta(LinkedList<Publisher> publishers, int max) throws SQLException {
        DBMeta meta = new DBMeta();
        meta.setResultMax(max);
        meta.setPublishers(publishers);
        return meta;
    }

    private int countByID(int id) throws SQLException {
        String exists = "SELECT COUNT(*) FROM publishers WHERE publisher_id = " + id + ";";
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
