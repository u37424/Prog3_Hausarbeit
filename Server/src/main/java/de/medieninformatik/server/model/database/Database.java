package de.medieninformatik.server.model.database;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.ResourceBundle;
/**
 * @author Luca Spirka m29987
 * @version 1.0
 * <p>
 * Programmieren 3 - Hausarbeit.
 * <p>
 * 2023-01-31
 * <p>
 * Die Klasse stellt eine Verbindung zu einer MySQL Datenbank mithilfe eines MySQL Treibers her.
 * Wenn die Datenbank noch nicht erstellt wurde, werden die Befehle aus der database_startup.sql ausgefuehrt.
 */
public class Database {
    private static final Database instance = new Database();
    private static Connection connection;

    private String driver;
    private String user, password;
    private String dbName;
    private String baseURL;
    private int tableAmount;

    private Database() {
        loadResources();
        if (connect()) init();
    }

    /**
     * Laedt alle Parameter zur Verbindung mit der Datenbank (Name, user, passwort usw.)
     */
    private void loadResources() {
        ResourceBundle bundle = ResourceBundle.getBundle("Database_Connection");
        this.driver = bundle.getString("Driver");
        String url = bundle.getString("URL");
        this.user = bundle.getString("User");
        this.password = bundle.getString("Password");
        this.dbName = bundle.getString("Database.Name");
        this.tableAmount = Integer.parseInt(bundle.getString("Database.Tables"));

        this.baseURL = url + "/" + dbName;
    }

    /**
     * Versucht eine Verbindung zur Datenbank mithilfe eines MySQL Treibers aufzubauen.
     * @return Erfolgsstatus der verbindung
     */
    private boolean connect() {
        try {
            Class.forName(driver);
            connection = DriverManager.getConnection(baseURL, user, password);
            return true;
        } catch (SQLException e) {
            printSQLErrors(e);
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL Driver not found.");
        }
        return false;
    }

    /**
     * Findet heraus, ob die Datenbank korrekt erstellt wurde, und fuehrt bei bedarf einen initialen reset aus.
     * Ansonsten wird angenommen, das eine Verbindung zu einer Datenbank besteht, die alle Tabellen besitzt.
     */
    private void init() {
        try {
            ResultSet initQuery = query("SELECT COUNT(*) AS amount FROM information_schema.tables WHERE table_schema = '" + dbName + "';");
            int tables = 0;
            while (initQuery.next()) {
                tables = initQuery.getInt("amount");
            }
            if (tables != tableAmount)
                if (!resetDatabase()) {
                    System.err.println("Setup falsely executed.");
                    System.exit(-1);
                }
            System.err.println("Connected to Database.");
        } catch (SQLException e) {
            printSQLErrors(e);
        }
    }

    /**
     * Setzt die Datenbank auf Werkseinstellungen zurueck, durch das ausfuehren der database_startup.sql.
     * @return Erfolgsstatus des Zuruecksetzens.
     */
    public boolean resetDatabase() {
        try {
            // read the SQL file
            StringBuilder sql = new StringBuilder();
            String s = File.separator;
            BufferedReader br = new BufferedReader(new FileReader("." + s + "src" + s + "main" + s + "resources" + s + "database_startup.sql"));
            String line;
            while ((line = br.readLine()) != null) {
                sql.append(line);
            }

            //execute as individual Queries
            String[] statements = sql.toString().split(";");
            for (String statement : statements) {
                if (statement.isBlank()) continue;
                update(statement);
            }
            System.err.println("Initialisation completed!");
            return true;
        } catch (SQLException e) {
            printSQLErrors(e);
        } catch (IOException e) {
            System.err.println("Couldn't read init File.");
        }
        return false;
    }

    /**
     * Fuehrt eingehende SQL Anfragen aus und liefert das entsprechende resultSet der Datenbank.
     * @param query SQL Anfrage.
     * @return ResultSet mit Daten der Datenbank.
     * @throws SQLException, wenn SQL Anfrage fehlerhaft
     */
    public synchronized ResultSet query(String query) throws SQLException {
        Statement statement = connection.createStatement();
        return statement.executeQuery(query);
    }

    /**
     * Fuehrt eingehende SQL Updates aus und liefert den entsprechenden Erfolgsstatus der Datenbank.
     * @param update SQL Update.
     * @return Erfolgsstatus des Updates
     * @throws SQLException, wenn SQL Update fehlerhaft
     */
    public synchronized int update(String update) throws SQLException {
        Statement statement = connection.createStatement();
        return statement.executeUpdate(update);
    }

    /**
     * Kann benutzt werden, um einheiltich alle SQL Fehler auszugeben.
     * @param e SQL Fehler
     */
    public void printSQLErrors(SQLException e) {
        while (e != null) {
            System.err.printf("""
                            Message: %s
                            SQLState: %s
                            ErrorCode : %d
                                                        
                             """.stripIndent(),
                    e.getMessage(), e.getSQLState(), e.getErrorCode());
            e = e.getNextException();
        }
    }

    public void shutdown(){
        try {
            connection.close();
        } catch (SQLException e) {
            System.err.println("Error in Database disconnect.");
        }
    }

    /**
     * Gibt Instanz der Datenbank zurueck
     * @return Singleton instanz
     */
    public static Database getInstance() {
        return instance;
    }
}
