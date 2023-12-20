/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ooc;

/**
 * Provides a simple utility class for establishing a connection to a MySQL database.
 * Ensure that the MySQL JDBC driver is available in the classpath.
 *
 * @author joao
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    
// Database connection details
    private static final String URL = "jdbc:mysql://localhost:3306/oocdb";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    // Static block to load the MySQL JDBC driver when the class is loaded
    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("MySQL JDBC driver not found", e);
        }
    }


     //Establishes and returns a connection to the MySQL database.
     //return A Connection object representing the database connection.
     //throws RuntimeException if there is an error connecting to the database.
     
    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            throw new RuntimeException("Error connecting to the database", e);
        }
    }
}