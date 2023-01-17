package de.medieninformatik.server.program;

import de.medieninformatik.server.model.DBRestData;
import jakarta.ws.rs.core.Application;

import java.util.HashSet;
import java.util.Set;

public class DBApplication extends Application {
    private Set<Object> singletons = new HashSet<>();
    private Set<Class<?>> classes = new HashSet<>();

    public DBApplication() {
        singletons.add(new DBRestData());
        classes.add(DBRestData.class);
    }

    @Override
    public Set<Class<?>> getClasses() {
        return classes;
    }

    @Override
    public Set<Object> getSingletons() {
        return singletons;
    }

}
