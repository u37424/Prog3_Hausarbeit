package de.medieninformatik.server.model.parsing;

import de.medieninformatik.common.Author;
import de.medieninformatik.common.DBMeta;
import de.medieninformatik.common.Publisher;

import java.util.LinkedList;

public class PublisherManager {

    public LinkedList<Publisher> getAll() {
        return null;
    }

    public LinkedList<Publisher> getSelection(int start, int size, boolean orderAsc, String string) {
        return null;
    }

    public Publisher getItem(int id) {
        return null;
    }

    public boolean putItem(Publisher author) {
        return false;
    }

    public boolean postItem(Publisher author) {
        return false;
    }

    public boolean deleteItem(int id) {
        return false;
    }


    public DBMeta asDBMeta(LinkedList<Publisher> publishers) {
        DBMeta meta = new DBMeta();
        meta.setResultMax(getMax());
        meta.setPublishers(publishers);
        return meta;
    }

    private int getMax() {
        return 0;
    }
}
