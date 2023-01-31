package de.medieninformatik.server.program;

import de.medieninformatik.server.model.database.Database;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import java.io.IOException;
import java.net.InetAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {
    public static void main(String[] args) throws URISyntaxException, IOException {
        ResourceBundle bundle = ResourceBundle.getBundle("ServerResources");
        String serverAddress = bundle.getString("Host.Address");
        String port = bundle.getString("Host.Port");
        String path = bundle.getString("Base.Path");

        URI baseUri = new URI("http://" + serverAddress + ":" + port + "/" + path);

        Logger.getLogger("org.glassfish").setLevel(Level.SEVERE);

        ResourceConfig config = ResourceConfig.forApplicationClass(DBApplication.class);
        HttpServer server = GrizzlyHttpServerFactory.createHttpServer(baseUri, config);
        Database.getInstance();

        if (!server.isStarted()) server.start();
        System.out.println("ENTER stoppt den Server.");
        System.in.read();
        server.shutdownNow();
    }
}
