package de.medieninformatik.server.model;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.ResourceBundle;

//Singleton
public class DBConnection {
    //Connection als Singleton
    private static final DBConnection instance = new DBConnection();

    private static Connection connection;

    private DBConnection() {
        //Connection aufbauen
        connection = getConnection();
        if(connection == null) return;
        //Datenbankinitialisierung bei Bedarf
        setupDatabase();
    }

    private static Connection getConnection() {
        //Verbindungsattribute
        ResourceBundle bundle = ResourceBundle.getBundle("Connection");
        String driver = bundle.getString("Driver");
        String baseUrl = bundle.getString("URL");
        String user = bundle.getString("User");
        String password = bundle.getString("Password");
        try {
            Class.forName(driver);
            return DriverManager.getConnection(baseUrl, user, password);
        } catch (SQLException e) {
            System.err.println("Could not find Database :(");
        } catch (ClassNotFoundException e) {
            System.err.println("Could not find Driver :(");
        }
        return null;
    }

    private void setupDatabase() {
        //Initialisierungsvariablen
        ResourceBundle bundle = ResourceBundle.getBundle("Connection");
        String query = bundle.getString("HasDataSQL");
        //Herausfinden, ob DB leer oder nicht
        try {
            Statement statement = connection.createStatement();
            ResultSet set = statement.executeQuery(query);
            if (set.next()) {
                int tables = set.getInt(1);
                //Wenn leer, dann befuellen
                if (tables == 0) initDatabase();
                else System.err.println("Data found.");
            }
            statement.close();
        } catch (Exception e) {
            System.err.println("Error during setup.");
        }
    }

    private void initDatabase() {
        try {
            //SQL Anweisungen laden
            FileReader reader = new FileReader(".\\src\\main\\resources\\informatik_init.sql");
            StringBuilder sb = new StringBuilder();
            int c;
            while ((c = reader.read()) != -1) {
                sb.append((char) c);
            }
            String init = sb.toString();

            //Queries hintereinander ausführen
            Statement statement = connection.createStatement();
            String[] queries = init.split(";");
            for (String query : queries) {
                statement.executeUpdate(query);
            }
            statement.close();
            System.err.println("Successfully executed Database initialisation!");
        } catch (FileNotFoundException | SQLException e) {
            System.err.println("Could not initialize Data.");
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("Could not load default SQL.");
        }
    }

    public static ResultSet query(String query) throws SQLException {
        //Beliebigen Query and DB senden und result set erhalten
        Statement statement = connection.createStatement();
        ResultSet set = statement.executeQuery(query);
        return set;
    }

    public static int update(String update) throws SQLException {
        //Beliebiges Update and DB senden und status erhalten
        Statement statement = connection.createStatement();
        int status = statement.executeUpdate(update);
        return status;
    }

    public static DBConnection getInstance() {
        return instance;
    }
}
