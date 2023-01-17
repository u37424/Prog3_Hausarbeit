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

        //Allgemeine Variablen fuer Server
        ResourceBundle bundle = ResourceBundle.getBundle("Server");
        String serverAddress = bundle.getString("ServerAddress");
        String port = bundle.getString("Port");
        String path = bundle.getString("Path");

        //Server erstellen
        URI baseUri = new URI("http://" + serverAddress + ":" + port + "/" + path);
        ResourceConfig config = ResourceConfig.forApplicationClass(DBApplication.class);
        HttpServer server =
                GrizzlyHttpServerFactory.createHttpServer(baseUri, config);

        //Datenbankverbindung herstellen
        DBConnection.getInstance();

        //Server starten
        if (!server.isStarted()) server.start();
        System.out.println("http://" + serverAddress + ":"+port+"/"+path+"/");
        System.out.println("ENTER stoppt den Server.");
        System.in.read();
        server.shutdownNow();
    }
}
