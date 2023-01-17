package de.medieninformatik.server.program;

import de.medieninformatik.server.model.DBConnection;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {
    public static void main(String[] args) throws URISyntaxException, IOException {
        Logger.getLogger("org.glassfish").setLevel(Level.SEVERE);

        ResourceBundle bundle = ResourceBundle.getBundle("Server");
        String serverAddress = bundle.getString("ServerAddress");

        URI baseUri = new URI("http://" + serverAddress + ":8080/informatik");
        ResourceConfig config = ResourceConfig.forApplicationClass(DBApplication.class);
        HttpServer server =
                GrizzlyHttpServerFactory.createHttpServer(baseUri, config);

        DBConnection.getInstance();
        if (!server.isStarted()) server.start();
        System.out.println("http://" + serverAddress + ":8080/informatik/");
        System.out.println("ENTER stoppt den Server.");
        System.in.read();
        server.shutdownNow();
    }
}
