/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ooc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

/**
 *
 * @author joao
 */
public class UserManager {
    
private Scanner scanner;

    public UserManager() {
        this.scanner = new Scanner(System.in);
    }

    public void registerUser() {
    System.out.println("Register New User");
    System.out.print("Enter username: ");
    String username = scanner.next();

    System.out.print("Enter password: ");
    String password = scanner.next(); 

    System.out.print("Enter first name: ");
    String firstName = scanner.next();

    System.out.print("Enter last name: ");
    String lastName = scanner.next();

    String sql = "INSERT INTO users (username, password, first_name, last_name, account_type) VALUES (?, ?, ?, ?, 'REGULAR')";

    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {

        pstmt.setString(1, username);
        pstmt.setString(2, password); // Remember to hash the password
        pstmt.setString(3, firstName);
        pstmt.setString(4, lastName);

        int affectedRows = pstmt.executeUpdate();

        if (affectedRows > 0) {
            System.out.println("User registered successfully!");
        } else {
            System.out.println("User registration failed.");
        }
    } catch (SQLException e) {
        System.out.println("Error registering user: " + e.getMessage());
    }
}
}
