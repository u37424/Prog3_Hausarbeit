package de.medieninformatik.server.program;

import de.medieninformatik.server.model.rest.*;
import jakarta.ws.rs.core.Application;

import java.util.HashSet;
import java.util.Set;
/**
 * @author Luca Spirka m29987
 * @version 1.0
 * <p>
 * Programmieren 3 - Hausarbeit.
 * <p>
 * 2023-01-31
 * <p>
 * Die Klasse stellt dem REST Server alle benoetigten Resource-Klassen zur Verfuegung.
 * Diese beinhalten alle xxxResource Klassen zur Behandlung von Anfragen unter spezifischen Pfaden.
 */
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

    /**
     * Erhaelt die Resource Klassen
     * @return Set von ResourceKlassen
     */
    @Override
    public Set<Class<?>> getClasses() {
        return classes;
    }

    /**
     * Erhaelt die Resource Objekte
     * @return Set von Resource Objekten
     */
    @Override
    public Set<Object> getSingletons() {
        return singletons;
    }

}
