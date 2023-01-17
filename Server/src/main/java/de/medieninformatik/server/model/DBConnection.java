package de.medieninformatik.server.model;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.ResourceBundle;

//Singleton
public class DBConnection {
    private static final DBConnection instance = new DBConnection();

    private static Connection connection;

    private DBConnection() {
        connection = getConnection();
        setupDatabase();
    }

    private static Connection getConnection() {
        ResourceBundle bundle = ResourceBundle.getBundle("Connection");
        String driver = bundle.getString("Driver");
        String baseUrl = bundle.getString("URL");
        String user = bundle.getString("User");
        String password = bundle.getString("Password");
        try {
            Connection connection = DriverManager.getConnection(baseUrl, user, password);
            return connection;
        } catch (SQLException e) {
            System.err.println("Could not find Database :(");
        }
        return null;
    }

    private void setupDatabase() {
        ResourceBundle bundle = ResourceBundle.getBundle("Connection");
        String query = bundle.getString("InitRequiredSQL");
        try {
            Statement statement = connection.createStatement();
            ResultSet set = statement.executeQuery(query);
            if (set.next()) {
                int tables = set.getInt(1);
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
            FileReader reader = new FileReader("./src/main/resources/informatik_init.sql");
            StringBuilder sb = new StringBuilder();
            int c;
            while ((c = reader.read()) != -1) {
                sb.append((char) c);
            }
            String init = sb.toString();
            Statement statement = connection.createStatement();
            String[] queries = init.split(";");
            for (String query : queries) {
                statement.executeUpdate(query);
            }
            System.err.println("Successfully executed Database initialisation!");
        } catch (FileNotFoundException | SQLException e) {
            System.err.println("Could not initialize Data.");
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("Could not load default SQL.");
        }
    }

    public static ResultSet query(String query) throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet set = statement.executeQuery(query);
        return set;
    }

    public static int update(String update) throws SQLException {
        Statement statement = connection.createStatement();
        int status = statement.executeUpdate(update);
        return status;
    }

    public static DBConnection getInstance() {
        return instance;
    }
}
