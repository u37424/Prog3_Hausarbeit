package de.medieninformatik.server.program;

import de.medieninformatik.server.model.rest.*;
import jakarta.ws.rs.core.Application;

import java.util.HashSet;
import java.util.Set;

public class DBApplication extends Application {
    private final Set<Object> singletons = new HashSet<>();
    private final Set<Class<?>> classes = new HashSet<>();

    public DBApplication() {
        singletons.add(new UserResource());
        singletons.add(new BookResource());
        singletons.add(new AuthorResource());
        singletons.add(new CategoryResource());
        singletons.add(new PublisherResource());
        classes.add(UserResource.class);
        classes.add(BookResource.class);
        classes.add(AuthorResource.class);
        classes.add(CategoryResource.class);
        classes.add(PublisherResource.class);
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
