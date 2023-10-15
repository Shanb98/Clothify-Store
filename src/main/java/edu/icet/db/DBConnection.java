package edu.icet.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static DBConnection dbConnection;
    private Connection connection;
    private DBConnection() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/clothify_store","root","Cwewwe_123");
    }

    public static DBConnection getInstance() throws SQLException, ClassNotFoundException {
        return dbConnection != null ? dbConnection:(dbConnection = new DBConnection());
    }

    public Connection getConnection(){
        return connection;
    }
}
