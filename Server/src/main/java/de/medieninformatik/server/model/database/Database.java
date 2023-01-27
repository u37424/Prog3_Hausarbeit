package de.medieninformatik.server.model.database;

import de.medieninformatik.server.model.parsing.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.ResourceBundle;

public class Database {

    private static final Database instance = new Database();
    private static final RequestManager manager = new RequestManager();

    private static Connection connection;
    private String driver;
    private String url;
    private String user;
    private String password;
    private String dbName;
    private String baseURL;
    private int tableAmount;

    private Database() {
        loadResources();
        connect();
        init();
    }

    private void loadResources() {
        ResourceBundle bundle = ResourceBundle.getBundle("Database_Connection");
        this.driver = bundle.getString("Driver");
        this.url = bundle.getString("URL");
        this.user = bundle.getString("User");
        this.password = bundle.getString("Password");
        this.dbName = bundle.getString("Database.Name");
        this.tableAmount = Integer.parseInt(bundle.getString("Database.Tables"));

        this.baseURL = url + "/" + dbName;
    }

    private void connect() {
        try {
            Class.forName(driver);
            connection = DriverManager.getConnection(baseURL, user, password);
        } catch (SQLException e) {
            printSQLErrors(e);
            System.exit(-1);
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL Driver not found.");
            System.exit(-1);
        }
    }

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

            // split the SQL file into individual statements
            String[] statements = sql.toString().split(";");

            // execute each statement
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

    public synchronized ResultSet query(String query) throws SQLException {
        Statement statement = connection.createStatement();
        return statement.executeQuery(query);
    }

    public synchronized int update(String update) throws SQLException {
        Statement statement = connection.createStatement();
        return statement.executeUpdate(update);
    }

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

    public static Database getInstance() {
        return instance;
    }

    public static RequestManager getRequestManager() {
        return manager;
    }
}
